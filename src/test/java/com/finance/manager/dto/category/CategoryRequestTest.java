package com.finance.manager.dto.category;

import com.finance.manager.entity.Category;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRequestTest {

    private Validator validator;
    private CategoryRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        request = new CategoryRequest();
    }

    @Test
    void testGettersAndSetters() {
        request.setName("Test Category");
        request.setType(Category.TransactionType.EXPENSE);

        assertEquals("Test Category", request.getName());
        assertEquals(Category.TransactionType.EXPENSE, request.getType());
    }

    @Test
    void testValidation_ValidRequest() {
        request.setName("Valid Category");
        request.setType(Category.TransactionType.INCOME);

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidation_InvalidRequest() {
        request.setName("");
        request.setType(null);

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }

    @Test
    void testValidation_BlankName() {
        request.setName("   ");
        request.setType(Category.TransactionType.EXPENSE);

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
} 