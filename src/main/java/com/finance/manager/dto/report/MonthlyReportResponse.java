package com.finance.manager.dto.report;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class MonthlyReportResponse {
    private int month;
    private int year;
    private Map<String, BigDecimal> totalIncome;
    private Map<String, BigDecimal> totalExpenses;
    private BigDecimal netSavings;
} 