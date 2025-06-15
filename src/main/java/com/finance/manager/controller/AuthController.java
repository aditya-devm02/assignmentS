package com.finance.manager.controller;

import com.finance.manager.dto.auth.AuthResponse;
import com.finance.manager.dto.auth.LoginRequest;
import com.finance.manager.dto.auth.RegisterRequest;
import com.finance.manager.entity.User;
import com.finance.manager.security.JwtTokenProvider;
import com.finance.manager.service.CategoryService;
import com.finance.manager.service.UserService;
import com.finance.manager.security.JwtBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;

/**
 * Controller handling authentication-related operations including user registration,
 * login, and logout functionality. Manages JWT token generation and validation.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;

    /**
     * Constructs an AuthController with required dependencies.
     *
     * @param userService Service for user-related operations
     * @param categoryService Service for category-related operations
     * @param authenticationManager Spring Security's authentication manager
     * @param jwtTokenProvider Service for JWT token operations
     * @param jwtBlacklistService Service for managing blacklisted JWT tokens
     */
    public AuthController(UserService userService,
                          CategoryService categoryService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          JwtBlacklistService jwtBlacklistService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    /**
     * Registers a new user in the system.
     * Creates default categories for the new user.
     *
     * @param request Registration request containing user details
     * @return ResponseEntity containing registration status and user ID
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.registerUser(request);
        categoryService.createDefaultCategories(user);
        return new ResponseEntity<>(
                new AuthResponse("User registered successfully", user.getId()),
                HttpStatus.CREATED
        );
    }

    /**
     * Authenticates a user and generates a JWT token.
     * Sets the token in an HTTP-only cookie for security.
     *
     * @param request Login request containing credentials
     * @param response HTTP response for setting cookies
     * @return ResponseEntity containing login status, user ID, and JWT token
     * @throws BadCredentialsException if authentication fails
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        System.out.println("[DEBUG] Login attempt for username: " + request.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            System.out.println("[DEBUG] Authentication successful for: " + authentication.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);

            // Set token in HttpOnly cookie for compatibility with test script
            jakarta.servlet.http.Cookie jwtCookie = new jakarta.servlet.http.Cookie("SESSION", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge((int) (jwtTokenProvider.getJwtExpiration() / 1000));
            response.addCookie(jwtCookie);

            Long userId = userService.getUserByUsername(request.getUsername()).getId();
            return ResponseEntity.ok(new AuthResponse("Login successful", userId, jwt));
        } catch (BadCredentialsException ex) {
            System.out.println("[DEBUG] Authentication failed for username: " + request.getUsername() + ": " + ex.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    /**
     * Logs out a user by invalidating their JWT token.
     * Clears security context and removes the session cookie.
     *
     * @param request HTTP request containing the current session
     * @param response HTTP response for clearing cookies
     * @return ResponseEntity containing logout status
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        // Extract JWT from SESSION cookie
        String jwt = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("SESSION".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }
        if (jwt != null) {
            jwtBlacklistService.blacklistToken(jwt);
        }
        SecurityContextHolder.clearContext();
        response.setHeader("Set-Cookie", "SESSION=; HttpOnly; Path=/; Max-Age=0; SameSite=Strict");
        return ResponseEntity.ok(new AuthResponse("Logout successful", null));
    }
}
