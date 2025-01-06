package com.example.habittracker.controllers;

import com.example.habittracker.services.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String showDashboard(Model model) {
        model.addAttribute("totalUsers", dashboardService.getTotalUsers());
        model.addAttribute("totalHabits", dashboardService.getTotalHabits());
        model.addAttribute("totalCompletedHabits", dashboardService.getTotalCompletedHabits());
        model.addAttribute("topUsersByLogs", dashboardService.getTopUsersByHabitLogs(5));
        return "dashboard/index";
    }
}
