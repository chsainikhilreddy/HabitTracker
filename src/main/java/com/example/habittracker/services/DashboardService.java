package com.example.habittracker.services;

import com.example.habittracker.dao.UserDAO;
import com.example.habittracker.dao.HabitDAO;
import com.example.habittracker.dao.HabitLogDAO;
import com.example.habittracker.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final UserDAO userDAO;
    private final HabitDAO habitDAO;
    private final HabitLogDAO habitLogDAO;

    public DashboardService(UserDAO userDAO, HabitDAO habitDAO, HabitLogDAO habitLogDAO) {
        this.userDAO = userDAO;
        this.habitDAO = habitDAO;
        this.habitLogDAO = habitLogDAO;
    }

    public long getTotalUsers() {
        return userDAO.countUsers();
    }

    public long getTotalHabits() {
        return habitDAO.countHabits();
    }

    public long getTotalCompletedHabits() {
        return habitLogDAO.countCompletedHabits();
    }

    public List<User> getTopUsersByHabitCount(int limit) {
        return userDAO.findTopUsersByHabitCount(limit);
    }

    public List<Object[]> getTopUsersByHabitLogs(int limit) {
        return userDAO.getTopUsersWithLogCounts(limit);
    }
}
