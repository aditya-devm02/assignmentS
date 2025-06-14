package com.finance.manager.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryRequestTest {

    @Test
    void testCategoryRequestGettersAndSetters() {
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("Test Category");

        assertEquals("Test Category", categoryRequest.getName());
    }
} 