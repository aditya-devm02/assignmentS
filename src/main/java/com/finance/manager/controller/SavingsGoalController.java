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

@RestController
@RequestMapping("/goals")
public class SavingsGoalController {
    private final SavingsGoalService savingsGoalService;
    private final UserService userService;

    public SavingsGoalController(SavingsGoalService savingsGoalService, UserService userService) {
        this.savingsGoalService = savingsGoalService;
        this.userService = userService;
    }

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