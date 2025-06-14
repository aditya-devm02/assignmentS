package com.finance.manager.dto.category;

import com.finance.manager.entity.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryResponseTest {

    @Test
    void testGettersAndSetters() {
        CategoryResponse response = new CategoryResponse();
        response.setName("Test Category");
        response.setType(Category.TransactionType.EXPENSE);
        response.setCustom(true);

        assertEquals("Test Category", response.getName());
        assertEquals(Category.TransactionType.EXPENSE, response.getType());
        assertTrue(response.isCustom());
    }

    @Test
    void testCustomFlag() {
        CategoryResponse response = new CategoryResponse();
        response.setCustom(false);
        assertFalse(response.isCustom());

        response.setCustom(true);
        assertTrue(response.isCustom());
    }

    @Test
    void testTypeHandling() {
        CategoryResponse response = new CategoryResponse();
        response.setType(Category.TransactionType.INCOME);
        assertEquals(Category.TransactionType.INCOME, response.getType());

        response.setType(Category.TransactionType.EXPENSE);
        assertEquals(Category.TransactionType.EXPENSE, response.getType());
    }
} 