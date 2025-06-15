package com.finance.manager.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for user login.
 * Contains credentials required for user authentication.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class LoginRequest {
    /** User's email address. Must not be blank. */
    @NotBlank
    private String username;

    /** User's password. Must not be blank. */
    @NotBlank
    private String password;
} 