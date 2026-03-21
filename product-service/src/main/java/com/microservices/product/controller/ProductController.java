package com.microservices.product.controller;

import com.microservices.product.dto.ProductRequest;
import com.microservices.product.entity.Product;
import com.microservices.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public Product create(@Valid @RequestBody ProductRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String name) {
        return service.search(name);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id,
                          @Valid @RequestBody ProductRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Product deleted";
    }
}