package com.example.habittracker.services;

import com.example.habittracker.dao.NotificationDAO;
import com.example.habittracker.models.Notification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @Transactional
    public void saveNotification(Notification notification) {
        notificationDAO.saveNotification(notification);
    }

    @Transactional(readOnly = true)
    public Notification getNotificationById(Long id) {
        return notificationDAO.getNotificationById(id);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationDAO.getNotificationsByUserId(userId);
    }

    @Transactional
    public void deleteNotification(Long id) {
        notificationDAO.deleteNotification(id);
    }
    
    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications() {
        return notificationDAO.getAllNotifications();
    }

}
