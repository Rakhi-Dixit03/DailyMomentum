package entity;



import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.persistence.*;
import java.util.List;

//import javax.persistence.Entity;
//import javax.persistence.*;
//@Table(name="Users")

@Entity
public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

@Column(nullable = false)
    private String name;

@Column(unique = true ,nullable = false)
    private String email;

@Column(unique = true ,nullable=false)
private String password;

@Column(nullable = false)
    private String userType;

@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
private List<Habit> habits;

public User(){}


    public User(String name, String email,String password, String userType) {
        this.name = name;
        this.email = email;
        this.password=password;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "{\n" +
                 "  id = " + id +
                "\n name = " + name +
                "\n email = '" + email +
                "\n userType = '" + userType +
                "\n }";
    }
}
