package com.finance.manager.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull
    @PastOrPresent
    private LocalDate date;

    @NotNull
    private String category;

    private String description;
} 