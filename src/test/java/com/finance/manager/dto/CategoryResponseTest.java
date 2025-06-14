package com.finance.manager.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryResponseTest {

    @Test
    void testCategoryResponseGettersAndSetters() {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Test Category");

        assertEquals(1L, categoryResponse.getId());
        assertEquals("Test Category", categoryResponse.getName());
    }
} 