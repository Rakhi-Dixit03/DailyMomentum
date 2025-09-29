package daoImpl;

import dao.BaseDao;
import dao.HabitsLogsDao;
import entity.Habit;
import entity.HabitLogs;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class HabitsLogImpl extends BaseDao<HabitLogs> implements HabitsLogsDao {

    public HabitsLogImpl(){

        super(HabitLogs.class);

    }


    public void updateHabitLog(int id, HabitLogs updates) {

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            try {
                tx = session.beginTransaction();
                HabitLogs existing = session.get(HabitLogs.class, id);

                if (existing.getDate() != null) {
                    existing.setDate(updates.getDate());
                }

                if (existing.getStatus() != null) {
                    existing.setStatus(updates.getStatus());
                }


                if (existing.getHabit() != null) {
                    existing.setHabit(updates.getHabit());
                }

                tx.commit();
                System.out.println("Operation Successful");

            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                e.printStackTrace();
            }

        }


    }
}