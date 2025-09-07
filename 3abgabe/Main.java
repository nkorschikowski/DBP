package com.example;

public class Main {
    public static void main(String[] args) {
        // Create a SessionFactory using the XML configuration
        //SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        // Open a session
        //Session session = sessionFactory.openSession();
        //System.out.println("Connected to the database...");

        ConnectDB db = new ConnectDB();
        
        db.saveObject(
        "testid3",
        "testprodukt3",
        0.5f,
        42,
        "testbildurl",
        "Book");
        


        // Always close resources
        //session.close();
        //sessionFactory.close();
    }
}
