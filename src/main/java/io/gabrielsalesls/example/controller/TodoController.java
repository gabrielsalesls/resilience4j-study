package io.gabrielsalesls.example.controller;

import io.gabrielsalesls.example.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<String> getTodos() {
        var data = service.getTodos();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> getTodosToFail() {
        var data = service.getTodosToFail();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/fail/timeout")
    public ResponseEntity<String> getTodosTimeout() {
        var data = service.getTodosTimeout();
        return ResponseEntity.ok(data);
    }

}
