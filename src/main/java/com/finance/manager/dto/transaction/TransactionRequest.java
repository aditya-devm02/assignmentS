package com.finance.manager.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating a new transaction.
 * Contains all necessary information for creating a transaction with validation constraints.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class TransactionRequest {
    /** Amount of the transaction. Must be greater than or equal to 0.01. */
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    /** Date when the transaction occurred. Must be in the past or present. */
    @NotNull
    @PastOrPresent
    private LocalDate date;

    /** Name of the category this transaction belongs to. Must not be null. */
    @NotNull
    private String category;

    /** Optional description of the transaction */
    private String description;
} 