package com.example.failure_resilient_integration.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    @NotBlank
    private String orderId;

    @NotNull
    private Integer amount;

    @NotBlank
    private String customerId;
}
