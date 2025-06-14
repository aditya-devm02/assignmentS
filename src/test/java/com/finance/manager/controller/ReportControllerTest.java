package com.finance.manager.controller;

import com.finance.manager.dto.report.MonthlyReportResponse;
import com.finance.manager.dto.report.YearlyReportResponse;
import com.finance.manager.entity.User;
import com.finance.manager.service.ReportService;
import com.finance.manager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@Import(com.finance.manager.config.TestSecurityConfig.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private UserService userService;

    private User testUser;
    private MonthlyReportResponse monthlyReport;
    private YearlyReportResponse yearlyReport;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        // Setup monthly report
        monthlyReport = new MonthlyReportResponse();
        monthlyReport.setMonth(6);
        monthlyReport.setYear(2024);
        Map<String, BigDecimal> income = new HashMap<>();
        income.put("Salary", new BigDecimal("5000.00"));
        monthlyReport.setTotalIncome(income);
        Map<String, BigDecimal> expenses = new HashMap<>();
        expenses.put("Rent", new BigDecimal("2000.00"));
        monthlyReport.setTotalExpenses(expenses);
        monthlyReport.setNetSavings(new BigDecimal("3000.00"));

        // Setup yearly report
        yearlyReport = new YearlyReportResponse();
        yearlyReport.setYear(2024);
        Map<String, BigDecimal> yearlyIncome = new HashMap<>();
        yearlyIncome.put("Salary", new BigDecimal("60000.00"));
        yearlyReport.setTotalIncome(yearlyIncome);
        Map<String, BigDecimal> yearlyExpenses = new HashMap<>();
        yearlyExpenses.put("Rent", new BigDecimal("24000.00"));
        yearlyReport.setTotalExpenses(yearlyExpenses);
        yearlyReport.setNetSavings(new BigDecimal("36000.00"));
    }

    @Test
    void getMonthlyReport_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(reportService.getMonthlyReport(any(User.class), anyInt(), anyInt()))
                .thenReturn(monthlyReport);

        mockMvc.perform(get("/reports/monthly/2024/6")
                        .with(user("test@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.month").value(6))
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.totalIncome.Salary").value(5000.00))
                .andExpect(jsonPath("$.totalExpenses.Rent").value(2000.00))
                .andExpect(jsonPath("$.netSavings").value(3000.00));

        verify(reportService).getMonthlyReport(any(User.class), anyInt(), anyInt());
    }

    @Test
    void getMonthlyReport_InvalidMonth() throws Exception {
        mockMvc.perform(get("/reports/monthly/2024/13")
                        .with(user("test@example.com")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMonthlyReport_Unauthenticated() throws Exception {
        mockMvc.perform(get("/reports/monthly/2024/6"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getYearlyReport_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(reportService.getYearlyReport(any(User.class), anyInt()))
                .thenReturn(yearlyReport);

        mockMvc.perform(get("/reports/yearly/2024")
                        .with(user("test@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.totalIncome.Salary").value(60000.00))
                .andExpect(jsonPath("$.totalExpenses.Rent").value(24000.00))
                .andExpect(jsonPath("$.netSavings").value(36000.00));

        verify(reportService).getYearlyReport(any(User.class), anyInt());
    }

    @Test
    void getYearlyReport_Unauthenticated() throws Exception {
        mockMvc.perform(get("/reports/yearly/2024"))
                .andExpect(status().isForbidden());
    }
} 