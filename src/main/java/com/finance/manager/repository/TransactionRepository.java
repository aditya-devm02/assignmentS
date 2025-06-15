package com.finance.manager.repository;

import com.finance.manager.entity.Transaction;
import com.finance.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Transaction entity.
 * Provides data access methods for transaction-related operations.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Finds all transactions for a user, ordered by date in descending order.
     *
     * @param user The user whose transactions to find
     * @return List of transactions ordered by date descending
     */
    List<Transaction> findByUserOrderByDateDesc(User user);

    /**
     * Finds all transactions for a user within a date range, ordered by date in descending order.
     *
     * @param user The user whose transactions to find
     * @param startDate The start date of the range (inclusive)
     * @param endDate The end date of the range (inclusive)
     * @return List of transactions within the date range, ordered by date descending
     */
    List<Transaction> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate);

    /**
     * Finds all transactions for a user in a specific category, ordered by date in descending order.
     *
     * @param user The user whose transactions to find
     * @param categoryName The name of the category to filter by
     * @return List of transactions in the specified category, ordered by date descending
     */
    List<Transaction> findByUserAndCategory_NameOrderByDateDesc(User user, String categoryName);

    /**
     * Finds all transactions for a user within a date range and in a specific category, ordered by date in descending order.
     *
     * @param user The user whose transactions to find
     * @param startDate The start date of the range (inclusive)
     * @param endDate The end date of the range (inclusive)
     * @param categoryName The name of the category to filter by
     * @return List of transactions within the date range and category, ordered by date descending
     */
    List<Transaction> findByUserAndDateBetweenAndCategory_NameOrderByDateDesc(
        User user, LocalDate startDate, LocalDate endDate, String categoryName);

    /**
     * Finds all transactions for a user in a specific category by ID, ordered by date in descending order.
     *
     * @param user The user whose transactions to find
     * @param categoryId The ID of the category to filter by
     * @return List of transactions in the specified category, ordered by date descending
     */
    List<Transaction> findByUserAndCategory_IdOrderByDateDesc(User user, Long categoryId);

    /**
     * Finds all transactions for a user within a date range and in a specific category by ID, ordered by date in descending order.
     *
     * @param user The user whose transactions to find
     * @param startDate The start date of the range (inclusive)
     * @param endDate The end date of the range (inclusive)
     * @param categoryId The ID of the category to filter by
     * @return List of transactions within the date range and category, ordered by date descending
     */
    List<Transaction> findByUserAndDateBetweenAndCategory_IdOrderByDateDesc(User user, LocalDate startDate, LocalDate endDate, Long categoryId);
} 