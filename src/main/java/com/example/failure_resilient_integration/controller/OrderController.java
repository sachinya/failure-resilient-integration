package com.example.failure_resilient_integration.controller;

import com.example.failure_resilient_integration.model.OrderRequest;
import com.example.failure_resilient_integration.model.OrderResponse;
import com.example.failure_resilient_integration.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody OrderRequest request
    ) {
        OrderResponse response =
                orderService.createOrder(request, idempotencyKey);

        return ResponseEntity.ok(response);
    }
}
