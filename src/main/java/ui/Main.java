package ui;

import daoImpl.*;
import entity.*;
import exception.DatabaseException;
import exception.HabitException;
import exception.HabitLogException;
import exception.UserException;
import org.hibernate.sql.Update;
import service.UserService;
import serviceImpl.HabitServiceImpl;
import serviceImpl.UserServiceImpl;

import javax.xml.crypto.Data;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc=new Scanner(System.in);
    static HabitServiceImpl habitService=new HabitServiceImpl();
    static UserService userService=new UserServiceImpl();
    static UserDaoImpl newUser=new UserDaoImpl();
    HabitDaoImpl habitDao=new HabitDaoImpl();
    static User currentUser=null;

   public static void main(String[] args){

   Main operator=new Main();
   operator.showMenu();

   }


   public void showMenu(){

       while(true) {
           System.out.println();
           System.out.println("**** 🎉 WELCOME TO DAILY MOMENTUM 😊 ****");
           System.out.println("🚀🚀 Build Habits,Break Limits!");
           System.out.println("🔥Start Your Self Growth Journey Today!");

           System.out.println("--------------------------------------------");
           System.out.println("👉 Please Select an Option : ");

           System.out.println("1️⃣ Register a new account");
           System.out.println("2️⃣ Login to your account");
           System.out.println("3️⃣ Exit.");
           System.out.println("--------------------------------------------");
           System.out.println("Enter Your Choice : ");


           try {
               int choice = Integer.parseInt(sc.nextLine());

               switch(choice){

                   case 1: {
                       if (registerUser()) {
                           afterRegisterMenu();
                           afterLogInMenu();
                       }
                       break;
                   }
                   case 2: {
                       boolean log = logInUser();
                       if (log) {
                           afterLoginSummary();
                           afterLogInMenu();
                       }
                       break;

                   }


                   case 3: {
                       System.out.println("Exiting. Goodbye!");
                       System.exit(0);
                       break;
                   }
                   default:
                       System.out.println("Invalid Choice!");
                       break;

               }
           }catch (NumberFormatException e){
               System.out.println("Please Enter a valid number.");

           }
       }

   }

    public  boolean registerUser(){

           try {
               System.out.println("Enter username : ");
               String name = sc.nextLine();
               System.out.println("Enter email : ");
               String email = sc.nextLine();
               userService.validateEmail(email);
               System.out.println("Enter a strong password  : ");
               String pass = sc.nextLine();
               userService.validatePassword(pass);
               System.out.println("Enter user Type like Student,Professional..etc : ");
               String userType = sc.nextLine();


               User u = new User(name, email, pass, userType);
               userService.inputValidator(u);

               boolean res = userService.register(u);
               if (res) {
                   currentUser = newUser.getUserByEmail(email, pass);
                   return true;
               } else {
                   System.out.println("Registration failed. Try logging in.");
                   return false;

               }

           } catch (UserException e) {
               System.out.println("⚠️" + e.getMessage());
           } catch (Exception e) {
               System.out.println("🚨" + e.getMessage());

           }
           return false;
    }


    public  boolean logInUser() {

            try {
                System.out.println("Enter email : ");
                String email = sc.nextLine();
                userService.checkField(email);
                userService.validateEmail(email);
                System.out.println("Enter password  : ");
                String pass = sc.nextLine();
                userService.checkField(pass);
                userService.validatePassword(pass);

                boolean res = userService.login(email, pass);
                if (res) {
                    currentUser = newUser.getUserByEmail(email, pass);
                    return true;
                }else{
                    System.out.println("Wrong data.Please enter right credentials.");
                    return false;
                }
            } catch (UserException e) {
                System.out.println("⚠️" + e.getMessage());
            } catch (Exception e) {
                System.out.println("🚨" + e.getMessage());

            }
            return false;
   }


    public  void createHabit(){
       boolean habitCreated=false;
       while(!habitCreated) {

           try {

               System.out.println("Enter Habit name : ");
               String title = sc.nextLine();
               System.out.println("Enter your Habit Description : ");
               String des = sc.nextLine();
               System.out.println("Enter Frequency of your Habit like Daily,Weekly,Monthly");
               HabitFrequency freq = habitService.getFrequencyInput();
               Habit h = new Habit(title, des, freq, 0, 0, currentUser);

               habitService.inputValidator(h);
               habitService.createHabit(h);

               habitCreated=true;

           } catch (HabitException e) {
               System.out.println("⚠️" + e.getMessage());
           } catch (DatabaseException e) {
               System.out.println("🚨 " + e.getMessage());

           }
       }

    }


    public  void logOut(){
       currentUser=null;
       System.out.println("You logged out successfully!");
//       showMenu();
   }


    public void  markHabit() {
        try {
            System.out.println("Enter name of habit that you completed : ");
            String title = sc.nextLine();
            habitService.isBlank(title);
            Habit h = habitDao.getHabitByUserAndTitle(currentUser, title);
            if (h == null) {
                System.out.println("**************************");
                System.out.println("This Habit doesn't exist.");
                System.out.println("**************************");
            } else {

                habitService.markHabitDone(title, currentUser);
                System.out.println();
                System.out.println("**************************");
                System.out.println("Now Your Habit data is : ");
                System.out.println("**************************");
                habitService.getHabit(title, currentUser);
            }
        } catch (HabitException e) {
            System.out.println("⚠️" + e.getMessage());
        } catch (Exception e) {
            System.out.println("🚨 Unexpected error: " + e.getMessage());

        }

    }

    public void getHabit(){
       try {
           System.out.println("Enter habit name that you want to view : ");
           String title = sc.nextLine();
           habitService.isBlank(title);

           habitService.getHabit(title, currentUser);
       }catch (HabitException e){
           System.out.println("⚠️"+e.getMessage());
       }catch (Exception e){
           System.out.println("🚨 Unexpected error: "+e.getMessage());

       }

    }


    public void afterRegisterMenu(){
           System.out.println();
           System.out.println("Registration Successful!🎉 Welcome  "+currentUser.getName()+"  Start Off Your Growth Journey!");
           System.out.println("You are now Logged In.");
//           afterLogInMenu();

       }

       public void afterLoginSummary(){
           habitService.afterLoginSummary(currentUser);
       }


       public void afterLogInMenu(){
       boolean logOut=false;
       while(!logOut){

           System.out.println();
           System.out.println("🌟 DailyMomentum Menu 🌟");
           System.out.println("--------------------------------------------");
           System.out.println("1️⃣ Create A New Habit ");
           System.out.println("2️⃣ View MY Habits ");
           System.out.println("3️⃣ Mark Habit As Done ");
           System.out.println("4️⃣ View Habit Details ");
           System.out.println("5️⃣ View Logs for your habit ");
           System.out.println("6️⃣ Update Habit ");
           System.out.println("7️⃣ Delete Habit ");
           System.out.println("8️⃣ Get Current Streak of Your Habit");
           System.out.println("9️⃣ Get Best Streak of Your Habit");
           System.out.println("🔟 Update User Details");
           System.out.println("11.❌ Delete Account");
           System.out.println("12. 👉 Logout ");

           System.out.println("--------------------------------------------");
           System.out.println("Choose an Option  : ");
           try {
               int choice = Integer.parseInt(sc.nextLine());

               switch (choice) {

                   case 1:
                       createHabit();
                       break;
                   case 2:
                       viewAllHabits();
                       break;
                   case 3:
                       markHabit();
                       break;
                   case 4:
                       getHabit();
                       break;
                   case 5:
                       viewLogs();
                       break;
                   case 6:
                       updateHabit();
                       break;
                   case 7:
                       deleteHabit();
                       break;
                   case 8:
                        getCurrentStreak();
                        break;
                   case 9:
                       getBestStreak();
                       break;

                   case 10:
                       updateUser();
                       break;
                   case 11:
                       deleteAccount();
                       break;

                   case 12: logOut=true;
                           logOut();break;

               }
           } catch (NumberFormatException e) {
               System.out.println("Please enter a valid number.");
           }
       }

    }

    public void updateHabit() {

       try {
           System.out.println("Enter the name for your habit which you want to update : ");
           String name = sc.nextLine();
           habitService.isBlank(name);
           habitService.updateHabit(name, currentUser);
       }catch (HabitException e){
           System.out.println("⚠️"+e.getMessage());
       }catch (Exception e){
           System.out.println("🚨 Unexpected error: "+e.getMessage());

       }

    }

    public void deleteHabit(){
       try {
           System.out.println("Enter the name for your habit which you want to Delete : ");
           String name = sc.nextLine();
           habitService.isBlank(name);

           habitService.deleteHabit(name, currentUser);
       }catch (HabitException e){
           System.out.println("⚠️"+e.getMessage());
       }catch (DatabaseException e){
           System.out.println("🚨"+e.getMessage());

       }


    }
    public void viewAllHabits(){
       try {
           int id = currentUser.getId();
           System.out.println(id);
           habitService.viewAllHabits(id);
       }catch (HabitException e){
           System.out.println("⚠️"+e.getMessage());
       }catch (Exception e){
           System.out.println("🚨 Unexpected error: "+e.getMessage());

       }

    }


    public void viewLogs(){
       try {
           System.out.println("Enter the name for your habit which you want to see the logs : ");
           String name = sc.nextLine();

           habitService.isBlank(name);

           habitService.viewLogs(currentUser, name);
       }catch (HabitException e){
           System.out.println("⚠️"+e.getMessage());
       }catch (HabitLogException e){
           System.out.println("🚨 "+e.getMessage());
       }catch (Exception e){
           System.out.println("🚨 Unexpected error: "+e.getMessage());

       }

    }

    public void getCurrentStreak(){

        try {
            System.out.println("Enter habit name : ");
            String title = sc.nextLine();
            habitService.isBlank(title);

            System.out.println("Current Streak of your habit is : "+habitService.getCurrentStreak(title, currentUser));

        }catch (HabitException e){
            System.out.println("⚠️"+e.getMessage());
        }catch (Exception e){
            System.out.println("🚨 Unexpected error: "+e.getMessage());

        }
    }


    public void getBestStreak() {

        try {
            System.out.println("Enter habit name : ");
            String title = sc.nextLine();

            habitService.isBlank(title);

            System.out.println("Best Streak of your habit is : " + habitService.getBestStreak(title, currentUser));

        } catch (HabitException e) {
            System.out.println("⚠️" + e.getMessage());
        } catch (Exception e) {
            System.out.println("🚨 Unexpected error: " + e.getMessage());

        }

    }


    // Update User

    public void updateUser() {

        try {
            System.out.println("Enter your Password : ");
            String pass = sc.nextLine();
            userService.checkField(pass);

            System.out.println("Enter name if you want to update else leave it empty : ");
            String name = sc.nextLine().trim();
            System.out.println("Enter email if you want to update else leave it empty : ");
            String email = sc.nextLine().trim();
            System.out.println("Enter password if you want to update else leave it empty : ");
            String newPass = sc.nextLine().trim();
            System.out.println("Enter User Type like Student,Professional..etc if you want to update else leave it empty : ");
            String userType = sc.nextLine().trim();

            User updates=new User(name,email,newPass,userType);
            System.out.println("User updated Successfully!");
            System.out.println("Updated User Details :  ");
            User updated=userService.UpdateUser(pass,updates);
            if(updated==null){
                System.out.println("Error occurred While updating user.");
            }else {
                System.out.println(updated.toString());
            }

        }catch (UserException e){
            System.out.println("⚠️"+e.getMessage());
        }catch (Exception e){
            System.out.println("🚨 Unexpected error: "+e.getMessage());

        }

    }

    //Deleting User  Account

    public void deleteAccount(){

       try {
           System.out.println("Enter your email: ");
           String email = sc.nextLine();
           userService.checkField(email);

           System.out.println("User Deleted SuccessFully! Deleted User  was : \n" + userService.deleteUser(email).toString());
       } catch (UserException e) {
           System.out.println("⚠️"+e.getMessage());

       }catch (Exception e){
           System.out.println("🚨 Unexpected error: "+e.getMessage());

       }
    }

}
