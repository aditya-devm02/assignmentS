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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;

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

    @PostMapping("/register")
public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
    User user = userService.registerUser(request);
    categoryService.createDefaultCategories(user);
    return new ResponseEntity<>(
            new AuthResponse("User registered successfully", user.getId()),
            HttpStatus.CREATED
    );
}

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
