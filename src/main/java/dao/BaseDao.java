package dao;

import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public abstract class BaseDao<T> {


    Class<T> entityClass;

    public BaseDao( Class<T> entityClass){

        this.entityClass=entityClass;

    }

  public void save(T entry) {

      Transaction tx = null;

      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
          try {
              tx = session.beginTransaction();
              session.merge(entry);
              tx.commit();
//              System.out.println("Operation Successful");


          } catch (Exception e) {

              if (tx != null) {
                  tx.rollback();
              }
              throw e;

          }
      }
  }

  public T getById(int id){

        try(Session session=HibernateUtil.getSessionFactory().openSession()){
        return  session.get(entityClass,id);

    }

    }


    public void delete(T entry) {

      Transaction tx = null;
      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
          try {
              tx = session.beginTransaction();

              session.delete(entry);
              tx.commit();

          } catch (Exception e) {
              if (tx != null) {
                  tx.rollback();
              }
              throw  e;
          }

      }
  }


  public void deleteById(int id){

      Transaction tx = null;
      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
          try {
              tx = session.beginTransaction();
              T obj=session.get(entityClass,id);
               session.delete(obj);

              tx.commit();

          } catch (Exception e) {
              if (tx != null) {
                  tx.rollback();
              }
              throw e;
          }

      }

    }


public List<T> getAll(int id){

        try(Session session=HibernateUtil.getSessionFactory().openSession()){

            return session.createQuery("From "+entityClass.getSimpleName()+" h Where h.user.id=:x",entityClass).setParameter("x",id).list();

        }



}



}
