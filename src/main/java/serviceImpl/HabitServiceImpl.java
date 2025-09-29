package serviceImpl;

import daoImpl.HabitDaoImpl;
import daoImpl.UserDaoImpl;
import entity.*;

import exception.DatabaseException;
import exception.HabitException;
import exception.HabitLogException;
import exception.UserException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import service.HabitService;
import util.HibernateUtil;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HabitServiceImpl implements HabitService {

    Scanner sc=new Scanner(System.in);
    HabitDaoImpl h=new HabitDaoImpl();
    UserDaoImpl user=new UserDaoImpl();

    public void markHabitDone(String habitName, User user) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        Transaction tx = session.beginTransaction();

        Habit habit =(Habit) session.createQuery("FROM Habit WHERE title=:x AND user=:y").setParameter("x",habitName)
                        .setParameter("y",user).uniqueResult();

        if (habit == null) {
            throw new HabitException("Habit not found!");
//            System.out.println("Habit not found!");
//            return;
        }


        //check if Today's Log already exists

        HabitLogs existingLog = session.createQuery("FROM HabitLogs WHERE habit.title=:x  AND date=:y", HabitLogs.class).setParameter("x", habitName).setParameter("y", LocalDate.now()).uniqueResult();

        if (existingLog != null) {
            System.out.println("**************************");
            System.out.println("Today's habit is already marked.");
            System.out.println("**************************");
            return;
        }

        //Save today's DONE log

        HabitLogs newLog = new HabitLogs(habit, LocalDate.now(), HabitStatus.DONE);
        session.save(newLog);

        //check yesterday's log

        LocalDate yesterday = LocalDate.now().minusDays(1);

        HabitLogs yesterdayLog = session.createQuery
                        ("FROM HabitLogs WHERE habit.title=:x AND date=:y AND status=:z", HabitLogs.class)
                .setParameter("x", habitName).setParameter("y", yesterday)
                .setParameter("z", HabitStatus.DONE).uniqueResult();

        if (yesterdayLog != null) {
            habit.setCurrentStreak(habit.getCurrentStreak() + 1);
        } else {

            habit.setCurrentStreak(1);
        }


        if (habit.getBestStreak() < habit.getCurrentStreak()) {
            habit.setBestStreak(habit.getCurrentStreak());

        }
        session.saveOrUpdate(habit);
        tx.commit();
        session.close();
        System.out.println("**************************");
        System.out.println("Habit marked ✅ as done and streak updated.");
        System.out.println("**************************");

    }


   public int getBestStreak(String habitName,User user){
        return h.getBestStreak(habitName,user);


    }

   public int getCurrentStreak(String habitName,User user){

       return h.getCurrentStreak(habitName,user);
   }



    public List<HabitLogs> getHabitLogs(String title,User user){

        Session session=HibernateUtil.getSessionFactory().openSession();

        List<HabitLogs> logs=session.createQuery("FROM HabitLogs  WHERE habit.title=:title ORDER BY date DESC",HabitLogs.class).setParameter("title",title).list();
        session.close();
        return logs;



    }

    public void createHabit(Habit habit){
          try {
              if (habit != null){

                  h.createHabit(habit);
                  System.out.println("**************************");
                  System.out.println("Habit Created SuccessFully!");
                  System.out.println("**************************");
              } else {
                  throw new HabitException("Habit cannot be null.");
              }
          }catch (HabitException e){
              throw e;
          }catch (org.hibernate.exception.ConstraintViolationException e){

              throw new HabitException("Habit title must be unique for the user.",e);

          }catch(org.hibernate.PropertyValueException e){

              throw new HabitException("Required Filled Missing!"+ e.getPropertyName(),e);

          }catch (Exception e){
              throw new  DatabaseException("Unexpected error occurred while creating habit.");
          }
    }


    public HabitFrequency getFrequencyInput(){

        while(true){

            System.out.println("Choose frequency (DAILY,WEEKLY ,MONTHLY) : ");
            String freq=sc.nextLine().trim().toUpperCase();

            try{
                return HabitFrequency.valueOf(freq);

            }catch (IllegalArgumentException e){

                System.out.println("Invalid Choice. Please enter DAILY,WEEKLY OR MONTHLY");

            }

        }


    }

   public void getHabit(String title,User user){

       Habit habit= h.getHabitByUserAndTitle(user,title);
       if(habit==null) throw new HabitException("Habit not Found!");
       else {
           System.out.println("Your Habit is : ");
           System.out.println("Title : " + habit.getTitle());
           System.out.println("Description : " + habit.getDescription());
           System.out.println("Best Steak Till now : " + habit.getBestStreak());
           System.out.println("Current Running Steak : " + habit.getCurrentStreak());
       }

    }


    public void updateHabit(String title,User user){
      try {
          System.out.println("Enter new Habit name if you want else leave it empty : ");
          String name = sc.nextLine().trim();
          System.out.println("Enter new Habit Description if you want else leave it empty : ");
          String des = sc.nextLine().trim();
          System.out.println("Enter Frequency of your Habit like Daily,Weekly,Monthly");
          HabitFrequency freq = this.getFrequencyInput();

          Habit habit= h.getHabitByUserAndTitle(user,title);
          if(habit==null) throw new HabitException("Cannot Update! Habit not Found!");

          h.updateHabit(title, new Habit(name, des, freq, 0, 0, user), user);
      }catch (Exception e){
          throw new HabitException("Error in updating habit.");

      }

    }

    public void deleteHabit(String name,User user){

       Habit habit= h.getHabitByUserAndTitle(user,name);
       if(habit==null){
           throw new HabitException("Can't delete a habit which doesn't exist.");
       }
       else{
           try {
               h.delete(habit);
               System.out.println("Habit Deleted Successfully!");
           }catch (Exception e){
               throw new DatabaseException("UnExpected Error while deleting habit!");
           }
       }

    }


    public void viewAllHabits(int id){

        List<Habit>list=h.getAll(id);
        if(list.isEmpty()){
            throw new HabitException("No Habits Created yet!");
        }else {
            for (Habit habit : list) {
                System.out.println("***************************");
                System.out.println();
                System.out.println("Habit id : " + habit.getId());
                System.out.println("Habit : " + habit.getTitle());
                System.out.println("Description : " + habit.getDescription());
                System.out.println("Best Steak : " + habit.getBestStreak());
                System.out.println("Current Streak : " + habit.getCurrentStreak());
                System.out.println();
                System.out.println("***************************");
            }
        }
    }

    public void viewLogs(User user,String title){

        Habit habit=h.getHabitByUserAndTitle(user,title);

        if(habit==null)throw new HabitException("Habit doesn't exist!");
        else {
           List<HabitLogs> list= h.getAllLogs(habit);
           if(list.isEmpty())throw new HabitLogException("No Habit Logs yet!");
           for (HabitLogs log : list) {
                System.out.println(log.toString());
                System.out.println("---------------------");
            }

        }

    }


    public  void inputValidator(Habit h){

        if(h==null) throw new UserException("Habit object cannot be null");

        if(h.getTitle()==null || h.getTitle().trim().isEmpty()) throw  new HabitException("Habit title cannot be empty! To Proceed fill this first");

        if(h.getDescription()==null || h.getDescription().trim().isEmpty()) throw  new HabitException("Habit Description cannot be empty! To Proceed fill this first");


    }


    public void  isBlank(String str){
        if ((str==null )|| (str.trim().isEmpty()))throw new HabitException("This field cannot be empty! Please fill this.");

    }

    public void afterLoginSummary(User u){
        try {
            h.afterLoginSummary(u);

        }catch (Exception e){
            System.out.println("Error occurred!");
        }
    }


}
