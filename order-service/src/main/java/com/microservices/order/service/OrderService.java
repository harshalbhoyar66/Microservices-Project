package com.microservices.order.service;

import com.microservices.order.client.PaymentClient;
import com.microservices.order.client.ProductClient;
import com.microservices.order.dto.OrderRequest;
import com.microservices.order.entity.Orders;
import com.microservices.order.repository.OrderRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderService(OrderRepository repository,
                        ProductClient productClient,
                        PaymentClient paymentClient) {
        this.repository = repository;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    // 🚀 MAIN FLOW
    public Orders createOrder(OrderRequest request) {

        // ✅ PRODUCT CALL WITH CB + TIMELIMITER
        Object product;
        try {
            product = getProductWithCB(request.getProductId()).join();
        } catch (Exception ex) {
            throw new RuntimeException("Product Service Timeout/Failure");
        }

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // 2. Calculate amount
        Double amount = 1000.0 * request.getQuantity();

        Orders order = new Orders();
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(amount);
        order.setStatus("CREATED");

        Orders savedOrder = repository.save(order);

        // 🔥 PAYMENT CALL (UNCHANGED)
        callPayment(savedOrder.getId(), amount);

        // 4. Update status
        savedOrder.setStatus("COMPLETED");

        return repository.save(savedOrder);
    }

    @Bulkhead(name = "productServiceBulkhead", type = Bulkhead.Type.SEMAPHORE)
    @TimeLimiter(name = "productServiceCB")
    @CircuitBreaker(name = "productServiceCB", fallbackMethod = "productFallback")
    public CompletableFuture<Object> getProductWithCB(Long productId) {

        return CompletableFuture.supplyAsync(() -> {
            System.out.println("👉 Calling Product Service...");
            return productClient.getProduct(productId);
        });
    }

    // 🔥 PRODUCT FALLBACK (FAIL FAST)
    public CompletableFuture<Object> productFallback(Long productId, Throwable ex) {

        System.out.println("🔥 Product Service failed or timed out: " + ex.getMessage());

        throw new RuntimeException("Product Service Down/Slow - Cannot place order");
    }

    // 🔥 PAYMENT CB (UNCHANGED)
    @CircuitBreaker(name = "paymentServiceCB", fallbackMethod = "paymentFallback")
    @Retry(name = "paymentServiceRetry")
    public void callPayment(Long orderId, Double amount) {

        System.out.println("👉 Calling Payment Service...");

        paymentClient.processPayment(orderId, amount);
    }

    // 🔥 PAYMENT FALLBACK (UNCHANGED)
    public void paymentFallback(Long orderId, Double amount, Throwable ex) {

        ex.printStackTrace();

        System.out.println("🔥 Payment fallback triggered: " + ex.getMessage());

        Orders order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("PAYMENT_PENDING");

        repository.save(order);
    }

    public Orders getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Orders> getAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}