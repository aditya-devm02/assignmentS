package com.finance.manager.dto.category;

import com.finance.manager.entity.Category;
import lombok.Data;

@Data
public class CategoryResponse {
    private String name;
    private Category.TransactionType type;
    private boolean isCustom;

    public void setCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }
} 