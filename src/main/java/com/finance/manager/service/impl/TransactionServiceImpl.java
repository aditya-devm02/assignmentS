package com.finance.manager.service.impl;

import com.finance.manager.dto.transaction.TransactionRequest;
import com.finance.manager.dto.transaction.TransactionResponse;
import com.finance.manager.dto.transaction.TransactionUpdateRequest;
import com.finance.manager.entity.User;
import com.finance.manager.entity.Category;
import com.finance.manager.entity.Transaction;
import com.finance.manager.repository.TransactionRepository;
import com.finance.manager.repository.CategoryRepository;
import com.finance.manager.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.finance.manager.exception.ResourceNotFoundException;
import com.finance.manager.exception.BadRequestException;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the TransactionService interface.
 * Handles CRUD operations for financial transactions with category management
 * and date-based filtering capabilities.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Constructs a TransactionServiceImpl with required dependencies.
     *
     * @param transactionRepository Repository for transaction data access
     * @param categoryRepository Repository for category data access
     */
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new transaction for a user.
     * Validates the category exists and associates it with the transaction.
     *
     * @param user The user creating the transaction
     * @param request Transaction creation request containing transaction details
     * @return The created transaction response
     * @throws ResourceNotFoundException if category is not found
     */
    @Override
    @Transactional
    public TransactionResponse createTransaction(User user, TransactionRequest request) {
        var category = categoryRepository.findByNameAndUser(request.getCategory(), user)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        var transaction = new com.finance.manager.entity.Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(category);
        transaction.setUser(user);
        var saved = transactionRepository.save(transaction);
        return toResponse(saved);
    }

    /**
     * Retrieves transactions for a user with optional filtering by date range and category.
     *
     * @param user The user whose transactions to retrieve
     * @param startDate Optional start date for filtering
     * @param endDate Optional end date for filtering
     * @param categoryId Optional category ID for filtering
     * @param categoryName Optional category name for filtering
     * @return List of transaction responses matching the criteria
     */
    @Override
    public List<TransactionResponse> getTransactions(User user, LocalDate startDate, LocalDate endDate, Long categoryId, String categoryName) {
        List<com.finance.manager.entity.Transaction> transactions;
        if (categoryId != null) {
            if (startDate != null && endDate != null) {
                transactions = transactionRepository.findByUserAndDateBetweenAndCategory_IdOrderByDateDesc(user, startDate, endDate, categoryId);
            } else {
                transactions = transactionRepository.findByUserAndCategory_IdOrderByDateDesc(user, categoryId);
            }
        } else if (categoryName != null) {
            if (startDate != null && endDate != null) {
                transactions = transactionRepository.findByUserAndDateBetweenAndCategory_NameOrderByDateDesc(user, startDate, endDate, categoryName);
            } else {
                transactions = transactionRepository.findByUserAndCategory_NameOrderByDateDesc(user, categoryName);
            }
        } else if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
        } else {
            transactions = transactionRepository.findByUserOrderByDateDesc(user);
        }
        return transactions.stream().map(this::toResponse).toList();
    }

    /**
     * Updates an existing transaction.
     * Validates user ownership and updates specified fields.
     *
     * @param user The user updating the transaction
     * @param id ID of the transaction to update
     * @param request Update request containing new values
     * @return Updated transaction response
     * @throws ResourceNotFoundException if transaction is not found
     * @throws BadRequestException if user is not authorized or category not found
     */
    @Override
    @Transactional
    public TransactionResponse updateTransaction(User user, Long id, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized");
        }
        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }
        if (request.getCategory() != null) {
            Category category = categoryRepository.findByNameAndUser(request.getCategory(), user)
                    .orElseThrow(() -> new BadRequestException("Category not found"));
            transaction.setCategory(category);
        }
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
        transactionRepository.save(transaction);
        return toResponse(transaction);
    }

    /**
     * Deletes a transaction.
     * Validates transaction exists before deletion.
     *
     * @param user The user deleting the transaction
     * @param id ID of the transaction to delete
     * @throws ResourceNotFoundException if transaction is not found
     */
    @Override
    @Transactional
    public void deleteTransaction(User user, Long id) {
        var transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
    }

    /**
     * Converts a Transaction entity to a TransactionResponse DTO.
     *
     * @param transaction The transaction entity to convert
     * @return TransactionResponse containing the transaction details
     */
    private TransactionResponse toResponse(com.finance.manager.entity.Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setDate(transaction.getDate());
        response.setCategory(transaction.getCategory() != null ? transaction.getCategory().getName() : null);
        response.setDescription(transaction.getDescription());
        response.setType(transaction.getCategory() != null ? transaction.getCategory().getType() : null);
        return response;
    }
} 