package com.finance.manager.dto.category;

import com.finance.manager.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for creating a new category.
 * Contains all necessary information for creating a category with validation constraints.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class CategoryRequest {
    /** Name of the category. Must not be blank. */
    @NotBlank
    private String name;

    /** Type of transactions this category will be used for. Must not be null. */
    @NotNull
    private Category.TransactionType type;
} 