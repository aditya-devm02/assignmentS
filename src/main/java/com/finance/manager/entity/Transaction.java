package com.finance.manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity class representing a financial transaction in the Personal Finance Manager system.
 * Transactions can be either income or expenses and are associated with a specific category
 * and user. Each transaction has an amount, date, and optional description.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    /**
     * Unique identifier for the transaction.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Amount of the transaction.
     * Must be greater than or equal to 0.01.
     */
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    /**
     * Date when the transaction occurred.
     * Must be in the past or present.
     */
    @NotNull
    @PastOrPresent
    private LocalDate date;

    /**
     * Category this transaction belongs to.
     * Many transactions can belong to one category.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Optional description of the transaction.
     */
    private String description;

    /**
     * User who owns this transaction.
     * Many transactions can belong to one user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
} 