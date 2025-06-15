package com.finance.manager.dto.transaction;

import com.finance.manager.entity.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response DTO for transaction information.
 * Used to return detailed transaction data to clients.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class TransactionResponse {
    /** Unique identifier of the transaction */
    private Long id;

    /** Amount of the transaction */
    private BigDecimal amount;

    /** Date when the transaction occurred */
    private LocalDate date;

    /** Name of the category this transaction belongs to */
    private String category;

    /** Optional description of the transaction */
    private String description;

    /** Type of the transaction (INCOME or EXPENSE) */
    private Category.TransactionType type;
} 