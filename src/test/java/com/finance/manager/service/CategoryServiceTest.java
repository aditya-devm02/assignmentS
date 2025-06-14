package com.finance.manager.service;

import com.finance.manager.dto.category.CategoryRequest;
import com.finance.manager.dto.category.CategoryResponse;
import com.finance.manager.entity.Category;
import com.finance.manager.entity.User;
import com.finance.manager.exception.BadRequestException;
import com.finance.manager.exception.ResourceNotFoundException;
import com.finance.manager.repository.CategoryRepository;
import com.finance.manager.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private User testUser;
    private Category testCategory;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setType(Category.TransactionType.EXPENSE);
        testCategory.setCustom(true);
        testCategory.setUser(testUser);

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("New Category");
        categoryRequest.setType(Category.TransactionType.EXPENSE);
    }

    @Test
    void getAllCategories_Success() {
        // Arrange
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryRepository.findByUser(testUser)).thenReturn(categories);

        // Act
        List<CategoryResponse> responses = categoryService.getAllCategories(testUser);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(testCategory.getName(), responses.get(0).getName());
        assertEquals(testCategory.getType(), responses.get(0).getType());
        assertEquals(testCategory.isCustom(), responses.get(0).isCustom());
    }

    @Test
    void createCustomCategory_Success() {
        // Arrange
        when(categoryRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // Act
        CategoryResponse response = categoryService.createCustomCategory(testUser, categoryRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testCategory.getName(), response.getName());
        assertEquals(testCategory.getType(), response.getType());
        assertEquals(testCategory.isCustom(), response.isCustom());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCustomCategory_NameExists() {
        // Arrange
        when(categoryRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> categoryService.createCustomCategory(testUser, categoryRequest));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteCustomCategory_Success() {
        // Arrange
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.of(testCategory));
        doNothing().when(categoryRepository).delete(testCategory);

        // Act & Assert
        assertDoesNotThrow(() -> categoryService.deleteCustomCategory(testUser, "Test Category"));
        verify(categoryRepository, times(1)).delete(testCategory);
    }

    @Test
    void deleteCustomCategory_NotFound() {
        // Arrange
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCustomCategory(testUser, "Nonexistent Category"));
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void deleteCustomCategory_DefaultCategory() {
        // Arrange
        testCategory.setCustom(false);
        when(categoryRepository.findByNameAndUser(anyString(), any(User.class))).thenReturn(Optional.of(testCategory));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> categoryService.deleteCustomCategory(testUser, "Test Category"));
        verify(categoryRepository, never()).delete(any(Category.class));
    }
} 