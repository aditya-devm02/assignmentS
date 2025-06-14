package com.finance.manager.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRegistrationRequestTest {

    @Test
    void testUserRegistrationRequestGettersAndSetters() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("testuser@example.com");
        request.setPassword("password123");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPhoneNumber("+1234567890");

        assertEquals("testuser@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("Test", request.getFirstName());
        assertEquals("User", request.getLastName());
        assertEquals("+1234567890", request.getPhoneNumber());
    }
} 