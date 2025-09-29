package dao;

import entity.Habit;
import entity.User;

public interface HabitDao {

    void updateHabit(String name, Habit updates,User user);
    Habit getHabitByUserAndTitle(User user, String title);
    void createHabit(Habit habit);
    int getBestStreak(String habitName,User user);
    int getCurrentStreak(String habitName,User user);
    void afterLoginSummary(User u);


}
