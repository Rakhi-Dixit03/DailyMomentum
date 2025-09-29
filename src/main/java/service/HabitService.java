package service;

import entity.Habit;
import entity.HabitFrequency;
import entity.HabitLogs;
import entity.User;

import java.util.List;

public interface HabitService {

     void markHabitDone(String habitName, User user);
     List<HabitLogs> getHabitLogs(String title,User user);
     void createHabit(Habit habit);
     HabitFrequency getFrequencyInput();
     void getHabit(String title,User user);
     int getBestStreak(String habitName,User user);
     int getCurrentStreak(String habitName,User user);
     public void isBlank(String str);
     public  void inputValidator(Habit h);
      void afterLoginSummary(User u);

}
