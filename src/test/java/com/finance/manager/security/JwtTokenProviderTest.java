package com.finance.manager.security;


import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private Authentication authentication;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret", "testSecretKey123456789012345678901234567890");
        ReflectionTestUtils.setField(tokenProvider, "jwtExpiration", 3600000L); // 1 hour

        userDetails = new User(
            "testuser@example.com",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void generateToken_Success() {
        String token = tokenProvider.generateToken(authentication);
        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
        assertEquals(userDetails.getUsername(), tokenProvider.getUsernameFromToken(token));
    }

    @Test
    void validateToken_ValidToken() {
        String token = tokenProvider.generateToken(authentication);
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_InvalidToken() {
        assertFalse(tokenProvider.validateToken("invalid.token.here"));
    }

    @Test
    void validateToken_ExpiredToken() {
        ReflectionTestUtils.setField(tokenProvider, "jwtExpiration", -3600000L); // -1 hour
        String token = tokenProvider.generateToken(authentication);
        assertFalse(tokenProvider.validateToken(token));
    }

    @Test
    void getUsernameFromToken_Success() {
        String token = tokenProvider.generateToken(authentication);
        assertEquals(userDetails.getUsername(), tokenProvider.getUsernameFromToken(token));
    }

    @Test
    void getUsernameFromToken_InvalidToken() {
        assertThrows(MalformedJwtException.class, () -> tokenProvider.getUsernameFromToken("invalid.token.here"));
    }

    @Test
    void getJwtExpiration() {
        assertEquals(3600000L, tokenProvider.getJwtExpiration());
    }
}