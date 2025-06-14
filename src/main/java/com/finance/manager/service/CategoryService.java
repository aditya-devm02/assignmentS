package com.finance.manager.service;

import com.finance.manager.dto.category.CategoryRequest;
import com.finance.manager.dto.category.CategoryResponse;
import com.finance.manager.entity.User;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories(User user);
    CategoryResponse createCustomCategory(User user, CategoryRequest request);
    void deleteCustomCategory(User user, String name);
    void createDefaultCategories(User user);
} 