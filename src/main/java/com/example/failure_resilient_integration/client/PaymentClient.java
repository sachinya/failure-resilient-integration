package com.example.failure_resilient_integration.client;

import org.springframework.stereotype.Component;

@Component
public class PaymentClient {

    public void charge(String orderId, Integer amount) {
        System.out.println("Payment successful for order " + orderId);
    }
}
