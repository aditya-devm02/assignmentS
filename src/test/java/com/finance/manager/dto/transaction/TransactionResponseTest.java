package com.finance.manager.dto.transaction;

import com.finance.manager.entity.Category;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionResponseTest {

    @Test
    void testGettersAndSetters() {
        TransactionResponse response = new TransactionResponse();
        response.setId(1L);
        response.setAmount(new BigDecimal("100.00"));
        response.setDate(LocalDate.now());
        response.setCategory("Test Category");
        response.setDescription("Test transaction");
        response.setType(Category.TransactionType.EXPENSE);

        assertEquals(1L, response.getId());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertEquals(LocalDate.now(), response.getDate());
        assertEquals("Test Category", response.getCategory());
        assertEquals("Test transaction", response.getDescription());
        assertEquals(Category.TransactionType.EXPENSE, response.getType());
    }

    @Test
    void testTypeHandling() {
        TransactionResponse response = new TransactionResponse();
        response.setType(Category.TransactionType.INCOME);
        assertEquals(Category.TransactionType.INCOME, response.getType());

        response.setType(Category.TransactionType.EXPENSE);
        assertEquals(Category.TransactionType.EXPENSE, response.getType());
    }

    @Test
    void testNullHandling() {
        TransactionResponse response = new TransactionResponse();
        response.setId(1L);

        assertNull(response.getAmount());
        assertNull(response.getDate());
        assertNull(response.getCategory());
        assertNull(response.getDescription());
        assertNull(response.getType());
    }
} 