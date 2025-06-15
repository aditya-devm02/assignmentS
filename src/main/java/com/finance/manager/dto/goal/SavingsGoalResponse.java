package com.finance.manager.dto.goal;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response DTO for savings goal information.
 * Used to return detailed savings goal data to clients, including progress tracking.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class SavingsGoalResponse {
    /** Unique identifier of the savings goal */
    private Long id;

    /** Name of the savings goal */
    private String goalName;

    /** Target amount to be saved */
    private BigDecimal targetAmount;

    /** Target date by which the goal should be achieved */
    private LocalDate targetDate;

    /** Date when the savings goal was created */
    private LocalDate startDate;

    /** Current amount saved towards the goal */
    private BigDecimal currentProgress;

    /** Percentage of progress towards the goal (0-100) */
    private BigDecimal progressPercentage;

    /** Remaining amount to be saved to reach the goal */
    private BigDecimal remainingAmount;

    /**
     * Gets the progress percentage as a rounded double value.
     * The percentage is rounded to 2 decimal places.
     *
     * @return The progress percentage as a double value between 0 and 100
     */
    public double getProgressPercentage() {
        return Math.round(progressPercentage.doubleValue() * 100.0) / 100.0;
    }
} 