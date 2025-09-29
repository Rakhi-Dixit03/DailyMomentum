package daoImpl;

import exception.HabitException;
import dao.BaseDao;
import dao.HabitDao;
import entity.Habit;
import entity.HabitLogs;
import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.Comparator;
import java.util.List;

public class HabitDaoImpl extends BaseDao<Habit> implements HabitDao {

    public HabitDaoImpl() {
        super(Habit.class);

    }

    public void updateHabit(String name, Habit updates, User user) {

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            try {
                tx = session.beginTransaction();
                Habit existing = session.createQuery("FROM Habit WHERE title=:x AND user=:user", Habit.class).setParameter("x", name).setParameter("user", user).uniqueResult();


                if (updates.getTitle() != null) {
                    existing.setTitle(updates.getTitle());
                }

                if (updates.getDescription() != null) {
                    existing.setDescription(updates.getDescription());
                }


                if (updates.getFrequency() != null) {
                    existing.setFrequency(updates.getFrequency());
                }
                tx.commit();
                System.out.println("Habit Updated Successfully");

            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw e;
            }

        }


    }

    @Override
    public Habit getHabitByUserAndTitle(User user, String title) {


        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Habit h WHERE h.user= :user AND h.title= :title ", Habit.class).setParameter("user", user)
                    .setParameter("title", title).uniqueResult();


        }

    }

    public List<HabitLogs> getAllLogs(Habit h) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM HabitLogs h WHERE h.habit= :h ", HabitLogs.class)
                    .setParameter("h", h).list();


        }

    }


    public void createHabit(Habit habit) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            //Checking if habit already exists

            Habit existing = session.createQuery("FROM Habit h WHERE h.user=:user AND h.title=:title", Habit.class)
                    .setParameter("user", habit.getUser()).setParameter("title", habit.getTitle()).uniqueResult();

            if (existing != null) {
                throw new HabitException("You already have a habit with this name.Cannot have two habits with same name.Enter a unique name.");

            }
            save(habit);
            tx.commit();

        } catch (Exception e) {
            throw e;
        }

    }


    public int getCurrentStreak(String habitName, User user) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Habit h = session.createQuery("FROM Habit habit  WHERE habit.title=:x AND habit.user=:user", Habit.class).setParameter("x", habitName).setParameter("user", user).uniqueResult();
            if (h == null) {
                throw new HabitException("Habit doesn't exist.");
//          System.out.println("Habit doesn't exist.");

            }

            return h.getCurrentStreak();
        }catch (Exception e){
            throw  e;
        }
    }

    public int getBestStreak(String habitName, User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Habit h = session.createQuery("FROM Habit habit WHERE habit.title=:x AND habit.user=:user", Habit.class)
                    .setParameter("x", habitName).setParameter("user", user).uniqueResult();
            if (h == null) {
                throw new HabitException("Habit doesn't exist.");
            }

            return h.getBestStreak();
        }catch (Exception e){
            throw e;
        }
    }

    public void afterLoginSummary(User u) {

        if (u != null) {

            int id = u.getId();
            List<Habit> list = getAll(id);
            if (!list.isEmpty()) {

                Habit bestHabit = list.stream()
                        .max(Comparator.comparingInt(Habit::getBestStreak))
                        .orElse(null);

                if (bestHabit != null) {

                    if(bestHabit.getBestStreak()>1 && list.size()>1){
                        System.out.println("Welcome back👋, " + u.getName() + "! You have " + list.size()+ " active habits and  your best streak is " + bestHabit.getBestStreak() + " days on habit " + bestHabit.getTitle() + "\" 🎉");
                    }else{

                        System.out.println("Welcome back👋, " + u.getName() + "! You have " + list.size()+ " active habit and  your best streak is " + bestHabit.getBestStreak() + " day on habit " + bestHabit.getTitle() + "\" 🎉");

                    }


                }

            }

        }
    }
}
