package com.finance.manager.dto.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthResponseTest {

    @Test
    void testAuthResponseGettersAndSetters() {
        AuthResponse response = new AuthResponse();
        response.setMessage("Login successful");
        response.setUserId(1L);
        response.setToken("some.jwt.token");

        assertEquals("Login successful", response.getMessage());
        assertEquals(1L, response.getUserId());
        assertEquals("some.jwt.token", response.getToken());
    }
} 