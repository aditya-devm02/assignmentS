package com.finance.manager.dto;

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
    /** Unique identifier of the category */
    private Long id;

    /** Name of the category */
    private String name;
} 