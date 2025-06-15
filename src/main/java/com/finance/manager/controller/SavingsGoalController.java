package com.finance.manager.controller;

import com.finance.manager.dto.goal.SavingsGoalRequest;
import com.finance.manager.dto.goal.SavingsGoalResponse;
import com.finance.manager.dto.goal.GoalUpdateRequest;
import com.finance.manager.entity.User;
import com.finance.manager.service.SavingsGoalService;
import com.finance.manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing savings goals.
 * Handles CRUD operations for user savings goals with authentication.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/goals")
public class SavingsGoalController {
    private final SavingsGoalService savingsGoalService;
    private final UserService userService;

    /**
     * Constructs a new SavingsGoalController with required dependencies.
     *
     * @param savingsGoalService Service for savings goal operations
     * @param userService Service for user operations
     */
    public SavingsGoalController(SavingsGoalService savingsGoalService, UserService userService) {
        this.savingsGoalService = savingsGoalService;
        this.userService = userService;
    }

    /**
     * Creates a new savings goal for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param request The savings goal creation request
     * @return ResponseEntity containing the created savings goal
     * @throws BadRequestException if user is not authenticated
     */
    @PostMapping
    public ResponseEntity<SavingsGoalResponse> createGoal(@AuthenticationPrincipal UserDetails userDetails,
                                                          @Valid @RequestBody SavingsGoalRequest request) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        SavingsGoalResponse response = savingsGoalService.createGoal(user, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves all savings goals for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @return ResponseEntity containing a list of savings goals
     * @throws BadRequestException if user is not authenticated
     */
    @GetMapping
    public ResponseEntity<List<SavingsGoalResponse>> getAllGoals(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        List<SavingsGoalResponse> goals = savingsGoalService.getAllGoals(user);
        return ResponseEntity.ok(goals);
    }

    /**
     * Retrieves a specific savings goal by ID for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param id The ID of the savings goal to retrieve
     * @return ResponseEntity containing the requested savings goal
     * @throws BadRequestException if user is not authenticated
     */
    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> getGoal(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable Long id) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        SavingsGoalResponse response = savingsGoalService.getGoal(user, id);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates a specific savings goal for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param id The ID of the savings goal to update
     * @param request The update request containing new goal details
     * @return ResponseEntity containing the updated savings goal
     * @throws BadRequestException if user is not authenticated
     */
    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> updateGoal(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable Long id,
                                                         @RequestBody GoalUpdateRequest request) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        SavingsGoalResponse response = savingsGoalService.updateGoal(user, id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a specific savings goal for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param id The ID of the savings goal to delete
     * @return ResponseEntity with success message
     * @throws BadRequestException if user is not authenticated
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable Long id) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        savingsGoalService.deleteGoal(user, id);
        return ResponseEntity.ok().body("Goal deleted successfully");
    }
} 