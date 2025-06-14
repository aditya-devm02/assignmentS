package com.finance.manager.service;

import com.finance.manager.dto.auth.RegisterRequest;
import com.finance.manager.entity.User;
import com.finance.manager.exception.BadRequestException;
import com.finance.manager.repository.UserRepository;
import com.finance.manager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequest registrationRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegisterRequest();
        registrationRequest.setUsername("test@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setFullName("John Doe");
        registrationRequest.setPhoneNumber("+1234567890");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername(registrationRequest.getUsername());
        testUser.setFullName(registrationRequest.getFullName());
        testUser.setPhoneNumber(registrationRequest.getPhoneNumber());
    }

    @Test
    void registerUser_Success() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        doNothing().when(categoryService).createDefaultCategories(any(User.class));

        // Act
        User registeredUser = userService.registerUser(registrationRequest);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(registrationRequest.getUsername(), registeredUser.getUsername());
        assertEquals(registrationRequest.getFullName(), registeredUser.getFullName());
        verify(userRepository, times(1)).save(any(User.class));
        verify(categoryService, times(1)).createDefaultCategories(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.registerUser(registrationRequest));
        verify(userRepository, never()).save(any(User.class));
        verify(categoryService, never()).createDefaultCategories(any(User.class));
    }

    @Test
    void getUserByUsername_Success() {
        // Arrange
        when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userService.getUserByUsername("test@example.com");

        // Assert
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getUsername());
    }

    @Test
    void getUserByUsername_NotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> userService.getUserByUsername("nonexistent@example.com"));
    }
} 