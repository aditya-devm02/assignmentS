package com.finance.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.manager.config.TestSecurityConfig;
import com.finance.manager.dto.goal.SavingsGoalRequest;
import com.finance.manager.dto.goal.SavingsGoalResponse;
import com.finance.manager.dto.goal.GoalUpdateRequest;
import com.finance.manager.entity.User;
import com.finance.manager.service.SavingsGoalService;
import com.finance.manager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SavingsGoalController.class)
@Import(TestSecurityConfig.class)
public class SavingsGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SavingsGoalService savingsGoalService;

    @MockBean
    private UserService userService;

    private User testUser;
    private SavingsGoalRequest savingsGoalRequest;
    private SavingsGoalResponse savingsGoalResponse;
    private GoalUpdateRequest goalUpdateRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test@example.com");

        savingsGoalRequest = new SavingsGoalRequest();
        savingsGoalRequest.setGoalName("Vacation Fund");
        savingsGoalRequest.setTargetAmount(new BigDecimal("5000.00"));
        savingsGoalRequest.setTargetDate(LocalDate.now().plusMonths(6));
        savingsGoalRequest.setStartDate(LocalDate.now());

        savingsGoalResponse = new SavingsGoalResponse();
        savingsGoalResponse.setId(1L);
        savingsGoalResponse.setGoalName("Vacation Fund");
        savingsGoalResponse.setTargetAmount(new BigDecimal("5000.00"));
        savingsGoalResponse.setTargetDate(LocalDate.now().plusMonths(6));
        savingsGoalResponse.setStartDate(LocalDate.now());
        savingsGoalResponse.setCurrentProgress(new BigDecimal("1000.00"));
        savingsGoalResponse.setProgressPercentage(new BigDecimal("20.00"));
        savingsGoalResponse.setRemainingAmount(new BigDecimal("4000.00"));

        goalUpdateRequest = new GoalUpdateRequest();
        goalUpdateRequest.setGoalName("Updated Vacation Fund");
        goalUpdateRequest.setTargetAmount(new BigDecimal("6000.00"));
        goalUpdateRequest.setTargetDate(LocalDate.now().plusMonths(12));
        goalUpdateRequest.setStartDate(LocalDate.now().plusDays(5));
    }

    @Test
    @WithMockUser
    void createGoal_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(savingsGoalService.createGoal(any(User.class), any(SavingsGoalRequest.class)))
                .thenReturn(savingsGoalResponse);

        mockMvc.perform(post("/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savingsGoalRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.goalName").value("Vacation Fund"));
    }

    @Test
    @WithMockUser
    void createGoal_InvalidRequest() throws Exception {
        savingsGoalRequest.setGoalName(null);

        mockMvc.perform(post("/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(savingsGoalRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getAllGoals_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        List<SavingsGoalResponse> goals = Arrays.asList(savingsGoalResponse);
        when(savingsGoalService.getAllGoals(any(User.class))).thenReturn(goals);

        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].goalName").value("Vacation Fund"));
    }

    @Test
    @WithMockUser
    void getGoal_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(savingsGoalService.getGoal(any(User.class), anyLong())).thenReturn(savingsGoalResponse);

        mockMvc.perform(get("/goals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.goalName").value("Vacation Fund"));
    }

    @Test
    @WithMockUser
    void updateGoal_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);
        when(savingsGoalService.updateGoal(any(User.class), anyLong(), any(GoalUpdateRequest.class)))
                .thenReturn(savingsGoalResponse);

        mockMvc.perform(put("/goals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goalUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.goalName").value("Vacation Fund"));
    }

    @Test
    @WithMockUser
    void deleteGoal_Success() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);

        mockMvc.perform(delete("/goals/1"))
                .andExpect(status().isOk());
    }
} 