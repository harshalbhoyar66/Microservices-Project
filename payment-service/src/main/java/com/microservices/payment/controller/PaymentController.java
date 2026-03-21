package com.microservices.payment.controller;

import com.microservices.payment.dto.PaymentRequest;
import com.microservices.payment.entity.Payment;
import com.microservices.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public Payment process(@Valid @RequestBody PaymentRequest request) {
        return service.processPayment(request);
    }

    @GetMapping
    public List<Payment> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Payment getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/order/{orderId}")
    public List<Payment> getByOrderId(@PathVariable Long orderId) {
        return service.getByOrderId(orderId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Payment deleted";
    }
}