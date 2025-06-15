package com.finance.manager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for transaction creation or update.
 * Used to receive transaction data from clients.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class TransactionRequest {
    /** Amount of the transaction. Must be greater than 0.01. */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    /** Date when the transaction occurred. Must not be null. */
    @NotNull(message = "Date is required")
    private LocalDate date;

    /** ID of the category this transaction belongs to. Must not be null. */
    @NotNull(message = "Category ID is required")
    private Long categoryId;
} 