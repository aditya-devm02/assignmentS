package com.finance.manager.service;

import com.finance.manager.dto.report.MonthlyReportResponse;
import com.finance.manager.dto.report.YearlyReportResponse;
import com.finance.manager.entity.User;

/**
 * Service interface for financial report generation.
 * Handles creation of monthly and yearly financial reports.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ReportService {
    /**
     * Generates a monthly financial report for a user.
     *
     * @param user The user to generate the report for
     * @param year The year for the report
     * @param month The month for the report (1-12)
     * @return Monthly report response containing financial data
     */
    MonthlyReportResponse getMonthlyReport(User user, int year, int month);

    /**
     * Generates a yearly financial report for a user.
     *
     * @param user The user to generate the report for
     * @param year The year for the report
     * @return Yearly report response containing financial data
     */
    YearlyReportResponse getYearlyReport(User user, int year);
} 