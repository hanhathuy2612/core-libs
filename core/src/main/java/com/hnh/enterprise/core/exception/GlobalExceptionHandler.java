package com.hnh.enterprise.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that intercepts common exceptions across all
 * controllers and returns a structured JSON response.  By extending
 * {@link ResponseEntityExceptionHandler} we can override Spring's default
 * validation error handling while still benefiting from its internal logic.
 * <p>
 * This implementation returns a simple map containing a timestamp,
 * an error code and a human–readable message.  In a production system you
 * would likely want to return a domain–specific response object such as
 * BaseResponse / ErrorResponse.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Catch all handler for any uncaught exceptions.  Returns a generic
     * internal server error response.  You can add more specific handlers
     * for domain exceptions annotated with {@link ExceptionHandler}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        Map<String, Object> body = defaultBody("INTERNAL_SERVER_ERROR", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private Map<String, Object> defaultBody(String code, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("code", code);
        body.put("message", message);
        return body;
    }
}