package com.jns.app_manager.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleObjectNotFound
            (
                ObjectNotFoundException ex,
                HttpServletRequest request
            ) {

        Map<String, Object> problem = new LinkedHashMap<>();
        problem.put("type", "https://app-manager.jns.com/errors/object-not-found");
        problem.put("title", ex.getMessage());
        problem.put("status", HttpStatus.NOT_FOUND.value());
        problem.put("instance", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ViolationIntegrityException.class)
    public ResponseEntity<Map<String, Object>> handleViolationIntegrity(
            ViolationIntegrityException ex,
            HttpServletRequest request
    ) {
        Map<String, Object> problem = new LinkedHashMap<>();
        problem.put("type", "https://app-manager.jns.com/errors/integrity-violation");
        problem.put("title", ex.getMessage());
        problem.put("status", HttpStatus.CONFLICT.value());
        problem.put("instance", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
}