package com.example.failure_resilient_integration.client;

import org.springframework.stereotype.Component;

@Component
public class FulfillmentClient {

    public void fulfill(String orderId) {
        System.out.println("Order fulfilled: " + orderId);
    }
}
