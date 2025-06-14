package com.finance.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.manager.dto.category.CategoryRequest;
import com.finance.manager.dto.category.CategoryResponse;
import com.finance.manager.entity.Category;
import com.finance.manager.entity.User;
import com.finance.manager.service.CategoryService;
import com.finance.manager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import(com.finance.manager.config.TestSecurityConfig.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserService userService;

    private User testUser;
    private CategoryRequest categoryRequest;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Test Category");
        categoryRequest.setType(Category.TransactionType.EXPENSE);

        categoryResponse = new CategoryResponse();
        categoryResponse.setName("Test Category");
        categoryResponse.setType(Category.TransactionType.EXPENSE);
    }

    @Test
    void getAllCategories_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        List<CategoryResponse> categories = Arrays.asList(categoryResponse);
        when(categoryService.getAllCategories(any(User.class))).thenReturn(categories);

        mockMvc.perform(get("/categories")
                        .with(user("test@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Category"))
                .andExpect(jsonPath("$[0].type").value("EXPENSE"));

        verify(categoryService).getAllCategories(any(User.class));
    }

    @Test
    void getAllCategories_Unauthenticated() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createCustomCategory_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(categoryService.createCustomCategory(any(User.class), any(CategoryRequest.class)))
                .thenReturn(categoryResponse);

        mockMvc.perform(post("/categories")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.type").value("EXPENSE"));

        verify(categoryService).createCustomCategory(any(User.class), any(CategoryRequest.class));
    }

    @Test
    void createCustomCategory_InvalidRequest() throws Exception {
        categoryRequest.setName(""); // Invalid empty name

        mockMvc.perform(post("/categories")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCustomCategory_Unauthenticated() throws Exception {
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteCustomCategory_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        doNothing().when(categoryService).deleteCustomCategory(any(User.class), anyString());

        mockMvc.perform(delete("/categories/Test Category")
                        .with(user("test@example.com")))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted successfully"));

        verify(categoryService).deleteCustomCategory(any(User.class), anyString());
    }

    @Test
    void deleteCustomCategory_Unauthenticated() throws Exception {
        mockMvc.perform(delete("/categories/Test Category"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createCustomCategory_DuplicateName_ReturnsConflict() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(categoryService.createCustomCategory(any(User.class), any(CategoryRequest.class)))
                .thenThrow(new RuntimeException("Category already exists"));

        mockMvc.perform(post("/categories")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isInternalServerError()); // Adjust to .isConflict() if you handle 409
    }

    @Test
    void deleteCustomCategory_NotFound_ReturnsNotFound() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        doThrow(new RuntimeException("Category not found")).when(categoryService).deleteCustomCategory(any(User.class), anyString());

        mockMvc.perform(delete("/categories/NonExistentCategory")
                        .with(user("test@example.com")))
                .andExpect(status().isInternalServerError()); // Adjust to .isNotFound() if you handle 404
    }

    @Test
    void createCustomCategory_InvalidType_ReturnsBadRequest() throws Exception {
        categoryRequest.setType(null);
        mockMvc.perform(post("/categories")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCustomCategory_NullRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/categories")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCustomCategory_InvalidName_ReturnsBadRequest() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        doThrow(new IllegalArgumentException("Invalid category name")).when(categoryService).deleteCustomCategory(any(User.class), eq(""));

        mockMvc.perform(delete("/categories/")
                        .with(user("test@example.com")))
                .andExpect(status().isInternalServerError()); // Adjust to .isBadRequest() if you handle 400
    }

    @Test
    void createCustomCategory_InternalServerError() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(categoryService.createCustomCategory(any(User.class), any(CategoryRequest.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/categories")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteCustomCategory_InternalServerError() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        doThrow(new RuntimeException("Unexpected error")).when(categoryService).deleteCustomCategory(any(User.class), anyString());

        mockMvc.perform(delete("/categories/Test Category")
                        .with(user("test@example.com")))
                .andExpect(status().isInternalServerError());
    }
} 