package com.finance.manager.service;

import com.finance.manager.dto.category.CategoryRequest;
import com.finance.manager.dto.category.CategoryResponse;
import com.finance.manager.entity.User;

import java.util.List;

/**
 * Service interface for category-related operations.
 * Handles creation, retrieval, and deletion of transaction categories.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CategoryService {
    /**
     * Retrieves all categories for a specific user.
     *
     * @param user The user whose categories to retrieve
     * @return List of category responses
     */
    List<CategoryResponse> getAllCategories(User user);

    /**
     * Creates a new custom category for a user.
     *
     * @param user The user creating the category
     * @param request Category creation request containing category details
     * @return The created category response
     */
    CategoryResponse createCustomCategory(User user, CategoryRequest request);

    /**
     * Deletes a custom category for a user.
     *
     * @param user The user deleting the category
     * @param name Name of the category to delete
     */
    void deleteCustomCategory(User user, String name);

    /**
     * Creates default categories for a new user.
     *
     * @param user The user to create default categories for
     */
    void createDefaultCategories(User user);
} 