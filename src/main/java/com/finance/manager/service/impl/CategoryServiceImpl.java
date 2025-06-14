package com.finance.manager.service.impl;

import com.finance.manager.dto.category.CategoryRequest;
import com.finance.manager.dto.category.CategoryResponse;
import com.finance.manager.entity.Category;
import com.finance.manager.entity.User;
import com.finance.manager.exception.BadRequestException;
import com.finance.manager.exception.ResourceNotFoundException;
import com.finance.manager.repository.CategoryRepository;
import com.finance.manager.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> getAllCategories(User user) {
        List<Category> categories = categoryRepository.findByUser(user);
        return categories.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponse createCustomCategory(User user, CategoryRequest request) {
        if (categoryRepository.existsByNameAndUser(request.getName(), user)) {
            throw new BadRequestException("Category name already exists");
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        category.setCustom(true);
        category.setUser(user);
        return toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCustomCategory(User user, String name) {
        Category category = categoryRepository.findByNameAndUser(name, user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (!category.isCustom()) {
            throw new BadRequestException("Cannot delete default category");
        }
        if (!category.getTransactions().isEmpty()) {
            throw new BadRequestException("Category is referenced by transactions");
        }
        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public void createDefaultCategories(User user) {
        System.out.println("[DEBUG] Creating default categories for user: " + user.getUsername());
        List<String> incomeCategories = List.of("Salary");
        List<String> expenseCategories = Arrays.asList("Food", "Rent", "Transportation", "Entertainment", "Healthcare", "Utilities");
        List<Category> categories = new ArrayList<>();
        for (String name : incomeCategories) {
            if (!categoryRepository.existsByNameAndUser(name, user)) {
                Category c = new Category();
                c.setName(name);
                c.setType(Category.TransactionType.INCOME);
                c.setCustom(false);
                c.setUser(user);
                categories.add(c);
            }
        }
        for (String name : expenseCategories) {
            if (!categoryRepository.existsByNameAndUser(name, user)) {
                Category c = new Category();
                c.setName(name);
                c.setType(Category.TransactionType.EXPENSE);
                c.setCustom(false);
                c.setUser(user);
                categories.add(c);
            }
        }
        categoryRepository.saveAll(categories);
        System.out.println("[DEBUG] Default categories created: " + categories.size());
    }

    private CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setName(category.getName());
        response.setType(category.getType());
        response.setCustom(category.isCustom());
        return response;
    }
} 