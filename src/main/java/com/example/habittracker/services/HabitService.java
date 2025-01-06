package com.example.habittracker.services;

import com.example.habittracker.dao.HabitDAO;
import com.example.habittracker.models.Habit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {

    @Autowired
    private HabitDAO habitDAO;

    public List<Habit> getAllHabits() {
        return habitDAO.getAllHabits();
    }

    public Habit getHabitById(Long id) {
        return habitDAO.getHabit(id);
    }

    public void saveHabit(Habit habit) {
        if (habit.getUser() == null) {
            throw new IllegalArgumentException("Habit must be associated with a user.");
        }
        habitDAO.saveHabit(habit);
    }


    public void deleteHabit(Long id) {
        habitDAO.deleteHabit(id);
    }
    
    public List<Habit> getHabitsByUser(Long userId) {
        return habitDAO.getHabitsByUser(userId);
    }

}
