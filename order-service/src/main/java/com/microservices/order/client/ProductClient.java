package com.microservices.order.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    private final RestTemplate restTemplate;

    // ✅ Inject RestTemplate
    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Object getProduct(Long productId) {
        return restTemplate.getForObject(
                "http://product-service/api/v1/products/" + productId, // 🔥 IMPORTANT CHANGE
                Object.class
        );
    }
}