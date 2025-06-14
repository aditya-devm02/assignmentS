package com.finance.manager.repository;

import com.finance.manager.entity.Transaction;
import com.finance.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByDateDesc(User user);
    List<Transaction> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);
    List<Transaction> findByUserAndCategory_NameOrderByDateDesc(User user, String categoryName);
    List<Transaction> findByUserAndDateBetweenAndCategory_NameOrderByDateDesc(
        User user, LocalDate startDate, LocalDate endDate, String categoryName);
    List<Transaction> findByUserAndCategory_IdOrderByDateDesc(User user, Long categoryId);
    List<Transaction> findByUserAndDateBetweenAndCategory_IdOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate, Long categoryId);
} 