package com.finance.manager.dto.goal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SavingsGoalResponseTest {

    @Test
    void testGettersAndSetters() {
        SavingsGoalResponse response = new SavingsGoalResponse();
        response.setId(1L);
        response.setGoalName("Vacation Fund");
        response.setTargetAmount(new BigDecimal("1000.00"));
        response.setTargetDate(LocalDate.now().plusMonths(6));
        response.setStartDate(LocalDate.now());
        response.setCurrentProgress(new BigDecimal("500.00"));
        response.setProgressPercentage(new BigDecimal("0.5"));
        response.setRemainingAmount(new BigDecimal("500.00"));

        assertEquals(1L, response.getId());
        assertEquals("Vacation Fund", response.getGoalName());
        assertEquals(new BigDecimal("1000.00"), response.getTargetAmount());
        assertTrue(response.getTargetDate().isAfter(LocalDate.now()));
        assertEquals(LocalDate.now(), response.getStartDate());
        assertEquals(new BigDecimal("500.00"), response.getCurrentProgress());
        assertEquals(0.5, response.getProgressPercentage(), 0.001);
        assertEquals(new BigDecimal("500.00"), response.getRemainingAmount());
    }

    @Test
    void testProgressPercentageCalculation() {
        SavingsGoalResponse response = new SavingsGoalResponse();
        response.setCurrentProgress(new BigDecimal("100.00"));
        response.setTargetAmount(new BigDecimal("300.00"));
        response.setProgressPercentage(new BigDecimal("0.33"));
        assertEquals(0.33, response.getProgressPercentage(), 0.001);
    }

    @Test
    void testProgressPercentageRounding() {
        SavingsGoalResponse response = new SavingsGoalResponse();
        response.setCurrentProgress(new BigDecimal("200.00"));
        response.setTargetAmount(new BigDecimal("300.00"));
        response.setProgressPercentage(new BigDecimal("0.67"));
        assertEquals(0.67, response.getProgressPercentage(), 0.001);
    }
} 