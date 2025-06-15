package com.finance.manager.controller;

import com.finance.manager.dto.transaction.TransactionRequest;
import com.finance.manager.dto.transaction.TransactionResponse;
import com.finance.manager.dto.transaction.TransactionUpdateRequest;
import com.finance.manager.entity.User;
import com.finance.manager.service.TransactionService;
import com.finance.manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller handling all transaction-related operations including creation,
 * retrieval, updating, and deletion of financial transactions.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;

    /**
     * Constructs a TransactionController with required dependencies.
     *
     * @param transactionService Service for transaction-related operations
     * @param userService Service for user-related operations
     */
    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    /**
     * Creates a new transaction for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param request Transaction creation request containing transaction details
     * @return ResponseEntity containing the created transaction details
     * @throws BadRequestException if user is not authenticated
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@AuthenticationPrincipal UserDetails userDetails,
                                                                @Valid @RequestBody TransactionRequest request) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        TransactionResponse response = transactionService.createTransaction(user, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves transactions for the authenticated user with optional filtering.
     *
     * @param userDetails The authenticated user's details
     * @param startDate Optional start date for filtering transactions
     * @param endDate Optional end date for filtering transactions
     * @param categoryId Optional category ID for filtering transactions
     * @param category Optional category name for filtering transactions
     * @return ResponseEntity containing list of filtered transactions
     * @throws BadRequestException if user is not authenticated
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String category) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        List<TransactionResponse> transactions = transactionService.getTransactions(user, startDate, endDate, categoryId, category);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Updates an existing transaction for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param id ID of the transaction to update
     * @param request Transaction update request containing new details
     * @return ResponseEntity containing the updated transaction details
     * @throws BadRequestException if user is not authenticated
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@AuthenticationPrincipal UserDetails userDetails,
                                                                @PathVariable Long id,
                                                                @RequestBody TransactionUpdateRequest request) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        TransactionResponse response = transactionService.updateTransaction(user, id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a transaction for the authenticated user.
     *
     * @param userDetails The authenticated user's details
     * @param id ID of the transaction to delete
     * @return ResponseEntity with success message
     * @throws BadRequestException if user is not authenticated
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long id) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        transactionService.deleteTransaction(user, id);
        return ResponseEntity.ok().body("Transaction deleted successfully");
    }

    /**
     * Handles invalid request format exceptions.
     *
     * @param ex The exception that was thrown
     * @return ResponseEntity with error message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid request format or date format. Please use yyyy-MM-dd."));
    }

    /**
     * Handles validation exceptions.
     *
     * @param ex The exception that was thrown
     * @return ResponseEntity with validation error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Validation failed: " + ex.getMessage()));
    }
} 