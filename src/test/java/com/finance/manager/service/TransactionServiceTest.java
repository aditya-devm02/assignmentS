package com.finance.manager.service;

import com.finance.manager.dto.transaction.TransactionRequest;
import com.finance.manager.dto.transaction.TransactionResponse;
import com.finance.manager.dto.transaction.TransactionUpdateRequest;
import com.finance.manager.entity.Category;
import com.finance.manager.entity.Transaction;
import com.finance.manager.entity.User;
import com.finance.manager.exception.BadRequestException;
import com.finance.manager.exception.ResourceNotFoundException;
import com.finance.manager.repository.CategoryRepository;
import com.finance.manager.repository.TransactionRepository;
import com.finance.manager.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User testUser;
    private Category testCategory;
    private Transaction testTransaction;
    private TransactionRequest transactionRequest;
    private TransactionUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setType(Category.TransactionType.EXPENSE);
        testCategory.setUser(testUser);

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setDate(LocalDate.now());
        testTransaction.setDescription("Test transaction");
        testTransaction.setCategory(testCategory);
        testTransaction.setUser(testUser);

        transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(new BigDecimal("100.00"));
        transactionRequest.setDate(LocalDate.now());
        transactionRequest.setDescription("Test transaction");
        transactionRequest.setCategory("Test Category");

        updateRequest = new TransactionUpdateRequest();
        updateRequest.setAmount(new BigDecimal("200.00"));
        updateRequest.setDescription("Updated transaction");
        updateRequest.setCategory("Test Category");
    }

    @Test
    void createTransaction_Success() {
        // Arrange
        when(categoryRepository.findByNameAndUser("Test Category", testUser)).thenReturn(Optional.of(testCategory));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // Act
        TransactionResponse response = transactionService.createTransaction(testUser, transactionRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testTransaction.getId(), response.getId());
        assertEquals(testTransaction.getAmount(), response.getAmount());
        assertEquals(testTransaction.getCategory().getName(), response.getCategory());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findByNameAndUser("Nonexistent Category", testUser)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionRequest.setCategory("Nonexistent Category");
            transactionService.createTransaction(testUser, transactionRequest);
        });
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void getTransactions_Success() {
        // Arrange
        List<Transaction> transactions = Arrays.asList(testTransaction);
        when(transactionRepository.findByUserOrderByDateDesc(testUser)).thenReturn(transactions);

        // Act
        List<TransactionResponse> responses = transactionService.getTransactions(testUser, null, null, null, null);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(testTransaction.getId(), responses.get(0).getId());
        assertEquals(testTransaction.getAmount(), responses.get(0).getAmount());
        assertEquals(testTransaction.getCategory().getName(), responses.get(0).getCategory());
    }

    @Test
    void updateTransaction_Success() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));
        when(categoryRepository.findByNameAndUser("Test Category", testUser)).thenReturn(Optional.of(testCategory));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // Act
        TransactionResponse response = transactionService.updateTransaction(testUser, 1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(updateRequest.getAmount(), response.getAmount());
        assertEquals(updateRequest.getDescription(), response.getDescription());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_NotFound() {
        // Arrange
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> transactionService.updateTransaction(testUser, 999L, updateRequest));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void updateTransaction_Unauthorized() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        testTransaction.setUser(otherUser);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> transactionService.updateTransaction(testUser, 1L, updateRequest));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_Success() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));
        doNothing().when(transactionRepository).delete(testTransaction);

        // Act & Assert
        assertDoesNotThrow(() -> transactionService.deleteTransaction(testUser, 1L));
        verify(transactionRepository, times(1)).delete(testTransaction);
    }

    @Test
    void deleteTransaction_NotFound() {
        // Arrange
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> transactionService.deleteTransaction(testUser, 999L));
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }
} 