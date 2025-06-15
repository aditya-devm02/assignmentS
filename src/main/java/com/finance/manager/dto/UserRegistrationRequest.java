package com.finance.manager.dto;

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
public class UserRegistrationRequest {
    /** User's email address. Must be a valid email format. */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /** User's password. Must not be blank. */
    @NotBlank(message = "Password is required")
    private String password;

    /** User's first name. Must not be blank. */
    @NotBlank(message = "First name is required")
    private String firstName;

    /** User's last name. Must not be blank. */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /** User's phone number. Must match the pattern of 10-15 digits, optionally starting with '+'. */
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;
} 