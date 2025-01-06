package com.example.habittracker.dao;

import com.example.habittracker.models.HabitLog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public class HabitLogDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public List<HabitLog> getLogsByHabitId(Long habitId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM HabitLog WHERE habit.id = :habitId", HabitLog.class)
                .setParameter("habitId", habitId)
                .list();
    }

    @Transactional
    public HabitLog getLogByDate(Long habitId, LocalDate logDate) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM HabitLog WHERE habit.id = :habitId AND logDate = :logDate", HabitLog.class)
                .setParameter("habitId", habitId)
                .setParameter("logDate", logDate)
                .uniqueResult();
    }

    @Transactional
    public void saveLog(HabitLog habitLog) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(habitLog);
    }

    @Transactional
    public void updateLog(HabitLog habitLog) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(habitLog);
    }

    @Transactional
    public void deleteLog(Long logId) {
        Session session = sessionFactory.getCurrentSession();
        HabitLog habitLog = session.get(HabitLog.class, logId);
        if (habitLog != null) {
            session.remove(habitLog);
        }
    }
    
    @Transactional(readOnly = true)
    public HabitLog getLogById(Long logId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(HabitLog.class, logId);
    }
    
    @Transactional(readOnly = true)
    public List<HabitLog> getLogsByUser(Long userId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM HabitLog WHERE user.id = :userId", HabitLog.class)
                      .setParameter("userId", userId)
                      .list();
    }
    
    public long countCompletedHabits() {
        return sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(hl) FROM HabitLog hl WHERE hl.completed = true", Long.class)
            .uniqueResult();
    }


}
