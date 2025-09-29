package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="Habits_logs")
public class HabitLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id;

    @ManyToOne
    @JoinColumn(name="habit_id",nullable = false)
    private Habit habit;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HabitStatus status;

    public HabitLogs() {
    }

    public HabitLogs(Habit habit, LocalDate date, HabitStatus status) {
        this.habit = habit;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HabitStatus getStatus() {
        return status;
    }

    public void setStatus(HabitStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return " HabitLogs : [" +
                "id = " + id +
                ", habit = " + habit.getTitle() +
                ", date = " + date +
                ", status = " + status +
                "]";
    }
}
