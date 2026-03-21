package com.microservices.order.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public Object processPayment(Long orderId, Double amount) {

        Map<String, Object> request = new HashMap<>();
        request.put("orderId", orderId);
        request.put("amount", amount);

        return restTemplate.postForObject(
                "http://localhost:8083/api/v1/payments",
                request,
                Object.class
        );
    }
}