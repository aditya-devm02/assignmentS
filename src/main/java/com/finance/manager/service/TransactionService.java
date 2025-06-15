package com.finance.manager.service;

import com.finance.manager.dto.transaction.TransactionRequest;
import com.finance.manager.dto.transaction.TransactionResponse;
import com.finance.manager.dto.transaction.TransactionUpdateRequest;
import com.finance.manager.entity.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for transaction-related operations.
 * Handles creation, retrieval, updating, and deletion of financial transactions.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
public interface TransactionService {
    /**
     * Creates a new transaction for a user.
     *
     * @param user The user creating the transaction
     * @param request Transaction creation request containing transaction details
     * @return The created transaction response
     */
    TransactionResponse createTransaction(User user, TransactionRequest request);

    /**
     * Retrieves transactions for a user with optional filtering.
     *
     * @param user The user whose transactions to retrieve
     * @param startDate Optional start date for filtering
     * @param endDate Optional end date for filtering
     * @param categoryId Optional category ID for filtering
     * @param categoryName Optional category name for filtering
     * @return List of filtered transaction responses
     */
    List<TransactionResponse> getTransactions(User user, LocalDate startDate, LocalDate endDate, Long categoryId, String categoryName);

    /**
     * Updates an existing transaction for a user.
     *
     * @param user The user updating the transaction
     * @param id ID of the transaction to update
     * @param request Transaction update request containing new details
     * @return The updated transaction response
     */
    TransactionResponse updateTransaction(User user, Long id, TransactionUpdateRequest request);

    /**
     * Deletes a transaction for a user.
     *
     * @param user The user deleting the transaction
     * @param id ID of the transaction to delete
     */
    void deleteTransaction(User user, Long id);
} 