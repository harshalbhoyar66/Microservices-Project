package com.microservices.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced   // 🔥 THIS ENABLES EUREKA + LOAD BALANCING
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}