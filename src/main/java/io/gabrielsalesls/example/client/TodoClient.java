package io.gabrielsalesls.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "todoClient", url = "http://localhost:8383/api/v1")
public interface TodoClient {

    @GetMapping("todos")
    String getTodos();

    @GetMapping("todos/fail")
    String getTodosToFail();

}
