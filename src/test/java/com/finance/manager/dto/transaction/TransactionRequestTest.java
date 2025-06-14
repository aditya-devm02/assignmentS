package com.finance.manager.dto.transaction;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TransactionRequestTest {

    @Test
    void testTransactionRequestGettersAndSetters() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setDate(LocalDate.of(2023, 1, 15));
        request.setCategory("Groceries");
        request.setDescription("Weekly groceries");

        assertEquals(new BigDecimal("100.00"), request.getAmount());
        assertEquals(LocalDate.of(2023, 1, 15), request.getDate());
        assertEquals("Groceries", request.getCategory());
        assertEquals("Weekly groceries", request.getDescription());
    }
} 