package com.finance.manager.security;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing blacklisted JWT tokens.
 * Maintains a thread-safe set of invalidated tokens to prevent their reuse.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class JwtBlacklistService {
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    /**
     * Adds a token to the blacklist.
     * Once blacklisted, the token cannot be used for authentication.
     *
     * @param token The JWT token to blacklist
     */
    public void blacklistToken(String token) {
        blacklist.add(token);
    }

    /**
     * Checks if a token is blacklisted.
     *
     * @param token The JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
} 