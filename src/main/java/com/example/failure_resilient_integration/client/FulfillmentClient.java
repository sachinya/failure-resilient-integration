package com.example.failure_resilient_integration.client;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FulfillmentClient {

    private final Random random = new Random();

    public void fulfill(String orderId) {
        int r = random.nextInt(10);

        if (r < 4) { // ~40% failure
            throw new RuntimeException("Fulfillment system unavailable");
        }

        System.out.println("Order fulfilled: " + orderId);
    }
}
