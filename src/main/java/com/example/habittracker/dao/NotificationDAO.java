package com.example.habittracker.dao;

import com.example.habittracker.models.Notification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class NotificationDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void saveNotification(Notification notification) {
        Session session = sessionFactory.getCurrentSession();
        if (notification.getId() == null) {
            session.persist(notification); // Use persist for new notifications
        } else {
            session.merge(notification); // Use merge for updates
        }
    }

    @Transactional(readOnly = true)
    public Notification getNotificationById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Notification.class, id);
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByUserId(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Notification WHERE recipient.id = :userId", Notification.class)
                      .setParameter("userId", userId)
                      .list();
    }

    @Transactional
    public void deleteNotification(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Notification notification = session.get(Notification.class, id);
        if (notification != null) {
            session.remove(notification); // Use remove for deletion
        }
    }
    
    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Notification", Notification.class).list();
    }

}
