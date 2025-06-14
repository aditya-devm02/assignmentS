package com.finance.manager.dto.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginRequestTest {

    @Test
    void testLoginRequestGettersAndSetters() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser@example.com");
        request.setPassword("password123");

        assertEquals("testuser@example.com", request.getUsername());
        assertEquals("password123", request.getPassword());
    }
} 