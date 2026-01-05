package com.example.failure_resilient_integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private OrderStatus status;
}
