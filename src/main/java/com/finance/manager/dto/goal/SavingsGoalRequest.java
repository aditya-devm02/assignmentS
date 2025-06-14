package com.finance.manager.dto.goal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SavingsGoalRequest {
    @NotBlank
    private String goalName;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal targetAmount;

    @NotNull
    @Future
    private LocalDate targetDate;

    private LocalDate startDate;
} 