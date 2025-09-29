package service;

import entity.User;

public interface UserService {

    boolean register(User user);
    boolean login(String email,String pass);
    User UpdateUser(String pass,User user);
    void inputValidator(User u);
    void checkField(String str);
    public User deleteUser(String email);
    boolean validateEmail(String email);
    boolean validatePassword(String pass);


}
