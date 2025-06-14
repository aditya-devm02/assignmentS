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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "savings_goals")
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String goalName;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal targetAmount;

    @NotNull
    @Future
    private LocalDate targetDate;

    @NotNull
    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private BigDecimal currentProgress;

    @Transient
    private BigDecimal progressPercentage;

    @Transient
    private BigDecimal remainingAmount;
} 