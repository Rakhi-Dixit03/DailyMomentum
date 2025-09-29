package dao;

import entity.User;

public interface UserDao {

    User getUserByEmail(String email,String pass);
    User updateUser(String password,User updates);
    public User getByEmail(String email);
    public User getByPassword(String pass);
}
