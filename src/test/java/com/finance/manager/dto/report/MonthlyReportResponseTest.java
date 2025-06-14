package com.finance.manager.dto.report;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MonthlyReportResponseTest {

    @Test
    void testGettersAndSetters() {
        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setMonth(6);
        response.setYear(2024);
        
        Map<String, BigDecimal> income = new HashMap<>();
        income.put("Salary", new BigDecimal("5000.00"));
        income.put("Bonus", new BigDecimal("1000.00"));
        response.setTotalIncome(income);
        
        Map<String, BigDecimal> expenses = new HashMap<>();
        expenses.put("Rent", new BigDecimal("2000.00"));
        expenses.put("Utilities", new BigDecimal("500.00"));
        response.setTotalExpenses(expenses);
        
        response.setNetSavings(new BigDecimal("3500.00"));

        assertEquals(6, response.getMonth());
        assertEquals(2024, response.getYear());
        assertEquals(2, response.getTotalIncome().size());
        assertEquals(2, response.getTotalExpenses().size());
        assertEquals(new BigDecimal("5000.00"), response.getTotalIncome().get("Salary"));
        assertEquals(new BigDecimal("2000.00"), response.getTotalExpenses().get("Rent"));
        assertEquals(new BigDecimal("3500.00"), response.getNetSavings());
    }

    @Test
    void testEmptyMaps() {
        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setTotalIncome(new HashMap<>());
        response.setTotalExpenses(new HashMap<>());
        
        assertNotNull(response.getTotalIncome());
        assertNotNull(response.getTotalExpenses());
        assertTrue(response.getTotalIncome().isEmpty());
        assertTrue(response.getTotalExpenses().isEmpty());
    }

    @Test
    void testNullHandling() {
        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setMonth(6);
        response.setYear(2024);
        
        assertNull(response.getTotalIncome());
        assertNull(response.getTotalExpenses());
        assertNull(response.getNetSavings());
    }
} 