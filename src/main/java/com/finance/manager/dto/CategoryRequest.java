package com.finance.manager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for category creation or update.
 * Used to receive category data from clients.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class CategoryRequest {
    /** Name of the category. Must not be blank. */
    @NotBlank(message = "Category name is required")
    private String name;
} 