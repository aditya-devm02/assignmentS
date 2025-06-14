package com.finance.manager.service;

import com.finance.manager.dto.auth.RegisterRequest;
import com.finance.manager.entity.User;

public interface UserService {
    User registerUser(RegisterRequest request);
    User getUserByUsername(String username);
} 