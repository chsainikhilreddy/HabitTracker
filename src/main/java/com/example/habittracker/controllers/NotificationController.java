package com.example.habittracker.controllers;

import com.example.habittracker.models.Notification;
import com.example.habittracker.models.User;
import com.example.habittracker.services.NotificationService;
import com.example.habittracker.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // Allow only admins
    @GetMapping("/add")
    public String showAddNotificationForm(Model model) {
        model.addAttribute("notification", new Notification());
        model.addAttribute("users", userService.getAllUsers());
        return "notifications/add";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public String addNotification(@ModelAttribute Notification notification, @RequestParam Long userId) {
        User recipient = userService.getUserById(userId);
        notification.setRecipient(recipient);
        // Format the current date-time as a String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        notification.setCreatedAt(LocalDateTime.now().format(formatter));

        notificationService.saveNotification(notification);
        return "redirect:/notifications";
    }
    
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String showEditNotificationForm(@PathVariable Long id, Model model) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification == null) {
            throw new IllegalArgumentException("Notification not found with ID: " + id);
        }
        model.addAttribute("notification", notification);
        model.addAttribute("users", userService.getAllUsers()); // To allow editing recipients
        return "notifications/edit";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/edit/{id}")
    public String editNotification(@PathVariable Long id, @ModelAttribute Notification notification, @RequestParam Long userId) {
        Notification existingNotification = notificationService.getNotificationById(id);
        if (existingNotification == null) {
            throw new IllegalArgumentException("Notification not found with ID: " + id);
        }
        User recipient = userService.getUserById(userId);
        existingNotification.setMessage(notification.getMessage());
        existingNotification.setRecipient(recipient);
        notificationService.saveNotification(existingNotification);
        return "redirect:/notifications";
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }

    @GetMapping("")
    public String viewNotifications(Authentication authentication, Model model) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUsername(username);

        // Check if the user has the ADMIN role
        boolean isAdmin = authentication.getAuthorities().stream()
                                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<Notification> notifications;
        if (isAdmin) {
            // Admin can view all notifications
            notifications = notificationService.getAllNotifications();
        } else {
            // Users can view only their own notifications
            notifications = notificationService.getNotificationsByUserId(loggedInUser.getId());
        }

        model.addAttribute("notifications", notifications);
        model.addAttribute("isAdmin", isAdmin); // Pass role information to the template
        return "notifications/admin"; // Use the same page for both roles
    }

}
