package io.gabrielsalesls.example.service;

import io.gabrielsalesls.example.client.TodoClient;
import io.gabrielsalesls.example.client.TodoClientB;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoClient todoClient;
    private final TodoClientB todoClientB;

    public TodoService(TodoClient todoClient, TodoClientB todoClientB) {
        this.todoClient = todoClient;
        this.todoClientB = todoClientB;
    }

    public String getTodos() {
        return todoClient.getTodos();
    }

    @CircuitBreaker(name = "backendA")
    @Retry(name = "retryBackEndA")
    public String getTodosToFail() {
        return todoClient.getTodosToFail();
    }

    @CircuitBreaker(name = "backendB")
    @Retry(name = "retryBackEndB")
    public String getTodosTimeout() {
        return todoClientB.getTimeout();
    }
}
