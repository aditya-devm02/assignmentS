package com.finance.manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a transaction category in the Personal Finance Manager system.
 * Categories are used to classify transactions as either income or expenses.
 * Each category belongs to a specific user and can be either system-defined or custom.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "user_id"})
})
public class Category {
    /**
     * Unique identifier for the category.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the category.
     * Must not be blank.
     */
    @NotBlank
    @Column(unique = false)
    private String name;

    /**
     * Type of transactions this category is used for.
     * Can be either INCOME or EXPENSE.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    /**
     * Flag indicating whether this is a custom category created by the user.
     * Default is false for system-defined categories.
     */
    private boolean isCustom = false;

    /**
     * The user who owns this category.
     * Many categories can belong to one user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Collection of transactions associated with this category.
     * One category can have many transactions.
     */
    @OneToMany(mappedBy = "category")
    private Set<Transaction> transactions = new HashSet<>();

    /**
     * Enum representing the type of transactions a category can be used for.
     */
    public enum TransactionType {
        /** Represents incoming money */
        INCOME,
        /** Represents outgoing money */
        EXPENSE
    }
} 