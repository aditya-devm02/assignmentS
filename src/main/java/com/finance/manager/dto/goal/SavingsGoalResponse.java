package com.finance.manager.dto.goal;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SavingsGoalResponse {
    private Long id;
    private String goalName;
    private BigDecimal targetAmount;
    private LocalDate targetDate;
    private LocalDate startDate;
    private BigDecimal currentProgress;
    private BigDecimal progressPercentage;
    private BigDecimal remainingAmount;

    public double getProgressPercentage() {
        return Math.round(progressPercentage.doubleValue() * 100.0) / 100.0;
    }
} 