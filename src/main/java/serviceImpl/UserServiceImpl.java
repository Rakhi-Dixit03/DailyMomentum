package serviceImpl;

import daoImpl.UserDaoImpl;
import entity.User;
import exception.HabitException;
import exception.UserException;
import service.UserService;

import java.util.regex.Pattern;

import static org.hibernate.internal.util.StringHelper.isBlank;

public class UserServiceImpl implements UserService {

    UserDaoImpl newUser=new UserDaoImpl();


    public boolean register(User user){

        try{
        User u=newUser.getByEmail(user.getEmail());
        User u1=newUser.getByPassword(user.getPassword());

        if(u!=null){

            throw new UserException("Email already registered! Email must be unique.");

        }else if(u1!=null){

            throw new UserException("Password must be unique.");

        }else{

            newUser.save(user);
            System.out.println();
            System.out.println("User registered SuccessFully! Check your Details : ");
            System.out.println();
            System.out.println(user.toString());
            System.out.println("If any of your details are incorrect,Please Update using [Update Option] in menu. ");


        }
        }catch (org.hibernate.exception.ConstraintViolationException e){

            throw new UserException("Email and Password must be unique for the user.User with this data already exists");

        }catch(org.hibernate.PropertyValueException e) {

            throw new UserException("Required Filled Missing!" + e.getPropertyName(), e);
        }catch (Exception e){

            throw e;
//            throw new UserException("Unexpected error occurred while Registering User.",e);
        }
        return true;
    }

    public boolean login(String email,String pass){
        try {
            User user = newUser.getUserByEmail(email, pass);
            if (user != null) {
                System.out.println();
                System.out.println("Log in Successful!");
                return true;

            } else {
                throw new UserException("User not Found! Use Correct Login Credentials OR You may not have an Account Create one. ");
            }
        }catch(org.hibernate.PropertyValueException e) {

            throw new UserException("Required Filled Missing!" + e.getPropertyName(), e);
        }catch (Exception e){
            throw e;
//          throw new UserException("Unexpected error occurred while Logging User.",e);
        }

    }

    public User UpdateUser(String pass,User user){

        try {
            User u = newUser.getByPassword(pass);
            if (u == null) throw new UserException("Cannot find user! Please enter correct password.");

            return newUser.updateUser(pass,user);

        }catch (Exception e){

            throw new UserException("Error occurred while Updating User!");
        }

    }


    public User deleteUser(String email){
        try {
            User u = newUser.getByEmail(email);

            if (u == null) throw new UserException("User doesn't exist!");
            newUser.delete(u);
            return u;

        }catch (Exception e){
            throw new UserException("Error occurred during deletion!");
        }
    }



    public  void inputValidator(User u){

        if(u==null) throw new UserException("User object cannot be null");

        if(isBlank(u.getName())) throw  new UserException("Name cannot be empty!");

        if(isBlank(u.getEmail())) throw  new UserException("Email cannot be empty!");

        if(isBlank(u.getPassword())) throw new UserException("Password cannot be empty");

        if(isBlank(u.getUserType())) throw new UserException("UserType cannot be empty");


    }

    public boolean isBlank(String str){
        return (str==null )|| (str.trim().isEmpty());

    }

    public void checkField(String str){
        if((str==null )|| (str.trim().isEmpty())) throw new UserException("You cannot leave this field empty.Please fill it.");

    }


   public boolean validateEmail(String email){

       final String regEx="^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
               + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

       Pattern p=Pattern.compile(regEx);

       if(email!=null && p.matcher(email).matches()){
           return true;
       }else{
           throw new UserException("Wrong email format! Please use correct email Address!");
       }

    }

    public boolean validatePassword(String password) {

        final String passRegEx = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(passRegEx);


        if(password!= null && p.matcher(password).matches()){
            return true;
        }else{
            throw new UserException("Your password must contain at least one uppercase,lowercase letter, digit, special char,length must be 8 or more and should not contain a space.");

        }
    }

    //^: Start of the string.
    //(?=.*[0-9]): Asserts that at least one digit (0-9) is present.
    //(?=.*[a-z]): Asserts that at least one lowercase letter (a-z) is present.
    //(?=.*[A-Z]): Asserts that at least one uppercase letter (A-Z) is present.
    //(?=.*[@#$%^&+=!]): Asserts that at least one special character from the defined set is present.
    //(?=\\S+$): Asserts that there are no whitespace characters.
    //.{8,20}: Ensures the password length is between 8 and 20 characters.
    //$: End of the string.


}
