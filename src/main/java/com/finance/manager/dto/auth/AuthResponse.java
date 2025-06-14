package com.finance.manager.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private Long userId;
    private String token;

    public AuthResponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }
} 