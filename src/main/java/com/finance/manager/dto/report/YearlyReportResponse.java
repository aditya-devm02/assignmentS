package com.finance.manager.dto.report;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Response DTO for yearly financial report.
 * Contains aggregated financial data for a specific year.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class YearlyReportResponse {
    /** The year for which the report is generated */
    private int year;

    /** Map of category names to total income amounts for each category */
    private Map<String, BigDecimal> totalIncome;

    /** Map of category names to total expense amounts for each category */
    private Map<String, BigDecimal> totalExpenses;

    /** Net savings for the year (total income minus total expenses) */
    private BigDecimal netSavings;
} 