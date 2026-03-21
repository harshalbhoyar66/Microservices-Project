package com.microservices.order.service;

import com.microservices.order.client.PaymentClient;
import com.microservices.order.client.ProductClient;
import com.microservices.order.dto.OrderRequest;
import com.microservices.order.entity.Orders;
import com.microservices.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Orders createOrder(OrderRequest request) {

        // 1. Fetch product
        Object product = productClient.getProduct(request.getProductId());

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // 2. Calculate amount (dummy logic)
        Double amount = 1000.0 * request.getQuantity();

        Orders order = new Orders();
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(amount);
        order.setStatus("CREATED");

        Orders savedOrder = repository.save(order);

        // 3. Call payment service
        Object paymentResponse =
                paymentClient.processPayment(savedOrder.getId(), amount);

        // 4. Update status (simple logic)
        savedOrder.setStatus("COMPLETED");

        return repository.save(savedOrder);
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