package com.finance.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.manager.dto.transaction.TransactionRequest;
import com.finance.manager.dto.transaction.TransactionResponse;
import com.finance.manager.dto.transaction.TransactionUpdateRequest;
import com.finance.manager.entity.User;

import com.finance.manager.service.TransactionService;
import com.finance.manager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(com.finance.manager.config.TestSecurityConfig.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserService userService;

    private User testUser;
    private TransactionRequest transactionRequest;
    private TransactionResponse transactionResponse;
    private TransactionUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(new BigDecimal("100.00"));
        transactionRequest.setDate(LocalDate.now());
        transactionRequest.setCategory("Test Category");
        transactionRequest.setDescription("Test transaction");

        transactionResponse = new TransactionResponse();
        transactionResponse.setId(1L);
        transactionResponse.setAmount(new BigDecimal("100.00"));
        transactionResponse.setDate(LocalDate.now());
        transactionResponse.setCategory("Test Category");
        transactionResponse.setDescription("Test transaction");

        updateRequest = new TransactionUpdateRequest();
        updateRequest.setAmount(new BigDecimal("200.00"));
        updateRequest.setDescription("Updated transaction");
        updateRequest.setCategory("Updated Category");
    }

    @Test
    void createTransaction_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(transactionService.createTransaction(any(User.class), any(TransactionRequest.class)))
                .thenReturn(transactionResponse);

        mockMvc.perform(post("/transactions")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.category").value("Test Category"));

        verify(transactionService).createTransaction(any(User.class), any(TransactionRequest.class));
    }

    @Test
    void createTransaction_Unauthenticated() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTransactions_Success() throws Exception {
        List<TransactionResponse> transactions = Arrays.asList(transactionResponse);
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(transactionService.getTransactions(any(User.class), any(), any(), any(), any()))
                .thenReturn(transactions);

        mockMvc.perform(get("/transactions")
                        .with(user("test@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[0].category").value("Test Category"));

        verify(transactionService).getTransactions(any(User.class), any(), any(), any(), any());
    }

    @Test
    void updateTransaction_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(transactionService.updateTransaction(any(User.class), anyLong(), any(TransactionUpdateRequest.class)))
                .thenReturn(transactionResponse);

        mockMvc.perform(put("/transactions/1")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.category").value("Test Category"));

        verify(transactionService).updateTransaction(any(User.class), anyLong(), any(TransactionUpdateRequest.class));
    }

    @Test
    void deleteTransaction_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        doNothing().when(transactionService).deleteTransaction(any(User.class), anyLong());

        mockMvc.perform(delete("/transactions/1")
                        .with(user("test@example.com")))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction deleted successfully"));

        verify(transactionService).deleteTransaction(any(User.class), anyLong());
    }

    @Test
    void handleInvalidFormat() throws Exception {
        mockMvc.perform(post("/transactions")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void handleValidation() throws Exception {
        transactionRequest.setAmount(null);
        mockMvc.perform(post("/transactions")
                        .with(user("test@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
} 