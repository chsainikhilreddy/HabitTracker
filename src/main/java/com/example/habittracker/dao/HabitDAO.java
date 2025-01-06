package com.example.habittracker.dao;

import com.example.habittracker.models.Habit;

import java.util.List;

public interface HabitDAO {
    List<Habit> getAllHabits();
    Habit getHabit(Long id);
    void saveHabit(Habit habit);
    void deleteHabit(Long id);
    public List<Habit> getHabitsByUser(Long userId);
    public long countHabits();
}
