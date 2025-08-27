package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {
        // Create a SessionFactory using the XML configuration
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        // Open a session
        Session session = sessionFactory.openSession();
        System.out.println("Connected to the database...");

        ConnectDB db = new ConnectDB();
        
        db.saveObject(session);
        


        // Always close resources
        session.close();
        sessionFactory.close();
    }
}
