package io.gabrielsalesls.example.controller.advice;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(FeignException.InternalServerError.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException.InternalServerError ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now().toString());
        response.put("message", "Error calling external API");
        response.put("statusCode", ex.status());
        response.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(response);
    }
}