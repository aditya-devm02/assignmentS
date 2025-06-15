package com.finance.manager.service.impl;

import com.finance.manager.dto.report.MonthlyReportResponse;
import com.finance.manager.dto.report.YearlyReportResponse;
import com.finance.manager.entity.Category;
import com.finance.manager.entity.Transaction;
import com.finance.manager.entity.User;
import com.finance.manager.repository.TransactionRepository;
import com.finance.manager.service.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the ReportService interface.
 * Handles generation of financial reports including monthly and yearly summaries
 * with income, expense, and savings calculations.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class ReportServiceImpl implements ReportService {
    private final TransactionRepository transactionRepository;

    /**
     * Constructs a ReportServiceImpl with required dependencies.
     *
     * @param transactionRepository Repository for transaction data access
     */
    public ReportServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Generates a monthly financial report for a user.
     * Calculates total income, expenses, and net savings for the specified month.
     *
     * @param user The user to generate the report for
     * @param year The year of the report
     * @param month The month of the report (1-12)
     * @return MonthlyReportResponse containing the report data
     */
    @Override
    public MonthlyReportResponse getMonthlyReport(User user, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        List<Transaction> transactions = transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, start, end);
        Map<String, BigDecimal> totalIncome = new HashMap<>();
        Map<String, BigDecimal> totalExpenses = new HashMap<>();
        BigDecimal netSavings = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            String cat = t.getCategory().getName();
            if (t.getCategory().getType() == Category.TransactionType.INCOME) {
                totalIncome.put(cat, totalIncome.getOrDefault(cat, BigDecimal.ZERO).add(t.getAmount()));
                netSavings = netSavings.add(t.getAmount());
            } else {
                totalExpenses.put(cat, totalExpenses.getOrDefault(cat, BigDecimal.ZERO).add(t.getAmount()));
                netSavings = netSavings.subtract(t.getAmount());
            }
        }
        MonthlyReportResponse response = new MonthlyReportResponse();
        response.setMonth(month);
        response.setYear(year);
        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpenses);
        response.setNetSavings(netSavings);
        return response;
    }

    /**
     * Generates a yearly financial report for a user.
     * Calculates total income, expenses, and net savings for the specified year.
     *
     * @param user The user to generate the report for
     * @param year The year of the report
     * @return YearlyReportResponse containing the report data
     */
    @Override
    public YearlyReportResponse getYearlyReport(User user, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<Transaction> transactions = transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, start, end);
        Map<String, BigDecimal> totalIncome = new HashMap<>();
        Map<String, BigDecimal> totalExpenses = new HashMap<>();
        BigDecimal netSavings = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            String cat = t.getCategory().getName();
            if (t.getCategory().getType() == Category.TransactionType.INCOME) {
                totalIncome.put(cat, totalIncome.getOrDefault(cat, BigDecimal.ZERO).add(t.getAmount()));
                netSavings = netSavings.add(t.getAmount());
            } else {
                totalExpenses.put(cat, totalExpenses.getOrDefault(cat, BigDecimal.ZERO).add(t.getAmount()));
                netSavings = netSavings.subtract(t.getAmount());
            }
        }
        YearlyReportResponse response = new YearlyReportResponse();
        response.setYear(year);
        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpenses);
        response.setNetSavings(netSavings);
        return response;
    }
} 