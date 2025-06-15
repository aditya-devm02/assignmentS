package com.finance.manager.service.impl;

import com.finance.manager.dto.goal.SavingsGoalRequest;
import com.finance.manager.dto.goal.SavingsGoalResponse;
import com.finance.manager.dto.goal.GoalUpdateRequest;
import com.finance.manager.entity.SavingsGoal;
import com.finance.manager.entity.Transaction;
import com.finance.manager.entity.User;
import com.finance.manager.exception.BadRequestException;
import com.finance.manager.exception.ResourceNotFoundException;
import com.finance.manager.repository.SavingsGoalRepository;
import com.finance.manager.repository.TransactionRepository;
import com.finance.manager.service.SavingsGoalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the SavingsGoalService interface.
 * Handles CRUD operations for savings goals with progress tracking
 * and transaction-based calculations.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class SavingsGoalServiceImpl implements SavingsGoalService {
    private final SavingsGoalRepository savingsGoalRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructs a SavingsGoalServiceImpl with required dependencies.
     *
     * @param savingsGoalRepository Repository for savings goal data access
     * @param transactionRepository Repository for transaction data access
     */
    public SavingsGoalServiceImpl(SavingsGoalRepository savingsGoalRepository, TransactionRepository transactionRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates a new savings goal for a user.
     * Validates target date and start date constraints.
     *
     * @param user The user creating the goal
     * @param request Goal creation request containing goal details
     * @return SavingsGoalResponse containing the created goal details
     * @throws BadRequestException if dates are invalid
     */
    @Override
    @Transactional
    public SavingsGoalResponse createGoal(User user, SavingsGoalRequest request) {
        if (request.getTargetDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Target date must be in the future");
        }
        if (request.getStartDate() != null && request.getStartDate().isAfter(request.getTargetDate())) {
            throw new BadRequestException("Start date cannot be after target date");
        }
        SavingsGoal goal = new SavingsGoal();
        goal.setGoalName(request.getGoalName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setStartDate(request.getStartDate() != null ? request.getStartDate() : LocalDate.now());
        goal.setUser(user);
        goal = savingsGoalRepository.save(goal);
        return toResponse(goal, user);
    }

    /**
     * Retrieves all savings goals for a user.
     *
     * @param user The user whose goals to retrieve
     * @return List of savings goal responses
     */
    @Override
    public List<SavingsGoalResponse> getAllGoals(User user) {
        return savingsGoalRepository.findByUser(user)
                .stream().map(goal -> toResponse(goal, user)).collect(Collectors.toList());
    }

    /**
     * Retrieves a specific savings goal for a user.
     *
     * @param user The user whose goal to retrieve
     * @param id ID of the goal to retrieve
     * @return SavingsGoalResponse containing the goal details
     * @throws ResourceNotFoundException if goal is not found
     */
    @Override
    public SavingsGoalResponse getGoal(User user, Long id) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        return toResponse(goal, user);
    }

    /**
     * Updates an existing savings goal.
     * Validates date constraints and updates specified fields.
     *
     * @param user The user updating the goal
     * @param id ID of the goal to update
     * @param request Update request containing new values
     * @return Updated savings goal response
     * @throws ResourceNotFoundException if goal is not found
     * @throws BadRequestException if dates are invalid
     */
    @Override
    @Transactional
    public SavingsGoalResponse updateGoal(User user, Long id, GoalUpdateRequest request) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        if (request.getTargetDate() != null && request.getTargetDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Target date must be in the future");
        }
        LocalDate newStartDate = request.getStartDate() != null ? request.getStartDate() : goal.getStartDate();
        LocalDate newTargetDate = request.getTargetDate() != null ? request.getTargetDate() : goal.getTargetDate();
        if (newStartDate != null && newTargetDate != null && newStartDate.isAfter(newTargetDate)) {
            throw new BadRequestException("Start date cannot be after target date");
        }
        if (request.getGoalName() != null) {
            goal.setGoalName(request.getGoalName());
        }
        if (request.getTargetAmount() != null) {
            goal.setTargetAmount(request.getTargetAmount());
        }
        if (request.getTargetDate() != null) {
            goal.setTargetDate(request.getTargetDate());
        }
        if (request.getStartDate() != null) {
            goal.setStartDate(request.getStartDate());
        }
        goal = savingsGoalRepository.save(goal);
        return toResponse(goal, user);
    }

    /**
     * Deletes a savings goal.
     * Validates goal exists and user ownership before deletion.
     *
     * @param user The user deleting the goal
     * @param id ID of the goal to delete
     * @throws ResourceNotFoundException if goal is not found
     */
    @Override
    @Transactional
    public void deleteGoal(User user, Long id) {
        SavingsGoal goal = savingsGoalRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        savingsGoalRepository.delete(goal);
    }

    /**
     * Converts a SavingsGoal entity to a SavingsGoalResponse DTO.
     * Calculates progress based on user's transactions.
     *
     * @param goal The savings goal entity to convert
     * @param user The user associated with the goal
     * @return SavingsGoalResponse containing the goal details and progress
     */
    private SavingsGoalResponse toResponse(SavingsGoal goal, User user) {
        SavingsGoalResponse response = new SavingsGoalResponse();
        response.setId(goal.getId());
        response.setGoalName(goal.getGoalName());
        response.setTargetAmount(goal.getTargetAmount());
        response.setTargetDate(goal.getTargetDate());
        response.setStartDate(goal.getStartDate());
        // Calculate progress
        LocalDate start = goal.getStartDate();
        List<Transaction> transactions = transactionRepository.findByUserOrderByDateDesc(user)
                .stream().filter(t -> !t.getDate().isBefore(start)).collect(Collectors.toList());
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getCategory().getType() == com.finance.manager.entity.Category.TransactionType.INCOME)
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getCategory().getType() == com.finance.manager.entity.Category.TransactionType.EXPENSE)
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal progress = totalIncome.subtract(totalExpense);
        response.setCurrentProgress(progress);
        BigDecimal percentage = progress.compareTo(BigDecimal.ZERO) > 0
            ? progress.divide(goal.getTargetAmount(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
            : BigDecimal.ZERO;
        response.setProgressPercentage(percentage);
        response.setRemainingAmount(goal.getTargetAmount().subtract(progress));
        return response;
    }
} 