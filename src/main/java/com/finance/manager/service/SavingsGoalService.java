package com.finance.manager.service;

import com.finance.manager.dto.goal.SavingsGoalRequest;
import com.finance.manager.dto.goal.SavingsGoalResponse;
import com.finance.manager.dto.goal.GoalUpdateRequest;
import com.finance.manager.entity.User;

import java.util.List;

/**
 * Service interface for savings goal operations.
 * Handles creation, retrieval, updating, and deletion of financial goals.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
public interface SavingsGoalService {
    /**
     * Creates a new savings goal for a user.
     *
     * @param user The user creating the goal
     * @param request Savings goal creation request containing goal details
     * @return The created savings goal response
     */
    SavingsGoalResponse createGoal(User user, SavingsGoalRequest request);

    /**
     * Retrieves all savings goals for a user.
     *
     * @param user The user whose goals to retrieve
     * @return List of savings goal responses
     */
    List<SavingsGoalResponse> getAllGoals(User user);

    /**
     * Retrieves a specific savings goal for a user.
     *
     * @param user The user whose goal to retrieve
     * @param id ID of the goal to retrieve
     * @return The requested savings goal response
     */
    SavingsGoalResponse getGoal(User user, Long id);

    /**
     * Updates an existing savings goal for a user.
     *
     * @param user The user updating the goal
     * @param id ID of the goal to update
     * @param request Goal update request containing new details
     * @return The updated savings goal response
     */
    SavingsGoalResponse updateGoal(User user, Long id, GoalUpdateRequest request);

    /**
     * Deletes a savings goal for a user.
     *
     * @param user The user deleting the goal
     * @param id ID of the goal to delete
     */
    void deleteGoal(User user, Long id);
} 