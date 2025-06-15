package com.finance.manager.dto.goal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for updating a savings goal.
 * Fields are optional and only included in the JSON if they are not null.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GoalUpdateRequest {
    /** New name for the savings goal */
    private String goalName;

    /** New target amount for the savings goal */
    private BigDecimal targetAmount;

    /** New target date for the savings goal */
    private LocalDate targetDate;

    /** New start date for the savings goal */
    private LocalDate startDate;
} 