package com.microservices.order.controller;

import com.microservices.order.dto.OrderRequest;
import com.microservices.order.entity.Orders;
import com.microservices.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public Orders create(@Valid @RequestBody OrderRequest request) {
        return service.createOrder(request);
    }

    @GetMapping
    public List<Orders> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Orders getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Order deleted";
    }
}