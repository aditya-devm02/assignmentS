package com.finance.manager.service;

import com.finance.manager.dto.report.MonthlyReportResponse;
import com.finance.manager.dto.report.YearlyReportResponse;
import com.finance.manager.entity.Category;
import com.finance.manager.entity.Transaction;
import com.finance.manager.entity.User;
import com.finance.manager.repository.TransactionRepository;
import com.finance.manager.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private User testUser;
    private Category incomeCategory;
    private Category expenseCategory;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        incomeCategory = new Category();
        incomeCategory.setId(1L);
        incomeCategory.setName("Salary");
        incomeCategory.setType(Category.TransactionType.INCOME);

        expenseCategory = new Category();
        expenseCategory.setId(2L);
        expenseCategory.setName("Rent");
        expenseCategory.setType(Category.TransactionType.EXPENSE);
    }

    private Transaction createTransaction(BigDecimal amount, LocalDate date, Category category) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDate(date);
        transaction.setCategory(category);
        return transaction;
    }

    @Test
    void getMonthlyReport_WithIncomeAndExpenses() {
        LocalDate date1 = LocalDate.of(2023, 10, 15);
        LocalDate date2 = LocalDate.of(2023, 10, 20);
        LocalDate date3 = LocalDate.of(2023, 10, 25);

        List<Transaction> transactions = Arrays.asList(
                createTransaction(new BigDecimal("2000.00"), date1, incomeCategory),
                createTransaction(new BigDecimal("500.00"), date2, expenseCategory),
                createTransaction(new BigDecimal("300.00"), date3, incomeCategory)
        );

        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(transactions);

        MonthlyReportResponse response = reportService.getMonthlyReport(testUser, 2023, 10);

        assertNotNull(response);
        assertEquals(2023, response.getYear());
        assertEquals(10, response.getMonth());
        assertEquals(new BigDecimal("2300.00"), response.getTotalIncome().get("Salary"));
        assertEquals(new BigDecimal("500.00"), response.getTotalExpenses().get("Rent"));
        assertEquals(new BigDecimal("1800.00"), response.getNetSavings());
    }

    @Test
    void getMonthlyReport_OnlyIncome() {
        LocalDate date1 = LocalDate.of(2023, 11, 10);
        List<Transaction> transactions = Arrays.asList(
                createTransaction(new BigDecimal("1500.00"), date1, incomeCategory)
        );

        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(transactions);

        MonthlyReportResponse response = reportService.getMonthlyReport(testUser, 2023, 11);

        assertNotNull(response);
        assertEquals(2023, response.getYear());
        assertEquals(11, response.getMonth());
        assertEquals(new BigDecimal("1500.00"), response.getTotalIncome().get("Salary"));
        assertEquals(0, response.getTotalExpenses().size());
        assertEquals(new BigDecimal("1500.00"), response.getNetSavings());
    }

    @Test
    void getMonthlyReport_OnlyExpenses() {
        LocalDate date1 = LocalDate.of(2023, 12, 5);
        List<Transaction> transactions = Arrays.asList(
                createTransaction(new BigDecimal("200.00"), date1, expenseCategory)
        );

        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(transactions);

        MonthlyReportResponse response = reportService.getMonthlyReport(testUser, 2023, 12);

        assertNotNull(response);
        assertEquals(2023, response.getYear());
        assertEquals(12, response.getMonth());
        assertEquals(0, response.getTotalIncome().size());
        assertEquals(new BigDecimal("200.00"), response.getTotalExpenses().get("Rent"));
        assertEquals(new BigDecimal("-200.00"), response.getNetSavings());
    }

    @Test
    void getMonthlyReport_NoTransactions() {
        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        MonthlyReportResponse response = reportService.getMonthlyReport(testUser, 2024, 1);

        assertNotNull(response);
        assertEquals(2024, response.getYear());
        assertEquals(1, response.getMonth());
        assertEquals(0, response.getTotalIncome().size());
        assertEquals(0, response.getTotalExpenses().size());
        assertEquals(BigDecimal.ZERO, response.getNetSavings());
    }

    @Test
    void getMonthlyReport_TransactionsOutsideMonthIgnored() {
        LocalDate dateInMonth = LocalDate.of(2023, 10, 15);
        LocalDate dateBeforeMonth = LocalDate.of(2023, 9, 20);
        LocalDate dateAfterMonth = LocalDate.of(2023, 11, 5);

        List<Transaction> allTransactions = Arrays.asList(
                createTransaction(new BigDecimal("1000.00"), dateInMonth, incomeCategory),
                createTransaction(new BigDecimal("200.00"), dateBeforeMonth, expenseCategory),
                createTransaction(new BigDecimal("150.00"), dateAfterMonth, incomeCategory)
        );

        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(createTransaction(new BigDecimal("1000.00"), dateInMonth, incomeCategory)));

        MonthlyReportResponse response = reportService.getMonthlyReport(testUser, 2023, 10);

        assertNotNull(response);
        assertEquals(2023, response.getYear());
        assertEquals(10, response.getMonth());
        assertEquals(new BigDecimal("1000.00"), response.getTotalIncome().get("Salary"));
        assertEquals(0, response.getTotalExpenses().size());
        assertEquals(new BigDecimal("1000.00"), response.getNetSavings());
    }

    @Test
    void getYearlyReport_WithIncomeAndExpenses() {
        LocalDate date1 = LocalDate.of(2023, 1, 15);
        LocalDate date2 = LocalDate.of(2023, 6, 20);
        LocalDate date3 = LocalDate.of(2023, 11, 25);

        List<Transaction> transactions = Arrays.asList(
                createTransaction(new BigDecimal("5000.00"), date1, incomeCategory),
                createTransaction(new BigDecimal("1500.00"), date2, expenseCategory),
                createTransaction(new BigDecimal("1000.00"), date3, incomeCategory)
        );

        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(transactions);

        YearlyReportResponse response = reportService.getYearlyReport(testUser, 2023);

        assertNotNull(response);
        assertEquals(2023, response.getYear());
        assertEquals(new BigDecimal("6000.00"), response.getTotalIncome().get("Salary"));
        assertEquals(new BigDecimal("1500.00"), response.getTotalExpenses().get("Rent"));
        assertEquals(new BigDecimal("4500.00"), response.getNetSavings());
    }

    @Test
    void getYearlyReport_OnlyIncome() {
        LocalDate date1 = LocalDate.of(2023, 3, 10);
        List<Transaction> transactions = Arrays.asList(
                createTransaction(new BigDecimal("3000.00"), date1, incomeCategory)
        );

        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(transactions);

        YearlyReportResponse response = reportService.getYearlyReport(testUser, 2023);

        assertNotNull(response);
        assertEquals(2023, response.getYear());
        assertEquals(new BigDecimal("3000.00"), response.getTotalIncome().get("Salary"));
        assertEquals(0, response.getTotalExpenses().size());
        assertEquals(new BigDecimal("3000.00"), response.getNetSavings());
    }

    @Test
    void getYearlyReport_OnlyExpenses() {
        LocalDate date1 = LocalDate.of(2023, 8, 5);
        List<Transaction> transactions = Arrays.asList(
                createTransaction(new BigDecimal("700.00"), date1, expenseCategory)
        );

        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(transactions);

        YearlyReportResponse response = reportService.getYearlyReport(testUser, 2023);

        assertNotNull(response);
        assertEquals(2023, response.getYear());
        assertEquals(0, response.getTotalIncome().size());
        assertEquals(new BigDecimal("700.00"), response.getTotalExpenses().get("Rent"));
        assertEquals(new BigDecimal("-700.00"), response.getNetSavings());
    }

    @Test
    void getYearlyReport_NoTransactions() {
        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        YearlyReportResponse response = reportService.getYearlyReport(testUser, 2024);

        assertNotNull(response);
        assertEquals(2024, response.getYear());
        assertEquals(0, response.getTotalIncome().size());
        assertEquals(0, response.getTotalExpenses().size());
        assertEquals(BigDecimal.ZERO, response.getNetSavings());
    }

    @Test
    void getYearlyReport_TransactionsOutsideYearIgnored() {
        LocalDate dateInYear = LocalDate.of(2023, 7, 1);
        LocalDate dateBeforeYear = LocalDate.of(2022, 12, 31);
        LocalDate dateAfterYear = LocalDate.of(2024, 1, 1);

        List<Transaction> allTransactions = Arrays.asList(
                createTransaction(new BigDecimal("2000.00"), dateInYear, incomeCategory),
                createTransaction(new BigDecimal("500.00"), dateBeforeYear, expenseCategory),
                createTransaction(new BigDecimal("300.00"), dateAfterYear, incomeCategory)
        );

        when(transactionRepository.findByUserAndDateBetweenOrderByDateDesc(eq(testUser), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(createTransaction(new BigDecimal("2000.00"), dateInYear, incomeCategory)));

        YearlyReportResponse response = reportService.getYearlyReport(testUser, 2023);

        assertNotNull(response);
        assertEquals(2023, response.getYear());
        assertEquals(new BigDecimal("2000.00"), response.getTotalIncome().get("Salary"));
        assertEquals(0, response.getTotalExpenses().size());
        assertEquals(new BigDecimal("2000.00"), response.getNetSavings());
    }
} 