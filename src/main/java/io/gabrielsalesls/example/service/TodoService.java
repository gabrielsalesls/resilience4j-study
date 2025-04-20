package io.gabrielsalesls.example.service;

import io.gabrielsalesls.example.client.TodoClient;
import io.gabrielsalesls.example.client.TodoClientB;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    public String getTodosToFail() {
        return todoClient.getTodosToFail();
    }

    @CircuitBreaker(name = "backendB")
    public String getTodosTimeout() {
        return todoClientB.getTimeout();
    }
}
