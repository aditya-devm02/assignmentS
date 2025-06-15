package com.finance.manager.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for savings goal information.
 * Used to transfer savings goal data between layers of the application.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class SavingsGoalDTO {
    /** Unique identifier of the savings goal */
    private Long id;

    /** Name of the savings goal */
    private String name;

    /** Target amount to be saved */
    private BigDecimal targetAmount;

    /** Current amount saved towards the goal */
    private BigDecimal currentAmount;

    /** Target date by which the goal should be achieved */
    private LocalDate targetDate;
} 