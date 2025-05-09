# Resilience Study
> 📝 This README was written with the assistance of generative AI.

This project is a study and demonstration of using [Resilience4j](https://resilience4j.readme.io/) to combine the Retry and Circuit Breaker patterns for robust error handling in Java microservices.

## Running the Project

1. In local folder, run docker-compose up.
2. Use the provided endpoints at postman folder to trigger errors.
3. Observe the retry and circuit breaker behavior in the logs and http responses.

## Overview

The application exposes two endpoints, both configured to simulate errors using WireMock. Each endpoint demonstrates different retry strategies and their interaction with the circuit breaker pattern:

- **Endpoint 1:** Configured with 5 retry attempts using exponential backoff.
- **Endpoint 2:** Configured with 3 fixed retry attempts.

A key configuration in this project is the adjustment of the `retry-aspect-order`, ensuring that retries are executed before the circuit breaker logic is applied. This means that all retry attempts are treated as a single call from the circuit breaker's perspective, preventing the circuit breaker from prematurely opening due to multiple retry attempts for the same request.

### Retry Pattern

The **Retry** pattern allows an operation to be automatically retried a specified number of times before finally failing.

- **Exponential Backoff:** For the first endpoint, retries are performed with increasing wait durations between attempts. For example, with a base wait duration of 5 seconds and a multiplier of 2, the delays will be 5s, 10s, 20s, etc.
- **Fixed Attempts:** The second endpoint uses a fixed number of retry attempts without exponential backoff.

**Retry Configuration:**
```yaml
resilience4j.retry:
  retry-aspect-order: 2
  metrics:
    legacy:
      enabled: true
    enabled: true
  instances:
    retryBackEndA:
      maxAttempts: 5
      waitDuration: 500ms
      enableExponentialBackoff: true
      exponentialBackoffMultiplier: 2
      ignoreExceptions:
        - io.github.resilience4j.circuitbreaker.CallNotPermittedException
    retryBackEndB:
      maxAttempts: 3
      waitDuration: 1s
      ignoreExceptions:
        - io.github.resilience4j.circuitbreaker.CallNotPermittedException
```

### Circuit Breaker Pattern

The **Circuit Breaker** pattern prevents a system from making requests to a failing service. It works in three states:

- **Closed:** Requests are allowed through. If failures reach a threshold, the breaker opens.
- **Open:** Requests are rejected immediately for a configured period.
- **Half-Open:** A limited number of test requests are allowed through to determine if the service has recovered.

**Circuit Breaker Configuration:**
```yaml
resilience4j.circuitbreaker:
  circuitbreaker-aspect-order: 1
  configs:
    forBackEndA:
      sliding-window-size: 3
      failure-rate-threshold: 50
      wait-duration-in-open-state: 15s
      minimum-number-of-calls: 5
      permitted-number-of-calls-in-half-open-state: 3
    forBackEndB:
      sliding-window-size: 10
      failure-rate-threshold: 50
      wait-duration-in-open-state: 30s
      minimum-number-of-calls: 10
      permitted-number-of-calls-in-half-open-state: 5
  instances:
    backendA:
      baseConfig: forBackEndA
    backendB:
      baseConfig: forBackEndB
logging:
  level:
    io.github.resilience4j: DEBUG
    org.springframework.cloud.openfeign: DEBUG
```

### Aspect Order

By configuring the `retry-aspect-order` to a higher value than `circuit-breaker-aspect-order`, retries are executed before the circuit breaker logic. This ensures that all retry attempts for a single operation are counted as one call by the circuit breaker, avoiding unintended trips to the open state.

### Timeout Considerations

During the development of this study, configuring individual timeouts for each endpoint was considered. However, implementing this would require modifying the functions to work asynchronously, for example using `CompletableFuture` or reactive types like `Mono`/`Flux`. Due to these limitations, the TimeLimiter is configured as a global fallback:

```yaml
resilience4j.timelimiter:
  configs:
    default:
      timeout-duration: 2s
```

### Known Limitations

- **Synchronous Execution and Timeout Control:**  
  Due to the use of synchronous method calls in this project, it is not possible to configure individual timeouts per endpoint. Resilience4j’s `TimeLimiter` requires asynchronous execution (e.g., with `CompletableFuture`, `Mono`, or `Flux`) to properly interrupt long-running operations. As a result, all endpoints share the same global timeout configuration, which may not reflect real-world scenarios where each operation might have different latency tolerances.

- **Fixed Timeout Configuration:**  
  The current setup uses a single, fixed timeout duration defined in the `TimeLimiter` default config. While this simplifies the configuration, it limits the flexibility to tailor timeout behavior based on the criticality or expected response time of each endpoint.