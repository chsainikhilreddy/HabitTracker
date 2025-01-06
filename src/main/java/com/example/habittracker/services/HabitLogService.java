package com.example.habittracker.services;

import com.example.habittracker.dao.HabitLogDAO;
import com.example.habittracker.models.HabitLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HabitLogService {

    @Autowired
    private HabitLogDAO habitLogDAO;

    public List<HabitLog> getLogsByHabitId(Long habitId) {
        return habitLogDAO.getLogsByHabitId(habitId);
    }

    public HabitLog getLogByDate(Long habitId, LocalDate logDate) {
        return habitLogDAO.getLogByDate(habitId, logDate);
    }

    public void saveLog(HabitLog habitLog) {
        habitLogDAO.saveLog(habitLog);
    }

    public void updateLog(HabitLog habitLog) {
        habitLogDAO.updateLog(habitLog);
    }

    public void deleteLog(Long logId) {
        habitLogDAO.deleteLog(logId);
    }
    
    public HabitLog getLogById(Long logId) {
        return habitLogDAO.getLogById(logId);
    }
    
    public List<HabitLog> getLogsByUser(Long userId) {
        return habitLogDAO.getLogsByUser(userId);
    }


}
