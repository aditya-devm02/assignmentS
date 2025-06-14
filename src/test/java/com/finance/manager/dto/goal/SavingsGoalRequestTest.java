package com.finance.manager.dto.goal;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SavingsGoalRequestTest {

    private Validator validator;
    private SavingsGoalRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        request = new SavingsGoalRequest();
    }

    @Test
    void testGettersAndSetters() {
        request.setGoalName("Vacation Fund");
        request.setTargetAmount(new BigDecimal("1000.00"));
        request.setTargetDate(LocalDate.now().plusMonths(6));
        request.setStartDate(LocalDate.now());

        assertEquals("Vacation Fund", request.getGoalName());
        assertEquals(new BigDecimal("1000.00"), request.getTargetAmount());
        assertTrue(request.getTargetDate().isAfter(LocalDate.now()));
        assertEquals(LocalDate.now(), request.getStartDate());
    }

    @Test
    void testValidation_ValidRequest() {
        request.setGoalName("Vacation Fund");
        request.setTargetAmount(new BigDecimal("1000.00"));
        request.setTargetDate(LocalDate.now().plusMonths(6));

        var violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidation_InvalidRequest() {
        request.setGoalName("");
        request.setTargetAmount(new BigDecimal("0.00"));
        request.setTargetDate(LocalDate.now().minusDays(1));

        var violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }
} 