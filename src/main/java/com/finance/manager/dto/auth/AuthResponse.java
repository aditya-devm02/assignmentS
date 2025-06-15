package com.finance.manager.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for authentication operations.
 * Used to return authentication results to clients.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    /** Message describing the result of the authentication operation */
    private String message;

    /** ID of the authenticated user */
    private Long userId;

    /** JWT token for authenticated sessions */
    private String token;

    /**
     * Constructs an AuthResponse without a token.
     * Used for operations that don't require a token, such as registration.
     *
     * @param message Message describing the result
     * @param userId ID of the user
     */
    public AuthResponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }
} 