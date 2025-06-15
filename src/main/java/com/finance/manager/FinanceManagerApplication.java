package com.finance.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Personal Finance Manager.
 * This class serves as the entry point for the Spring Boot application.
 * 
 * The application provides a comprehensive solution for managing personal finances,
 * including expense tracking, budget management, and financial goal setting.
 * 
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootApplication
public class FinanceManagerApplication {
    /**
     * Main method that starts the Spring Boot application.
     * 
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(FinanceManagerApplication.class, args);
    }
} 