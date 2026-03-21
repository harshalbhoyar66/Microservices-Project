package com.microservices.order.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public Object getProduct(Long productId) {
        return restTemplate.getForObject(
                "http://localhost:8082/api/v1/products/" + productId,
                Object.class
        );
    }
}