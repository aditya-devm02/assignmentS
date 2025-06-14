package com.finance.manager.dto.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionUpdateRequestTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGettersAndSetters() {
        TransactionUpdateRequest request = new TransactionUpdateRequest();
        request.setAmount(new BigDecimal("200.00"));
        request.setCategory("Updated Category");
        request.setDescription("Updated description");

        assertEquals(new BigDecimal("200.00"), request.getAmount());
        assertEquals("Updated Category", request.getCategory());
        assertEquals("Updated description", request.getDescription());
    }

    @Test
    void testNullFieldsAreExcluded() throws Exception {
        TransactionUpdateRequest request = new TransactionUpdateRequest();
        request.setAmount(new BigDecimal("200.00"));
        // Other fields are null

        String json = objectMapper.writeValueAsString(request);
        assertTrue(json.contains("amount"));
        assertFalse(json.contains("category"));
        assertFalse(json.contains("description"));
    }

    @Test
    void testPartialUpdate() {
        TransactionUpdateRequest request = new TransactionUpdateRequest();
        request.setDescription("New description");
        // Only setting description, other fields remain null

        assertEquals("New description", request.getDescription());
        assertNull(request.getAmount());
        assertNull(request.getCategory());
    }
} 