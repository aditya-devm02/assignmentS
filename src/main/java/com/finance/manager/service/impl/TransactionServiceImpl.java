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

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

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
        // Convert to TransactionResponse (assuming a toResponse method exists)
        return transactions.stream().map(this::toResponse).toList();
    }

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
            // Find category by name for this user
            Category category = categoryRepository.findByNameAndUser(request.getCategory(), user)
                    .orElseThrow(() -> new BadRequestException("Category not found"));
            transaction.setCategory(category);
        }
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
        // Ignore date field if present
        transactionRepository.save(transaction);
        return toResponse(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(User user, Long id) {
        var transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        // Optionally, check if transaction.getUser().equals(user) for security
        transactionRepository.delete(transaction);
    }

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