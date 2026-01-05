package com.example.failure_resilient_integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdempotencyRecord {
    private String key;
    private String requestHash;
    private OrderResponse response;
}
