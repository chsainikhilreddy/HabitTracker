package com.example.habittracker.dao;

import com.example.habittracker.models.Habit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class HabitDAOImpl implements HabitDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(readOnly = true)
    public List<Habit> getAllHabits() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Habit", Habit.class).list();
    }

    @Transactional(readOnly = true)
    public Habit getHabit(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Habit.class, id);
    }

    @Transactional
    public void saveHabit(Habit habit) {
        Session session = sessionFactory.getCurrentSession();
        if (habit.getId() == null) {
            session.persist(habit); // For new entities
        } else {
            session.merge(habit); // For updates
        }
    }

    @Transactional
    public void deleteHabit(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Habit habit = session.get(Habit.class, id);
        if (habit != null) {
            session.remove(habit);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Habit> getHabitsByUser(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Habit WHERE user.id = :userId", Habit.class)
                      .setParameter("userId", userId)
                      .list();
    }
    
    public long countHabits() {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(h) FROM Habit h", Long.class)
            .uniqueResult();
    }
}
