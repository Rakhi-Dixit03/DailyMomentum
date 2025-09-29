package daoImpl;

import dao.BaseDao;
import dao.UserDao;
import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class UserDaoImpl extends BaseDao<User> implements UserDao{

    public UserDaoImpl(){
        super(User.class);
    }

    @Override
    public User getUserByEmail(String email, String pass) {

        try(Session session=HibernateUtil.getSessionFactory().openSession()){

            return session.createQuery("FROM User WHERE email=:email AND password=:pass",User.class).setParameter("email",email).setParameter("pass",pass).uniqueResult();
        }catch(Exception e){

            throw e;


        }
    }

    @Override
    public User getByEmail(String email) {

        try(Session session=HibernateUtil.getSessionFactory().openSession()){

            return session.createQuery("FROM User WHERE email=:email",User.class).setParameter("email",email).uniqueResult();
        }catch(Exception e){

            throw e;

        }
    }


    public User getByPassword(String pass) {

        try(Session session=HibernateUtil.getSessionFactory().openSession()){

            return session.createQuery("FROM User WHERE password=:pass",User.class).setParameter("pass",pass).uniqueResult();
        }catch(Exception e){

            throw e;

        }
    }


    public User updateUser(String password,User updates){

        Transaction tx=null;
        try(Session session=HibernateUtil.getSessionFactory().openSession()) {
            try {

                tx = session.beginTransaction();
               User existing=session.createQuery("FROM User WHERE password=:x",User.class).setParameter("x",password).uniqueResult();

               if(updates.getName()!=null && !updates.getName().isEmpty()){
                   existing.setName(updates.getName());
               }

                if(updates.getEmail()!=null && !updates.getEmail().isEmpty()){
                    existing.setEmail(updates.getEmail());
                }


                if(updates.getPassword()!=null && !updates.getPassword().isEmpty()){
                    existing.setPassword(updates.getPassword());
                }


                if(updates.getUserType()!=null && !updates.getUserType().isEmpty()){
                    existing.setUserType(updates.getUserType());
                }
                session.update(existing);
                tx.commit();
                return existing;

            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw e;
            }

        }
    }



}
