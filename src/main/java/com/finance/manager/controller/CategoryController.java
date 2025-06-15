package com.finance.manager.controller;

import com.finance.manager.dto.category.CategoryRequest;
import com.finance.manager.dto.category.CategoryResponse;
import com.finance.manager.entity.User;
import com.finance.manager.service.CategoryService;
import com.finance.manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller handling category-related operations including retrieval,
 * creation, and deletion of transaction categories.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    /**
     * Constructs a CategoryController with required dependencies.
     *
     * @param categoryService Service for category-related operations
     * @param userService Service for user-related operations
     */
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    /**
     * Retrieves all categories for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @return ResponseEntity containing list of user's categories
     * @throws BadRequestException if user is not authenticated
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        List<CategoryResponse> categories = categoryService.getAllCategories(user);
        return ResponseEntity.ok(categories);
    }

    /**
     * Creates a new custom category for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param request Category creation request containing category details
     * @return ResponseEntity containing the created category details
     * @throws BadRequestException if user is not authenticated
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCustomCategory(@AuthenticationPrincipal UserDetails userDetails,
                                                                @Valid @RequestBody CategoryRequest request) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        CategoryResponse response = categoryService.createCustomCategory(user, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Deletes a custom category for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param name Name of the category to delete
     * @return ResponseEntity with success message
     * @throws BadRequestException if user is not authenticated
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCustomCategory(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable String name) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        categoryService.deleteCustomCategory(user, name);
        return ResponseEntity.ok().body("Category deleted successfully");
    }
} 