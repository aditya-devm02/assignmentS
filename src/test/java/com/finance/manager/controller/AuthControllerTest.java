package com.finance.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.finance.manager.dto.auth.LoginRequest;
import com.finance.manager.dto.auth.RegisterRequest;
import com.finance.manager.entity.User;
import com.finance.manager.security.JwtBlacklistService;
import com.finance.manager.security.JwtTokenProvider;
import com.finance.manager.service.CategoryService;
import com.finance.manager.service.UserService;
import com.finance.manager.exception.BadRequestException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(com.finance.manager.config.TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtBlacklistService jwtBlacklistService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFullName("New User");
        registerRequest.setPhoneNumber("+1234567890");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void register_Success() throws Exception {
        when(userService.registerUser(any(RegisterRequest.class))).thenReturn(testUser);
        doNothing().when(categoryService).createDefaultCategories(any(User.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.userId").value(1L));

        verify(userService, times(1)).registerUser(any(RegisterRequest.class));
        verify(categoryService, times(1)).createDefaultCategories(any(User.class));
    }

    @Test
    void register_UsernameAlreadyExists_ReturnsBadRequest() throws Exception {
        when(userService.registerUser(any(RegisterRequest.class)))
                .thenThrow(new BadRequestException("Username already exists"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).registerUser(any(RegisterRequest.class));
        verify(categoryService, never()).createDefaultCategories(any(User.class));
    }

    @Test
    void login_Success() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(loginRequest.getUsername());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("mock_jwt_token");
        when(jwtTokenProvider.getJwtExpiration()).thenReturn(3600000L); // 1 hour
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.token").value("mock_jwt_token"))
                .andExpect(cookie().exists("SESSION"))
                .andExpect(cookie().httpOnly("SESSION", true))
                .andExpect(cookie().path("SESSION", "/"))
                .andExpect(cookie().maxAge("SESSION", 3600));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(any(Authentication.class));
        verify(userService, times(1)).getUserByUsername(anyString());
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, never()).generateToken(any(Authentication.class));
        verify(userService, never()).getUserByUsername(anyString());
    }

    @Test
    void logout_Success() throws Exception {
        doNothing().when(jwtBlacklistService).blacklistToken(anyString());

        mockMvc.perform(post("/auth/logout")
                        .cookie(new Cookie("SESSION", "some_valid_jwt_token"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"))
                .andExpect(jsonPath("$.userId").isEmpty())
                .andExpect(cookie().maxAge("SESSION", 0));

        verify(jwtBlacklistService, times(1)).blacklistToken("some_valid_jwt_token");
    }

    @Test
    void logout_NoCookie() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));

        verify(jwtBlacklistService, never()).blacklistToken(anyString());
    }

    @Test
    void register_InvalidRequest_MissingFields() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest();
        // Missing all required fields
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_InvalidRequest_MissingFields() throws Exception {
        LoginRequest invalidLogin = new LoginRequest();
        // Missing username and password
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLogin))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_MissingCsrfToken_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void login_MissingCsrfToken_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void register_InternalServerError() throws Exception {
        when(userService.registerUser(any(RegisterRequest.class))).thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void logout_AlreadyBlacklistedToken() throws Exception {
        doThrow(new RuntimeException("Token already blacklisted")).when(jwtBlacklistService).blacklistToken(anyString());
        mockMvc.perform(post("/auth/logout")
                .cookie(new Cookie("SESSION", "some_valid_jwt_token"))
                .with(csrf()))
                .andExpect(status().isInternalServerError());
        verify(jwtBlacklistService, times(1)).blacklistToken("some_valid_jwt_token");
    }
} 