package com.finance.manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity class representing a savings goal in the Personal Finance Manager system.
 * Savings goals help users track their progress towards specific financial targets
 * with defined target amounts and dates. The class includes calculated fields for
 * tracking progress and remaining amounts.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "savings_goals")
public class SavingsGoal {
    /**
     * Unique identifier for the savings goal.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the savings goal.
     * Must not be blank.
     */
    @NotBlank
    private String goalName;

    /**
     * Target amount to be saved.
     * Must be greater than or equal to 0.01.
     */
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal targetAmount;

    /**
     * Target date by which the goal should be achieved.
     * Must be a future date.
     */
    @NotNull
    @Future
    private LocalDate targetDate;

    /**
     * Date when the savings goal was created.
     * Must not be null.
     */
    @NotNull
    private LocalDate startDate;

    /**
     * User who owns this savings goal.
     * Many savings goals can belong to one user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Current progress towards the savings goal.
     * This is a transient field calculated at runtime.
     */
    @Transient
    private BigDecimal currentProgress;

    /**
     * Percentage of progress towards the savings goal.
     * This is a transient field calculated at runtime.
     */
    @Transient
    private BigDecimal progressPercentage;

    /**
     * Remaining amount to be saved to reach the goal.
     * This is a transient field calculated at runtime.
     */
    @Transient
    private BigDecimal remainingAmount;
} 