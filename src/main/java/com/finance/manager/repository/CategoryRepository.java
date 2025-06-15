package com.finance.manager.repository;

import com.finance.manager.entity.Category;
import com.finance.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity.
 * Provides data access methods for category-related operations.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds all categories for a user.
     *
     * @param user The user whose categories to find
     * @return List of categories for the user
     */
    List<Category> findByUser(User user);

    /**
     * Finds a specific category by its name and associated user.
     *
     * @param name The name of the category
     * @param user The user who owns the category
     * @return Optional containing the category if found, empty otherwise
     */
    Optional<Category> findByNameAndUser(String name, User user);

    /**
     * Checks if a category exists with the given name for a user.
     *
     * @param name The name of the category to check
     * @param user The user to check for
     * @return true if a category exists with the given name for the user, false otherwise
     */
    boolean existsByNameAndUser(String name, User user);

    /**
     * Finds all categories of a specific type for a user.
     *
     * @param user The user whose categories to find
     * @param type The type of categories to find
     * @return List of categories of the specified type for the user
     */
    List<Category> findByUserAndType(User user, Category.TransactionType type);
} 