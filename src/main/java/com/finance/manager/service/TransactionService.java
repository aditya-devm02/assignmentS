package com.finance.manager.service;

import com.finance.manager.dto.transaction.TransactionRequest;
import com.finance.manager.dto.transaction.TransactionResponse;
import com.finance.manager.dto.transaction.TransactionUpdateRequest;
import com.finance.manager.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(User user, TransactionRequest request);
    List<TransactionResponse> getTransactions(User user, LocalDate startDate, LocalDate endDate, Long categoryId, String categoryName);
    TransactionResponse updateTransaction(User user, Long id, TransactionUpdateRequest request);
    void deleteTransaction(User user, Long id);
} 