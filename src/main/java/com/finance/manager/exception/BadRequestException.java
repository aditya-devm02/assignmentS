package com.finance.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a request contains invalid or malformed data.
 * Results in a 400 Bad Request HTTP response.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    /**
     * Constructs a new BadRequestException with the specified error message.
     *
     * @param message The detail message explaining the reason for the bad request
     */
    public BadRequestException(String message) {
        super(message);
    }
} 