package com.example.habittracker.controllers;

import com.example.habittracker.models.Habit;
import com.example.habittracker.models.User;
import com.example.habittracker.services.HabitService;
import com.example.habittracker.services.UserService;
import com.example.habittracker.utils.PdfGenerator;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;
    
    @Autowired
    private final UserService userService;
    
    public HabitController(HabitService habitService, UserService userService) {
        this.habitService = habitService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String viewHabitLogs(@PathVariable Long id, Model model) {
        Habit habit = habitService.getHabitById(id);
        model.addAttribute("habit", habit);
        model.addAttribute("logs", habit.getHabitLogs());
        return "habits/view";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("habit", new Habit());
        return "habits/add";
    }

    @PostMapping
    public String saveHabit(@ModelAttribute Habit habit, Authentication authentication) {
        // Get the logged-in user's username
        String username = authentication.getName();

        // Fetch the full User entity from the database
        User loggedInUser = userService.findByUsername(username);

        // Set the user for the habit
        habit.setUser(loggedInUser);

        // Save the habit
        habitService.saveHabit(habit);

        return "redirect:/habits";
    }

    @GetMapping("/delete/{id}")
    public String deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return "redirect:/habits";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditHabitForm(@PathVariable Long id, Authentication authentication, Model model) {
        // Get the logged-in user's username
        String username = authentication.getName();

        // Fetch the full User entity from the database
        User loggedInUser = userService.findByUsername(username);

        // Fetch the habit and check ownership
        Habit habit = habitService.getHabitById(id);
        if (!habit.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to edit this habit.");
        }

        // Format the start date to yyyy-MM-dd
        String formattedStartDate = habit.getStartDate().toString(); // LocalDate default format is yyyy-MM-dd
        model.addAttribute("habit", habit);
        model.addAttribute("formattedStartDate", formattedStartDate);

        return "habits/edit";
    }


    @PostMapping("/update/{id}")
    public String updateHabit(@PathVariable Long id, @ModelAttribute Habit habit, Authentication authentication) {
        // Get the logged-in user's username
        String username = authentication.getName();

        // Fetch the full User entity from the database
        User loggedInUser = userService.findByUsername(username);

        // Fetch the existing habit and check ownership
        Habit existingHabit = habitService.getHabitById(id);
        if (!existingHabit.getUser().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You are not authorized to update this habit.");
        }

        // Update the habit's fields
        existingHabit.setName(habit.getName());
        existingHabit.setDescription(habit.getDescription());
        existingHabit.setStartDate(habit.getStartDate());
        existingHabit.setTargetDays(habit.getTargetDays());

        // Save the updated habit
        habitService.saveHabit(existingHabit);

        return "redirect:/habits";
    }

    
    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportHabitsToPdf() {
        List<Habit> habits = habitService.getAllHabits();
        byte[] pdfData = PdfGenerator.generateHabitReport(habits);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=habit_report.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfData);
    }
    
    @GetMapping
    public String listHabits(Authentication authentication, Model model) {
    	String username = authentication.getName();
    	
    	boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

		// Conditionally add the Dashboard link
		if (isAdmin) {
			model.addAttribute("dashboardLink", "/dashboard");
		}

        // Fetch the full User entity from the database
        User loggedInUser = userService.findByUsername(username);
        // Fetch habits for the logged-in user
        model.addAttribute("habits", habitService.getHabitsByUser(loggedInUser.getId()));

        return "habits/list"; // Thymeleaf template
    }
}
