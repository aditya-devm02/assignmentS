package com.finance.manager.service;

import com.finance.manager.dto.report.MonthlyReportResponse;
import com.finance.manager.dto.report.YearlyReportResponse;
import com.finance.manager.entity.User;

public interface ReportService {
    MonthlyReportResponse getMonthlyReport(User user, int year, int month);
    YearlyReportResponse getYearlyReport(User user, int year);
} 