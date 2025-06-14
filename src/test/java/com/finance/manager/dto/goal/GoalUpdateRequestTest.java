package com.finance.manager.dto.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GoalUpdateRequestTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGettersAndSetters() {
        GoalUpdateRequest request = new GoalUpdateRequest();
        request.setGoalName("Updated Goal");
        request.setTargetAmount(new BigDecimal("2000.00"));
        request.setTargetDate(LocalDate.now().plusMonths(12));
        request.setStartDate(LocalDate.now());

        assertEquals("Updated Goal", request.getGoalName());
        assertEquals(new BigDecimal("2000.00"), request.getTargetAmount());
        assertTrue(request.getTargetDate().isAfter(LocalDate.now()));
        assertEquals(LocalDate.now(), request.getStartDate());
    }

    @Test
    void testNullFieldsAreExcluded() throws Exception {
        GoalUpdateRequest request = new GoalUpdateRequest();
        request.setGoalName("Partial Update");
        // Other fields are null

        String json = objectMapper.writeValueAsString(request);
        assertTrue(json.contains("goalName"));
        assertFalse(json.contains("targetAmount"));
        assertFalse(json.contains("targetDate"));
        assertFalse(json.contains("startDate"));
    }

    @Test
    void testPartialUpdate() {
        GoalUpdateRequest request = new GoalUpdateRequest();
        request.setGoalName("New Name");
        // Only setting name, other fields remain null

        assertEquals("New Name", request.getGoalName());
        assertNull(request.getTargetAmount());
        assertNull(request.getTargetDate());
        assertNull(request.getStartDate());
    }
} 