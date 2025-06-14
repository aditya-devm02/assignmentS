package com.finance.manager.service;

import com.finance.manager.entity.User;
import com.finance.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[DEBUG] CustomUserDetailsService: Attempting to load user by username: " + username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("[DEBUG] CustomUserDetailsService: User not found for username: " + username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
        System.out.println("[DEBUG] CustomUserDetailsService: Found user: " + user.getUsername() + ", Password (encoded): " + user.getPassword().substring(0, Math.min(10, user.getPassword().length())) + "...");
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // You can customize roles if needed
                .build();
    }
} 