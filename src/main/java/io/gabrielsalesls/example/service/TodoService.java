package io.gabrielsalesls.example.service;

import io.gabrielsalesls.example.client.TodoClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoClient todoClient;

    public TodoService(TodoClient todoClient) {
        this.todoClient = todoClient;
    }

    public String getTodos() {
        return todoClient.getTodos();
    }

    public String getTodosToFail() {
        return todoClient.getTodosToFail();
    }
}
