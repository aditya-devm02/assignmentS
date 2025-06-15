package com.finance.manager.repository;

import com.finance.manager.entity.SavingsGoal;
import com.finance.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for SavingsGoal entity.
 * Provides data access methods for savings goal-related operations.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    /**
     * Finds all savings goals for a user.
     *
     * @param user The user whose savings goals to find
     * @return List of savings goals for the user
     */
    List<SavingsGoal> findByUser(User user);

    /**
     * Finds a specific savings goal by its ID and associated user.
     *
     * @param id The ID of the savings goal
     * @param user The user who owns the savings goal
     * @return Optional containing the savings goal if found, empty otherwise
     */
    Optional<SavingsGoal> findByIdAndUser(Long id, User user);
} 