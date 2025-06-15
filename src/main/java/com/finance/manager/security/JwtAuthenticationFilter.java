package com.finance.manager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * JWT Authentication Filter that processes incoming requests to validate JWT tokens.
 * This filter intercepts all requests and performs JWT-based authentication.
 * It skips authentication for public endpoints and validates tokens for protected endpoints.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    final JwtTokenProvider tokenProvider;
    final UserDetailsService userDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JwtBlacklistService jwtBlacklistService;

    // Public endpoints using Ant-style patterns
    private static final String[] PUBLIC_ENDPOINTS = {
        "/auth/register",
        "/auth/login",
        "/auth/logout"
    };

    /**
     * Constructs a JwtAuthenticationFilter with required dependencies.
     *
     * @param tokenProvider Service for JWT token operations
     * @param userDetailsService Service for loading user details
     * @param jwtBlacklistService Service for managing blacklisted tokens
     */
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService, JwtBlacklistService jwtBlacklistService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    /**
     * Processes each request to validate JWT token and set up authentication.
     * Skips authentication for public endpoints and validates tokens for protected endpoints.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        logger.info("[JWT FILTER] Path: " + path);

        if (isPublicEndpoint(path)) {
            logger.info("[JWT FILTER] Public endpoint, skipping JWT auth");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getJwtFromRequest(request);
            logger.info("[JWT FILTER] Extracted JWT: " + (jwt != null ? jwt.substring(0, Math.min(10, jwt.length())) + "..." : "null"));

            if (org.springframework.util.StringUtils.hasText(jwt)
                    && !jwtBlacklistService.isTokenBlacklisted(jwt)
                    && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                logger.info("[JWT FILTER] Valid JWT for username: " + username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("[JWT FILTER] Authentication set in context for: " + username);
            } else {
                logger.warn("[JWT FILTER] No valid JWT found or token is blacklisted");
            }

        } catch (Exception ex) {
            logger.error("[JWT FILTER] Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts JWT token from the request.
     * Checks both Authorization header and SESSION cookie.
     *
     * @param request The HTTP request
     * @return The JWT token if found, null otherwise
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        // Check for JWT in SESSION cookie if Authorization header is missing
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("SESSION".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Checks if the given path matches any public endpoint pattern.
     *
     * @param path The request path to check
     * @return true if the path matches a public endpoint, false otherwise
     */
    private boolean isPublicEndpoint(String path) {
        for (String pattern : PUBLIC_ENDPOINTS) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
 