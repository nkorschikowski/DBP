package com.uni;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            //Lade properties
            Properties props = new Properties();
            // nutzt classpath statt hardcoded filepath
            props.load(HibernateUtil.class.getClassLoader().getResourceAsStream("db.properties"));

            // überschreibt (nur diese) Properties aus der hibernate.cfg
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", props.getProperty("driver"));
            configuration.setProperty("hibernate.connection.url", props.getProperty("url"));
            configuration.setProperty("hibernate.connection.username", props.getProperty("name"));
            configuration.setProperty("hibernate.connection.password", props.getProperty("password"));

            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}