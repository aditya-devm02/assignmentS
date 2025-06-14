package com.finance.manager.service;

import com.finance.manager.dto.goal.GoalUpdateRequest;
import com.finance.manager.dto.goal.SavingsGoalRequest;
import com.finance.manager.dto.goal.SavingsGoalResponse;
import com.finance.manager.entity.Category;
import com.finance.manager.entity.SavingsGoal;
import com.finance.manager.entity.Transaction;
import com.finance.manager.entity.User;
import com.finance.manager.exception.BadRequestException;
import com.finance.manager.exception.ResourceNotFoundException;
import com.finance.manager.repository.SavingsGoalRepository;
import com.finance.manager.repository.TransactionRepository;
import com.finance.manager.service.impl.SavingsGoalServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavingsGoalServiceTest {

    @Mock
    private SavingsGoalRepository savingsGoalRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private SavingsGoalServiceImpl savingsGoalService;

    private User testUser;
    private SavingsGoalRequest savingsGoalRequest;
    private SavingsGoal savingsGoal;
    private Category incomeCategory;
    private Category expenseCategory;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        savingsGoalRequest = new SavingsGoalRequest();
        savingsGoalRequest.setGoalName("Vacation Fund");
        savingsGoalRequest.setTargetAmount(new BigDecimal("1000.00"));
        savingsGoalRequest.setTargetDate(LocalDate.now().plusMonths(6));
        savingsGoalRequest.setStartDate(LocalDate.now());

        savingsGoal = new SavingsGoal();
        savingsGoal.setId(1L);
        savingsGoal.setGoalName("Vacation Fund");
        savingsGoal.setTargetAmount(new BigDecimal("1000.00"));
        savingsGoal.setTargetDate(LocalDate.now().plusMonths(6));
        savingsGoal.setStartDate(LocalDate.now());
        savingsGoal.setUser(testUser);

        incomeCategory = new Category();
        incomeCategory.setId(1L);
        incomeCategory.setName("Salary");
        incomeCategory.setType(Category.TransactionType.INCOME);

        expenseCategory = new Category();
        expenseCategory.setId(2L);
        expenseCategory.setName("Rent");
        expenseCategory.setType(Category.TransactionType.EXPENSE);
    }

    @Test
    void createGoal_Success() {
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(savingsGoal);
        when(transactionRepository.findByUserOrderByDateDesc(any(User.class))).thenReturn(Collections.emptyList());

        SavingsGoalResponse response = savingsGoalService.createGoal(testUser, savingsGoalRequest);

        assertNotNull(response);
        assertEquals("Vacation Fund", response.getGoalName());
        verify(savingsGoalRepository, times(1)).save(any(SavingsGoal.class));
    }

    @Test
    void createGoal_TargetDateInPast_ThrowsBadRequestException() {
        savingsGoalRequest.setTargetDate(LocalDate.now().minusDays(1));

        assertThrows(BadRequestException.class, () -> savingsGoalService.createGoal(testUser, savingsGoalRequest));
        verify(savingsGoalRepository, never()).save(any(SavingsGoal.class));
    }

    @Test
    void createGoal_StartDateAfterTargetDate_ThrowsBadRequestException() {
        savingsGoalRequest.setTargetDate(LocalDate.now().plusMonths(1));
        savingsGoalRequest.setStartDate(LocalDate.now().plusMonths(2));

        assertThrows(BadRequestException.class, () -> savingsGoalService.createGoal(testUser, savingsGoalRequest));
        verify(savingsGoalRepository, never()).save(any(SavingsGoal.class));
    }

    @Test
    void createGoal_StartDateIsNull_DefaultsToNow() {
        savingsGoalRequest.setStartDate(null);
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(savingsGoal);
        when(transactionRepository.findByUserOrderByDateDesc(any(User.class))).thenReturn(Collections.emptyList());

        SavingsGoalResponse response = savingsGoalService.createGoal(testUser, savingsGoalRequest);

        assertNotNull(response);
        assertEquals(LocalDate.now(), response.getStartDate());
        verify(savingsGoalRepository, times(1)).save(any(SavingsGoal.class));
    }

    @Test
    void getAllGoals_ReturnsListOfGoals() {
        when(savingsGoalRepository.findByUser(any(User.class))).thenReturn(Arrays.asList(savingsGoal));
        when(transactionRepository.findByUserOrderByDateDesc(any(User.class))).thenReturn(Collections.emptyList());

        List<SavingsGoalResponse> goals = savingsGoalService.getAllGoals(testUser);

        assertNotNull(goals);
        assertFalse(goals.isEmpty());
        assertEquals(1, goals.size());
        assertEquals("Vacation Fund", goals.get(0).getGoalName());
        verify(savingsGoalRepository, times(1)).findByUser(any(User.class));
    }

    @Test
    void getAllGoals_ReturnsEmptyList_WhenNoGoals() {
        when(savingsGoalRepository.findByUser(any(User.class))).thenReturn(Collections.emptyList());

        List<SavingsGoalResponse> goals = savingsGoalService.getAllGoals(testUser);

        assertNotNull(goals);
        assertTrue(goals.isEmpty());
        verify(savingsGoalRepository, times(1)).findByUser(any(User.class));
    }

    @Test
    void getGoal_Success() {
        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));
        when(transactionRepository.findByUserOrderByDateDesc(any(User.class))).thenReturn(Collections.emptyList());

        SavingsGoalResponse response = savingsGoalService.getGoal(testUser, 1L);

        assertNotNull(response);
        assertEquals("Vacation Fund", response.getGoalName());
        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
    }

    @Test
    void getGoal_NotFound_ThrowsResourceNotFoundException() {
        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> savingsGoalService.getGoal(testUser, 1L));
        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
    }

    @Test
    void updateGoal_Success_AllFields() {
        GoalUpdateRequest updateRequest = new GoalUpdateRequest();
        updateRequest.setGoalName("Updated Vacation Fund");
        updateRequest.setTargetAmount(new BigDecimal("1200.00"));
        updateRequest.setTargetDate(LocalDate.now().plusMonths(12));
        updateRequest.setStartDate(LocalDate.now().plusDays(5));

        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(savingsGoal);
        when(transactionRepository.findByUserOrderByDateDesc(any(User.class))).thenReturn(Collections.emptyList());

        SavingsGoalResponse response = savingsGoalService.updateGoal(testUser, 1L, updateRequest);

        assertNotNull(response);
        assertEquals("Updated Vacation Fund", response.getGoalName());
        assertEquals(new BigDecimal("1200.00"), response.getTargetAmount());
        assertEquals(LocalDate.now().plusMonths(12), response.getTargetDate());
        assertEquals(LocalDate.now().plusDays(5), response.getStartDate());

        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(savingsGoalRepository, times(1)).save(any(SavingsGoal.class));
    }

    @Test
    void updateGoal_Success_PartialFields() {
        GoalUpdateRequest updateRequest = new GoalUpdateRequest();
        updateRequest.setGoalName("Updated Partial Goal");

        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));
        when(savingsGoalRepository.save(any(SavingsGoal.class))).thenReturn(savingsGoal);
        when(transactionRepository.findByUserOrderByDateDesc(any(User.class))).thenReturn(Collections.emptyList());

        SavingsGoalResponse response = savingsGoalService.updateGoal(testUser, 1L, updateRequest);

        assertNotNull(response);
        assertEquals("Updated Partial Goal", response.getGoalName());
        // Assert that other fields remain unchanged
        assertEquals(new BigDecimal("1000.00"), response.getTargetAmount());
        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(savingsGoalRepository, times(1)).save(any(SavingsGoal.class));
    }

    @Test
    void updateGoal_NotFound_ThrowsResourceNotFoundException() {
        GoalUpdateRequest updateRequest = new GoalUpdateRequest();
        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> savingsGoalService.updateGoal(testUser, 1L, updateRequest));
        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(savingsGoalRepository, never()).save(any(SavingsGoal.class));
    }

    @Test
    void updateGoal_TargetDateInPast_ThrowsBadRequestException() {
        GoalUpdateRequest updateRequest = new GoalUpdateRequest();
        updateRequest.setTargetDate(LocalDate.now().minusDays(1));

        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));

        assertThrows(BadRequestException.class, () -> savingsGoalService.updateGoal(testUser, 1L, updateRequest));
        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(savingsGoalRepository, never()).save(any(SavingsGoal.class));
    }

    @Test
    void updateGoal_StartDateAfterTargetDate_ThrowsBadRequestException() {
        GoalUpdateRequest updateRequest = new GoalUpdateRequest();
        updateRequest.setStartDate(LocalDate.now().plusMonths(2));
        updateRequest.setTargetDate(LocalDate.now().plusMonths(1));

        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));

        assertThrows(BadRequestException.class, () -> savingsGoalService.updateGoal(testUser, 1L, updateRequest));
        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(savingsGoalRepository, never()).save(any(SavingsGoal.class));
    }

    @Test
    void deleteGoal_Success() {
        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));
        doNothing().when(savingsGoalRepository).delete(any(SavingsGoal.class));

        savingsGoalService.deleteGoal(testUser, 1L);

        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(savingsGoalRepository, times(1)).delete(any(SavingsGoal.class));
    }

    @Test
    void deleteGoal_NotFound_ThrowsResourceNotFoundException() {
        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> savingsGoalService.deleteGoal(testUser, 1L));
        verify(savingsGoalRepository, times(1)).findByIdAndUser(anyLong(), any(User.class));
        verify(savingsGoalRepository, never()).delete(any(SavingsGoal.class));
    }

    @Test
    void toResponse_CalculatesProgressCorrectly() {
        Transaction incomeTransaction = new Transaction();
        incomeTransaction.setAmount(new BigDecimal("500.00"));
        incomeTransaction.setDate(LocalDate.now().minusDays(10));
        incomeTransaction.setCategory(incomeCategory);

        Transaction expenseTransaction = new Transaction();
        expenseTransaction.setAmount(new BigDecimal("100.00"));
        expenseTransaction.setDate(LocalDate.now().minusDays(5));
        expenseTransaction.setCategory(expenseCategory);

        when(transactionRepository.findByUserOrderByDateDesc(any(User.class)))
                .thenReturn(Arrays.asList(incomeTransaction, expenseTransaction));
        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));

        savingsGoal.setStartDate(LocalDate.now().minusDays(15));
        savingsGoal.setTargetAmount(new BigDecimal("1000.00"));

        SavingsGoalResponse response = savingsGoalService.getGoal(testUser, 1L); // Calling through getGoal to use toResponse

        assertEquals(new BigDecimal("400.00"), response.getCurrentProgress());
        assertEquals(40.00, response.getProgressPercentage(), 0.001);
        assertEquals(new BigDecimal("600.00"), response.getRemainingAmount());
    }

    @Test
    void toResponse_ZeroProgressIfNoTransactions() {
        when(transactionRepository.findByUserOrderByDateDesc(any(User.class))).thenReturn(Collections.emptyList());
        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));

        savingsGoal.setStartDate(LocalDate.now().minusDays(15));
        savingsGoal.setTargetAmount(new BigDecimal("1000.00"));

        SavingsGoalResponse response = savingsGoalService.getGoal(testUser, 1L); // Calling through getGoal to use toResponse

        assertEquals(BigDecimal.ZERO, response.getCurrentProgress());
        assertEquals(0.00, response.getProgressPercentage(), 0.001);
        assertEquals(new BigDecimal("1000.00"), response.getRemainingAmount());
    }

    @Test
    void toResponse_ProgressWithTransactionsBeforeStartDateIgnored() {
        Transaction oldIncomeTransaction = new Transaction();
        oldIncomeTransaction.setAmount(new BigDecimal("500.00"));
        oldIncomeTransaction.setDate(LocalDate.now().minusMonths(2));
        oldIncomeTransaction.setCategory(incomeCategory);

        Transaction recentIncomeTransaction = new Transaction();
        recentIncomeTransaction.setAmount(new BigDecimal("200.00"));
        recentIncomeTransaction.setDate(LocalDate.now().minusDays(5));
        recentIncomeTransaction.setCategory(incomeCategory);

        when(transactionRepository.findByUserOrderByDateDesc(any(User.class)))
                .thenReturn(Arrays.asList(recentIncomeTransaction, oldIncomeTransaction));
        when(savingsGoalRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(savingsGoal));

        savingsGoal.setStartDate(LocalDate.now().minusMonths(1));
        savingsGoal.setTargetAmount(new BigDecimal("500.00"));

        SavingsGoalResponse response = savingsGoalService.getGoal(testUser, 1L); // Calling through getGoal to use toResponse

        assertEquals(new BigDecimal("200.00"), response.getCurrentProgress());
        assertEquals(40.00, response.getProgressPercentage(), 0.001);
        assertEquals(new BigDecimal("300.00"), response.getRemainingAmount());
    }
} 