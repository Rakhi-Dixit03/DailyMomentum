package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;


    static {

        try{

            //Load configuration
            Configuration config=new Configuration().configure("hibernate.cfg.xml");
            //Register annotated classes
            config.addAnnotatedClass(entity.User.class);
            config.addAnnotatedClass(entity.Habit.class);
            config.addAnnotatedClass(entity.HabitLogs.class);

            //Create ServiceRegistry
            ServiceRegistry  serviceRegistry=new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

            //Build SessionFactory
            sessionFactory=config.buildSessionFactory(serviceRegistry);

            System.out.println("Mapped entities: " +
                    sessionFactory.getMetamodel().getEntities());


        }catch (Throwable ex){
            System.out.println("SessionFactory creation failed."+ex);
            throw new ExceptionInInitializerError(ex);

        }

    }

    public static SessionFactory getSessionFactory(){

        return  sessionFactory;
    }
}
