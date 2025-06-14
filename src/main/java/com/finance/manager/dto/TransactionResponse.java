package com.finance.manager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String categoryName;
    private Long categoryId;
} 