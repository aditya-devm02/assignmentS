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

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid request format or date format. Please use yyyy-MM-dd."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "Validation failed: " + ex.getMessage()));
    }
} 