package io.gabrielsalesls.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "backendB", url = "http://localhost:8383/api/v2")
public interface TodoClientB {

    @GetMapping("todos/fail/timeout")
    String getTimeout();

}
