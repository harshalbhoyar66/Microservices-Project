package com.microservices.product.service;

import com.microservices.product.dto.ProductRequest;
import com.microservices.product.entity.Product;
import com.microservices.product.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        System.out.println("✅ Product created: " + product.getName());

        return repository.save(product);
    }

    public List<Product> getAll() {
        System.out.println("👉 Fetching all products...");
        return repository.findAll();
    }

    public Product getById(Long id) {
        System.out.println("👉 Fetching product with ID: " + id);

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with id: " + id
                ));
    }

    public List<Product> search(String name) {
        System.out.println("👉 Searching product: " + name);
        return repository.findByNameContaining(name);
    }

    public Product update(Long id, ProductRequest request) {
        Product product = getById(id);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        System.out.println("✅ Product updated: " + id);

        return repository.save(product);
    }

    public void delete(Long id) {
        System.out.println("🗑 Deleting product: " + id);
        repository.deleteById(id);
    }
}