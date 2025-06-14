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

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CategoryService categoryService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.categoryService = categoryService;
    }

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

    @Override
    public User getUserByUsername(String username) {
        System.out.println("[DEBUG] Looking up user by username: " + username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("User not found"));
        System.out.println("[DEBUG] Found user: " + user);
        return user;
    }
} 