# Failure-Resilient Order Integration Service

## Business Problem

In real-world distributed systems, retries, timeouts, and partial failures are unavoidable.
If these scenarios are not handled carefully, they can lead to duplicate payments, lost orders, or inconsistent state across systems.

This project demonstrates how to design a backend service that remains **safe, predictable, and correct under failure** by applying:

* Idempotency
* Controlled retries
* Explicit handling of partial success scenarios

---

## Architecture Overview

The service exposes a single **Order API** that coordinates calls to two downstream systems:

* **Payment Service**
* **Fulfillment Service**

The primary design goal is **reliability and correctness**, not UI or database complexity.

### Component Responsibilities

#### Order API

* Accepts client requests
* Enforces idempotency
* Orchestrates the business workflow
* Applies retries only when safe
* Captures partial failures explicitly instead of hiding them

#### Downstream Systems

* **Payment Service** – simulated, retryable, may experience transient failures
* **Fulfillment Service** – simulated, may fail independently

Downstream instability is intentional and used to model real-world failure scenarios.

---

## API Contract

### Request Body

```json
{
  "orderId": "ORD-101",
  "amount": 2500,
  "customerId": "C-9"
}
```

### Successful Response

```json
{
  "orderId": "ORD-101",
  "status": "CONFIRMED"
}
```

### Payment Succeeds but Fulfillment Fails

```json
{
  "orderId": "ORD-101",
  "status": "PAYMENT_SUCCESS_PENDING_FULFILLMENT"
}
```

### Failure Response

```json
{
  "orderId": "ORD-101",
  "status": "FAILURE"
}
```

---

## Idempotency Strategy

Each client-generated **Idempotency-Key** represents a single user action and must be reused across retries.

On the server side, the following data is stored:

* Idempotency key
* Request hash
* Final response

### Rules

* Same key + same request → previously stored response is replayed
* Same key + different request → `409 Conflict`
* Business logic is executed only once per key

This prevents duplicate side effects while still allowing safe client retries.

---

## Retry Strategy

Retries are applied **only** for transient failures such as:

* Network timeouts
* Temporary downstream outages

### Retry Characteristics

* Maximum of 3 attempts
* Simple backoff between retries
* Applied only to idempotent operations

Permanent errors and business rule violations are **never retried**.

---

## Partial Failure Handling

Distributed systems rarely fail in an all-or-nothing manner.

### Scenario: Payment succeeds but fulfillment fails

* Order is marked as `PAYMENT_SUCCESS_PENDING_FULFILLMENT`
* Payment is **not retried**
* System remains in a consistent state
* Fulfillment can be safely retried later

This approach avoids duplicate charges while preserving the correct business state.

---

## Key Takeaway

This project focuses on **failure-resilient backend design**, demonstrating how to:

* Protect against duplicate side effects
* Handle partial success explicitly
* Build predictable behavior in unreliable environments

---
