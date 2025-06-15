package com.finance.manager.service.impl;

import com.finance.manager.dto.auth.RegisterRequest;
import com.finance.manager.entity.User;
import com.finance.manager.exception.BadRequestException;
import com.finance.manager.repository.UserRepository;
import com.finance.manager.service.UserService;
import com.finance.manager.service.CategoryService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the UserService interface.
 * Handles user registration and retrieval operations with password encryption
 * and default category creation.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;

    /**
     * Constructs a UserServiceImpl with required dependencies.
     *
     * @param userRepository Repository for user data access
     * @param passwordEncoder Encoder for password hashing
     * @param categoryService Service for category operations
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CategoryService categoryService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
    }

    /**
     * Registers a new user with encrypted password and default categories.
     * Validates username uniqueness before registration.
     *
     * @param request Registration request containing user details
     * @return The registered user entity
     * @throws BadRequestException if username already exists
     */
    @Override
    @Transactional
    public User registerUser(RegisterRequest request) {
        System.out.println("[DEBUG] Registering user: " + request.getUsername() + ", password (raw): " + request.getPassword().substring(0, Math.min(5, request.getPassword().length())) + "...");
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        System.out.println("[DEBUG] Encoded password: " + encodedPassword.substring(0, Math.min(10, encodedPassword.length())) + "...");
        user.setPassword(encodedPassword);
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        User savedUser = userRepository.save(user);
        System.out.println("[DEBUG] User saved: " + savedUser.getUsername() + ", ID: " + savedUser.getId());
        categoryService.createDefaultCategories(savedUser);
        return savedUser;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username to search for
     * @return The user entity
     * @throws BadRequestException if user is not found
     */
    @Override
    public User getUserByUsername(String username) {
        System.out.println("[DEBUG] Looking up user by username: " + username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));
        System.out.println("[DEBUG] Found user: " + user);
        return user;
    }
} 