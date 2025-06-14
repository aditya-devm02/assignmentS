package com.finance.manager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtBlacklistServiceTest {

    private JwtBlacklistService blacklistService;

    @BeforeEach
    void setUp() {
        blacklistService = new JwtBlacklistService();
    }

    @Test
    void blacklistToken_Success() {
        String token = "test.token.here";
        blacklistService.blacklistToken(token);
        assertTrue(blacklistService.isTokenBlacklisted(token));
    }

    @Test
    void isTokenBlacklisted_NotBlacklisted() {
        String token = "test.token.here";
        assertFalse(blacklistService.isTokenBlacklisted(token));
    }

    @Test
    void blacklistToken_MultipleTokens() {
        String token1 = "token1";
        String token2 = "token2";
        String token3 = "token3";

        blacklistService.blacklistToken(token1);
        blacklistService.blacklistToken(token2);
        blacklistService.blacklistToken(token3);

        assertTrue(blacklistService.isTokenBlacklisted(token1));
        assertTrue(blacklistService.isTokenBlacklisted(token2));
        assertTrue(blacklistService.isTokenBlacklisted(token3));
    }

    @Test
    void blacklistToken_DuplicateToken() {
        String token = "test.token.here";
        blacklistService.blacklistToken(token);
        blacklistService.blacklistToken(token); // Add same token again
        assertTrue(blacklistService.isTokenBlacklisted(token));
    }
} 