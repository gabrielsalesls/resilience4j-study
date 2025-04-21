package io.gabrielsalesls.example.controller.advice;

import feign.FeignException;
import io.gabrielsalesls.example.exception.ExternalServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandle.class);

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

    @ExceptionHandler(ExternalServiceUnavailableException.class)
    public ResponseEntity<String> handleExternalServiceUnavailable(ExternalServiceUnavailableException ex) {
        logger.error("External Service: " + ex.getExternalService() + " unavailable: " + ex.getMessage());
        HttpStatus status = switch (ex.getStatusCode()) {
            case 503 -> HttpStatus.SERVICE_UNAVAILABLE;
            case 504 -> HttpStatus.GATEWAY_TIMEOUT;
            default -> HttpStatus.BAD_GATEWAY;
        };
        return new ResponseEntity<>(ex.getMessage(), status);
    }
}