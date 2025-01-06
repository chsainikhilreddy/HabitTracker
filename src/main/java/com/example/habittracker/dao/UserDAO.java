package com.example.habittracker.dao;

import com.example.habittracker.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public List<User> getAllUsers() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM User", User.class).list();
    }

    @Transactional
    public User getUserById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Transactional
    public void saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        if (user.getId() == null) {
            session.persist(user); // Persist for new entities
        } else {
            session.merge(user); // Merge for updates
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if (user != null) {
            session.remove(user); // Remove instead of delete
        }
    }
    
    @Transactional
    public User findByUsername(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM User WHERE username = :username", User.class)
                      .setParameter("username", username)
                      .uniqueResult();
    }
    
    public long countUsers() {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(u) FROM User u", Long.class)
            .uniqueResult();
    }

    public List<User> findTopUsersByHabitCount(int limit) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT u FROM User u JOIN u.habits h GROUP BY u ORDER BY COUNT(h) DESC", User.class)
            .setMaxResults(limit)
            .list();
    }

    public List<User> findTopUsersByHabitLogs(int limit) {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT u FROM User u JOIN HabitLog hl ON u.id = hl.user.id GROUP BY u ORDER BY COUNT(hl) DESC", User.class)
            .setMaxResults(limit)
            .list();
    }
    
    public List<Object[]> getTopUsersWithLogCounts(int limit) {
        return sessionFactory.getCurrentSession()
            .createQuery(
                "SELECT u.username, COUNT(hl) FROM HabitLog hl JOIN hl.user u GROUP BY u ORDER BY COUNT(hl) DESC",
                Object[].class
            )
            .setMaxResults(limit)
            .list();
    }
}
