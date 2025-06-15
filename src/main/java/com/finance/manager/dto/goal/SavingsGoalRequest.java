package com.finance.manager.dto.goal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating a new savings goal.
 * Contains all necessary information for creating a savings goal with validation constraints.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class SavingsGoalRequest {
    /** Name of the savings goal. Must not be blank. */
    @NotBlank
    private String goalName;

    /** Target amount to be saved. Must be greater than or equal to 0.01. */
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal targetAmount;

    /** Target date by which the goal should be achieved. Must be a future date. */
    @NotNull
    @Future
    private LocalDate targetDate;

    /** Optional start date for the savings goal. If not provided, current date will be used. */
    private LocalDate startDate;
} 