package com.finance.manager.service;

import com.finance.manager.dto.goal.SavingsGoalRequest;
import com.finance.manager.dto.goal.SavingsGoalResponse;
import com.finance.manager.dto.goal.GoalUpdateRequest;
import com.finance.manager.entity.User;

import java.util.List;

public interface SavingsGoalService {
    SavingsGoalResponse createGoal(User user, SavingsGoalRequest request);
    List<SavingsGoalResponse> getAllGoals(User user);
    SavingsGoalResponse getGoal(User user, Long id);
    SavingsGoalResponse updateGoal(User user, Long id, GoalUpdateRequest request);
    void deleteGoal(User user, Long id);
} 