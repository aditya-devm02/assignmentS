package com.finance.manager.dto.category;

import com.finance.manager.entity.Category;
import lombok.Data;

/**
 * Response DTO for category information.
 * Used to return category data to clients.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class CategoryResponse {
    /** Name of the category */
    private String name;

    /** Type of transactions this category is used for (INCOME or EXPENSE) */
    private Category.TransactionType type;

    /** Flag indicating whether this is a custom category created by the user */
    private boolean isCustom;

    /**
     * Sets whether this is a custom category.
     *
     * @param isCustom true if this is a custom category, false otherwise
     */
    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }
} 