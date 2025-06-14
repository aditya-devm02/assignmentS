package com.finance.manager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtBlacklistService blacklistService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter filter;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(tokenProvider, userDetailsService, blacklistService);
        userDetails = new User("testuser@example.com", "password", Collections.emptyList());
    }

    @Test
    void doFilterInternal_PublicEndpoint() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/auth/login");
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        verify(tokenProvider, never()).validateToken(any());
    }

    @Test
    void doFilterInternal_ValidTokenInHeader() throws ServletException, IOException {
        String token = "valid.jwt.token";
        when(request.getServletPath()).thenReturn("/api/transactions");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(blacklistService.isTokenBlacklisted(token)).thenReturn(false);
        when(tokenProvider.getUsernameFromToken(token)).thenReturn("testuser@example.com");
        when(userDetailsService.loadUserByUsername("testuser@example.com")).thenReturn(userDetails);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenProvider).validateToken(token);
        verify(userDetailsService).loadUserByUsername("testuser@example.com");
    }

    @Test
    void doFilterInternal_ValidTokenInCookie() throws ServletException, IOException {
        String token = "valid.jwt.token";
        Cookie[] cookies = new Cookie[]{new Cookie("SESSION", token)};
        when(request.getServletPath()).thenReturn("/api/transactions");
        when(request.getCookies()).thenReturn(cookies);
        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(blacklistService.isTokenBlacklisted(token)).thenReturn(false);
        when(tokenProvider.getUsernameFromToken(token)).thenReturn("testuser@example.com");
        when(userDetailsService.loadUserByUsername("testuser@example.com")).thenReturn(userDetails);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenProvider).validateToken(token);
        verify(userDetailsService).loadUserByUsername("testuser@example.com");
    }

    @Test
    void doFilterInternal_InvalidToken() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/transactions");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token");
        when(tokenProvider.validateToken("invalid.token")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenProvider).validateToken("invalid.token");
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void doFilterInternal_BlacklistedToken() throws ServletException, IOException {
        String token = "blacklisted.token";
        when(request.getServletPath()).thenReturn("/api/transactions");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(blacklistService.isTokenBlacklisted(token)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenProvider, never()).validateToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void doFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getServletPath()).thenReturn("/api/transactions");
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenProvider, never()).validateToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }
} 