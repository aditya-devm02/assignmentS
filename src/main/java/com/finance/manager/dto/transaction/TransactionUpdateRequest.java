package com.finance.manager.dto.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Request DTO for updating a transaction.
 * Fields are optional and only included in the JSON if they are not null.
 * Date field is intentionally omitted as it should not be updated.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TransactionUpdateRequest {
    /** New amount for the transaction */
    private BigDecimal amount;

    /** New category for the transaction */
    private String category;

    /** New description for the transaction */
    private String description;
    // Intentionally omit date for update
} 