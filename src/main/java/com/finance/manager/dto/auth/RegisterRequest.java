package com.finance.manager.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Request DTO for user registration.
 * Contains all necessary information for creating a new user account with validation constraints.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class RegisterRequest {
    /** User's email address. Must be a valid email format. */
    @NotBlank
    @Email
    private String username;

    /** User's password. Must not be blank. */
    @NotBlank
    private String password;

    /** User's full name. Must not be blank. */
    @NotBlank
    private String fullName;

    /** User's phone number. Must start with '+' and contain 10 to 15 digits. */
    @NotBlank
    @Pattern(regexp = "^\\+[0-9]{10,15}$")
    private String phoneNumber;
} 