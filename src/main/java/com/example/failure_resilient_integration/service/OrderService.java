package com.example.failure_resilient_integration.service;

import com.example.failure_resilient_integration.client.FulfillmentClient;
import com.example.failure_resilient_integration.client.PaymentClient;
import com.example.failure_resilient_integration.exception.IdempotencyConflictException;
import com.example.failure_resilient_integration.model.*;
import com.example.failure_resilient_integration.repository.IdempotencyRepository;
import com.example.failure_resilient_integration.util.RequestHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentClient paymentClient;
    private final FulfillmentClient fulfillmentClient;
    private final IdempotencyRepository idempotencyRepository;

    public OrderResponse createOrder(OrderRequest request, String idempotencyKey) {

        String requestHash = RequestHashUtil.hash(request.toString());

        // Check idempotency store
        return idempotencyRepository.find(idempotencyKey)
                .map(record -> {
                    if (!record.getRequestHash().equals(requestHash)) {
                        throw new IdempotencyConflictException(
                                "Same idempotency key used with different request");
                    }
                    return record.getResponse(); // replay
                })
                .orElseGet(() -> {

                    paymentClient.charge(request.getOrderId(), request.getAmount());
                    fulfillmentClient.fulfill(request.getOrderId());

                    OrderResponse response = new OrderResponse(
                            request.getOrderId(),
                            OrderStatus.CONFIRMED);

                    idempotencyRepository.save(
                            new IdempotencyRecord(
                                    idempotencyKey,
                                    requestHash,
                                    response));

                    return response;
                });
    }
}
