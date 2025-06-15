package com.finance.manager.service;

import com.finance.manager.dto.auth.RegisterRequest;
import com.finance.manager.entity.User;

/**
 * Service interface for user-related operations.
 * Handles user registration and retrieval functionality.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
public interface UserService {
    /**
     * Registers a new user in the system.
     *
     * @param request Registration request containing user details
     * @return The registered user entity
     * @throws BadRequestException if username already exists
     */
    User registerUser(RegisterRequest request);

    /**
     * Retrieves a user by their username.
     *
     * @param username The username to search for
     * @return The user entity
     * @throws BadRequestException if user is not found
     */
    User getUserByUsername(String username);
} 