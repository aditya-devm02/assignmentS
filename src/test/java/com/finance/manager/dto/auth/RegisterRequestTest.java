package com.finance.manager.dto.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterRequestTest {

    @Test
    void testRegisterRequestGettersAndSetters() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser@example.com");
        request.setPassword("password123");
        request.setFullName("Test User");
        request.setPhoneNumber("+1234567890");

        assertEquals("testuser@example.com", request.getUsername());
        assertEquals("password123", request.getPassword());
        assertEquals("Test User", request.getFullName());
        assertEquals("+1234567890", request.getPhoneNumber());
    }
} 