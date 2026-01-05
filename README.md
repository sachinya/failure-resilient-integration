# Failure-Resilient Order Integration Service

## Business Problem

In real-world distributed systems, retries, timeouts, and partial failures are unavoidable.  
If these cases aren’t handled carefully, they can result in duplicate payments, lost orders, or inconsistent data across systems.

This project shows how to design a backend service that remains **safe and predictable under failure**, by using idempotency, controlled retries, and explicit handling of partial success scenarios.

---

## Architecture Overview

The service exposes a single **Order API** that coordinates calls to two downstream systems:
- a **Payment Service**
- a **Fulfillment Service**

The goal of the design is **reliability and correctness**, rather than UI or database complexity.

### Responsibilities

**Order API**
- Accepts client requests
- Enforces idempotency
- Orchestrates the business flow
- Applies retries only when safe
- Captures partial failures instead of hiding them

**Downstream Systems**
- Payment Service (simulated and retryable)
- Fulfillment Service (simulated and may fail)

Downstream instability is intentional and used to demonstrate real-world failure scenarios.

---

#### API Contract 

** Request Body :
{
  "orderId": "ORD-101",
  "amount": 2500,
  "customerId": "C-9"
}

** Successful Response :
{
  "orderId": "ORD-101",
  "status": "CONFIRMED"
}

** Payment success but fulfillment fails :
{
    "orderId": "ORD-101",
    "status": "PAYMENT_SUCCESS_PENDING_FULFILLMENT"
}  

** Failure Response :
{
    "orderId": "ORD-101",
    "status": "FAILURE"
}   

##### Idempotency Strategy

Each client-generated Idempotency-Key represents a single user action and is reused across retries.
On the server side, the following are stored:
Idempotency key
Request hash
Final response

** Rules:
Same key and same request → response is replayed
Same key with a different request → 409 Conflict
Business logic is executed only once
This prevents duplicate side effects while still allowing safe retries.                 |

