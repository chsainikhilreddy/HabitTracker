package com.example.habittracker.controllers;

import com.example.habittracker.models.User;
import com.example.habittracker.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles the login page request.
     * Redirects authenticated users to the habits page to avoid redirection loops.
     */
    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Redirect already authenticated users to the default success URL
            return "redirect:/habits";
        }
        return "login"; // Show the login page for unauthenticated users
    }

    /**
     * Handles the signup page request.
     * Displays the signup form.
     */
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User()); // Provide a blank user object for form binding
        return "signup";
    }

    /**
     * Handles signup form submission.
     * Creates a new user and redirects to the login page.
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, Model model) {
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists!");
            return "signup";
        }
        userService.saveUser(user); // Save the new user
        return "redirect:/login";
    }
}
