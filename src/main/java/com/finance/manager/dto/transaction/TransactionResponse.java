package com.finance.manager.dto.transaction;

import com.finance.manager.entity.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String category;
    private String description;
    private Category.TransactionType type;
} 