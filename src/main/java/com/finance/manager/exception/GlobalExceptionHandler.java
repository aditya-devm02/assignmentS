package com.finance.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling and consistent error responses across all controllers.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException by returning a 404 Not Found response.
     *
     * @param ex The ResourceNotFoundException that was thrown
     * @return ResponseEntity containing error message and 404 status code
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles BadRequestException by returning a 400 Bad Request response.
     *
     * @param ex The BadRequestException that was thrown
     * @return ResponseEntity containing error message and 400 status code
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles BadCredentialsException by returning a 401 Unauthorized response.
     *
     * @param ex The BadCredentialsException that was thrown
     * @return ResponseEntity containing error message and 401 status code
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid username or password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles validation exceptions by returning a 400 Bad Request response with field-specific errors.
     *
     * @param ex The MethodArgumentNotValidException that was thrown
     * @return ResponseEntity containing field-specific error messages and 400 status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles invalid request format exceptions by returning a 400 Bad Request response.
     *
     * @param ex The HttpMessageNotReadableException that was thrown
     * @return ResponseEntity containing error message and 400 status code
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid request format or date format. Please use yyyy-MM-dd.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles data integrity violation exceptions by returning a 409 Conflict response.
     *
     * @param ex The DataIntegrityViolationException that was thrown
     * @return ResponseEntity containing error message and 409 status code
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Category is referenced by transactions and cannot be deleted.");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Handles all unhandled exceptions by returning a 500 Internal Server Error response.
     *
     * @param ex The Exception that was thrown
     * @return ResponseEntity containing generic error message and 500 status code
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "An unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 