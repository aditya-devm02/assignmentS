package com.finance.manager.dto.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TransactionUpdateRequest {
    private BigDecimal amount;
    private String category;
    private String description;
    // Intentionally omit date for update
} 