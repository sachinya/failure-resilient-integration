package com.example.failure_resilient_integration.client;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PaymentClient {

    private final Random random = new Random();

    public void charge(String orderId, Integer amount) {
        int r = random.nextInt(10);

        if (r < 3) { // ~30% failure
            throw new RuntimeException("Payment service timeout");
        }

        System.out.println("Payment successful for order " + orderId);
    }
}
