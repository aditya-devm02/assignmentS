package com.finance.manager.dto.report;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class YearlyReportResponse {
    private int year;
    private Map<String, BigDecimal> totalIncome;
    private Map<String, BigDecimal> totalExpenses;
    private BigDecimal netSavings;
} 