package com.finance.manager.controller;

import com.finance.manager.dto.report.MonthlyReportResponse;
import com.finance.manager.dto.report.YearlyReportResponse;
import com.finance.manager.entity.User;
import com.finance.manager.service.ReportService;
import com.finance.manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final UserService userService;

    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @PathVariable int year,
                                                                 @PathVariable int month) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        if (month < 1 || month > 12) {
            return ResponseEntity.badRequest().body(null);
        }
        String username = userDetails.getUsername();
        System.out.println("[DEBUG] Authenticated username: " + username);
        User user = userService.getUserByUsername(username);
        System.out.println("[DEBUG] User lookup result: " + user);
        MonthlyReportResponse response = reportService.getMonthlyReport(user, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/yearly/{year}")
    public ResponseEntity<YearlyReportResponse> getYearlyReport(@AuthenticationPrincipal UserDetails userDetails,
                                                               @PathVariable int year) {
        if (userDetails == null) {
            throw new com.finance.manager.exception.BadRequestException("User not authenticated");
        }
        User user = userService.getUserByUsername(userDetails.getUsername());
        YearlyReportResponse response = reportService.getYearlyReport(user, year);
        return ResponseEntity.ok(response);
    }
} 