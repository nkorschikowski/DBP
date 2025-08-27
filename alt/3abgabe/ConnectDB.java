package com.example;

import java.sql.Connection;
import java.sql.DriverManager;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class ConnectDB {

    public Connection connect_to_db(String dbname,
        String user,
        String pass
    ){
    Connection conn = null;
    try{
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5435/"+dbname, user, user);
        if(conn!=null){
            System.err.println("Connected to DB...");
        }else{
            System.err.println("Connection failed");
        }
    }catch(Exception e){
        System.err.println(e);
    }
    return conn;
    }

    public void createTable(Session session, 
        String tableName
    ){
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();

            String query = "CREATE TABLE " + tableName + " ("
                        + "id SERIAL PRIMARY KEY, "
                        + "name VARCHAR(200), "
                        + "parameter VARCHAR(200))";

            session.createNativeQuery(query).executeUpdate();

            transaction.commit();
            System.out.println("Table " + tableName + " created...");
        }catch(Exception e){
            System.err.println(e);
        }

    }

    public void deleteTable(Session session, 
        String tableName
    ){
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            String query = "drop table " + tableName + ";";
            session.createNativeQuery(query).executeUpdate();
            transaction.commit();
            System.out.println("Table " + tableName + " deleted...");
        }catch(Exception e){
            System.err.println(e);
        }

    }

    public void saveObject(
        Session session
    ){
        
        Produkte produkt = new Produkte("testid",
        "testprodukt",
        0.5f,
        42,
        "testbildurl",
        "Book");

        session.beginTransaction();

        session.persist(produkt);

        session.getTransaction().commit();
    }

    
}
