package com.example.habittracker.controllers;

import com.example.habittracker.models.Habit;
import com.example.habittracker.models.HabitLog;
import com.example.habittracker.models.User;
import com.example.habittracker.services.HabitLogService;
import com.example.habittracker.services.HabitService;
import com.example.habittracker.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/habit-logs")
public class HabitLogController {

    @Autowired
    private HabitLogService habitLogService;

    @Autowired
    private HabitService habitService;
    
    private final UserService userService;
    
    public HabitLogController(HabitService habitService, UserService userService) {
        this.habitService = habitService;
        this.userService = userService;
    }

    @GetMapping("/{habitId}/add")
    public String showAddLogForm(@PathVariable Long habitId, Authentication authentication, Model model) {
        // Get logged-in user
        String username = authentication.getName();
        User loggedInUser = userService.findByUsername(username);

        // Fetch the habit and check ownership
        Habit habit = habitService.getHabitById(habitId);
        if (!habit.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to add logs to this habit.");
        }

        HabitLog habitLog = new HabitLog();
        habitLog.setHabit(habit);

        model.addAttribute("habitLog", habitLog);
        return "habit-logs/add";
    }

    @PostMapping("/{habitId}/save")
    public String saveHabitLog(@PathVariable Long habitId, @ModelAttribute HabitLog habitLog, Authentication authentication) {
        // Get logged-in user
        String username = authentication.getName();
        User loggedInUser = userService.findByUsername(username);

        // Fetch the habit and check ownership
        Habit habit = habitService.getHabitById(habitId);
        if (!habit.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to add logs to this habit.");
        }

        // Associate the log with the habit and user
        habitLog.setHabit(habit);
        habitLog.setUser(loggedInUser);

        // Save the habit log
        habitLogService.saveLog(habitLog);

        return "redirect:/habits/" + habitId;
    }
    
    @GetMapping("/{habitId}/edit/{logId}")
    public String showEditLogForm(
            @PathVariable Long habitId,
            @PathVariable Long logId,
            Authentication authentication,
            Model model
    ) {
    	// Get the logged-in user's username
        String username = authentication.getName();
        User loggedInUser = userService.findByUsername(username);

        // Fetch the habit and check ownership
        Habit habit = habitService.getHabitById(habitId);
        if (!habit.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to edit logs for this habit.");
        }

        // Fetch the habit log and check ownership
        HabitLog habitLog = habitLogService.getLogById(logId);
        if (!habitLog.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to edit this log.");
        }

        // Format the log date to yyyy-MM-dd
        String formattedLogDate = habitLog.getLogDate().toString(); // LocalDate default format is yyyy-MM-dd
        model.addAttribute("habitLog", habitLog);
        model.addAttribute("formattedLogDate", formattedLogDate);

        return "habit-logs/edit";
    }

    @PostMapping("/{habitId}/update/{logId}")
    public String updateHabitLog(
            @PathVariable Long habitId,
            @PathVariable Long logId,
            @ModelAttribute HabitLog habitLog,
            Authentication authentication
    ) {
        // Get logged-in user's username
        String username = authentication.getName();
        User loggedInUser = userService.findByUsername(username);

        // Fetch the habit and check ownership
        Habit habit = habitService.getHabitById(habitId);
        if (!habit.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to edit logs for this habit.");
        }

        // Fetch the existing habit log and check ownership
        HabitLog existingLog = habitLogService.getLogById(logId);
        if (!existingLog.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to edit this log.");
        }

        // Update the habit log fields
        existingLog.setLogDate(habitLog.getLogDate());
        existingLog.setCompleted(habitLog.isCompleted());

        // Save the updated habit log
        habitLogService.saveLog(existingLog);

        return "redirect:/habits/" + habitId;
    }
    
    @GetMapping("/{habitId}")
    public String viewLogs(@PathVariable Long habitId, Authentication authentication, Model model) {
        // Get the logged-in user's username
        String username = authentication.getName();

        // Fetch the full User entity from the database
        User loggedInUser = userService.findByUsername(username);

        // Fetch the habit and verify ownership
        Habit habit = habitService.getHabitById(habitId);
        if (!habit.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to view this habit's logs.");
        }

        model.addAttribute("habit", habit);
        model.addAttribute("logs", habit.getHabitLogs());
        return "habit-logs/list";
    }
    
    @GetMapping("/{habitId}/delete/{logId}")
    public String deleteHabitLog(@PathVariable Long habitId, @PathVariable Long logId, Authentication authentication) {
        // Get logged-in user's username
        String username = authentication.getName();
        User loggedInUser = userService.findByUsername(username);

        // Fetch the habit and check ownership
        Habit habit = habitService.getHabitById(habitId);
        if (!habit.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to delete logs for this habit.");
        }

        // Fetch the habit log and check ownership
        HabitLog habitLog = habitLogService.getLogById(logId);
        if (!habitLog.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to delete this log.");
        }

        // Delete the habit log
        habitLogService.deleteLog(logId);

        return "redirect:/habits/" + habitId;
    }


}
