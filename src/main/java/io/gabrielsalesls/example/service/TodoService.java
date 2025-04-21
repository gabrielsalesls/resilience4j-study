package io.gabrielsalesls.example.service;

import io.gabrielsalesls.example.client.TodoClient;
import io.gabrielsalesls.example.client.TodoClientB;
import io.gabrielsalesls.example.exception.ExternalServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    Logger logger = LoggerFactory.getLogger(TodoService.class);

    private final TodoClient todoClient;
    private final TodoClientB todoClientB;

    public TodoService(TodoClient todoClient, TodoClientB todoClientB) {
        this.todoClient = todoClient;
        this.todoClientB = todoClientB;
    }

    public String getTodos() {
        return todoClient.getTodos();
    }

    @CircuitBreaker(name = "backendA", fallbackMethod = "backendAFallback")
    @Retry(name = "retryBackEndA")
    public String getTodosToFail() {
        return todoClient.getTodosToFail();
    }

    @CircuitBreaker(name = "backendB", fallbackMethod = "backendBFallback")
    @Retry(name = "retryBackEndB")
    public String getTodosTimeout() {
        return todoClientB.getTimeout();
    }

    private String backendAFallback(Exception e) {
        logger.info("BackEnd A Fallback");
        throw new ExternalServiceUnavailableException("/fail", "Fail to call external service.", 500);
    }

    private String backendBFallback(CallNotPermittedException e) {
        logger.info("BackEnd B Fallback");
        return todoClient.getTodos();
    }
}
