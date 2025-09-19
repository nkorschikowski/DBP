package com.uni;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;
import com.uni.Tablefier;

import java.util.Scanner;

import com.uni.entities.*;

import com.uni.HibernateUtil;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Methods implements Interface{

    private SessionFactory sessionFactory;
    
    public void init(){
        System.out.println("Die Sitzung wird aufgebaut!"); // LOG
        sessionFactory = HibernateUtil.getSessionFactory();
        //das laden der Properties ist in HibernateUtil geregelt
        System.out.println("Sitzungsaufbau abgeschlossen!"); // LOG
    };

    public void finish(){
        System.out.println("Sitzund wird beendet"); // LOG
        if(sessionFactory!=null){
            sessionFactory.close();
        }
        System.out.println("Sitzung wude beendet");// LOG
    };

    public Produkt getProduct(String produkt_nr){
        Session session = sessionFactory.openSession();
        String hql = "from Produkt p where p.produkt_nr = :produkt_nr"; 
        Query<Produkt> q = session.createQuery(hql);
        q.setParameter("produkt_nr", produkt_nr);

        Produkt result = q.uniqueResult();
        System.out.println(
            "produkt_nr\ttitel\trating\tverkaufsrank\tbild\tprodukttyp \n" +
            result.get_produkt_nr() + "\t"+ 
            result.get_titel() + "\t" + 
            result.get_rating() + "\t" +
            result.get_verkaufsrang() + "\t" +
            result.get_bild() + "\t" +
            result.get_produkttyp()
            );

        session.close();
        return result; //TODO: was soll man mit return machen?
    };

    public List<Produkt> getProducts(String pattern){
        Session session = sessionFactory.openSession();
        String hql = "from Produkt where titel LIKE :pattern";
        Query<Produkt> q = session.createQuery(hql);
        q.setParameter("pattern",pattern); // lt. Aufgabenstellung kann pattern Wildcards enthalten, also nicht Sache der Query
        
        List<Produkt> result = q.getResultList();

        List<String> headers = new ArrayList<>();
        headers.add("produkt_nr");
        headers.add("titel");
        headers.add("rating");
        headers.add("verkaufsrang");
        headers.add("bild");
        headers.add("produkttyp");

        try {
            Tablefier.printTable(result, headers);
        } catch (Exception e) {
            System.out.println("Tablefier will nicht mehr!");
        }
        session.close();

        return result;
    };

    public Kategorie getCategoryTree(){
        
        Kategorie root = new Kategorie();
        return root;
    }; //TODO: soll ein Tree werden // Parameter = Wurzelknoten?

    public List<Produkt> getProductsByCategoryPath(String categoryPath){

        List<Produkt> result = new ArrayList<>();
        return result;
    };

    public List<Produkt> getTopProducts(int k){
        Session session = sessionFactory.openSession();
        String hql = "from Produkt where verkaufsrang < :k AND verkaufsrang != (-1)"; // TODO: ist das sauber? ^^
        Query<Produkt> q = session.createQuery(hql);
        q.setParameter("k",k);
        
        List<Produkt> result = q.getResultList();

        List<String> headers = new ArrayList<>();
        headers.add("produkt_nr");
        // headers.add("titel");
        // headers.add("rating");
        headers.add("verkaufsrang");
        // headers.add("bild");
        // headers.add("produkttyp");

        try {
            Tablefier.printTable(result, headers);
        } catch (Exception e) {
            System.out.println("Tablefier will nicht mehr!");
        }
        session.close();

        return result;
    };

    public List<Produkt> getSimilarCheaperProduct(String produkt_nr){

        List<Produkt> result = new ArrayList<>();
        return result;
    };

    public void addNewReview(){
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        Scanner sc = new Scanner(System.in);
        String input;
        Rezension rezension = new Rezension();
        System.out.println("Wie lautet der (genaue) Name der Person?");
        // input = sc.nextLine();
        rezension.set_Person_id(getPersonByName("Va")); // TODO: dynamic
        System.out.println("Wie lautet die Produktnummer?");
        // input = sc.nextLine();
        rezension.set_Produkt_nr(getProduct("B0000668PG")); // TODO: dynamic
        System.out.println("Wie lautet die Kurzbeschreibung?");
        input = sc.nextLine();
        rezension.set_Summary(input);
        System.out.println("Welche Wertung von 1 bis 5?");
        input = sc.nextLine();
        rezension.set_Bewertung(Integer.parseInt(input));
        System.out.println("Inhalt der Rezension?");
        input = sc.nextLine();
        rezension.set_Content(input);
        sc.close();
    
        try {
            transaction = session.beginTransaction();
            session.persist(rezension);
            transaction.commit();
            System.out.println("Objekt wurde gespeichert");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    };

    public List<Person> getTrolls(double maxRating){
        Session session = sessionFactory.openSession();
        String hql = """
        FROM Person p
        WHERE p IN 
            (SELECT r.person_id 
            FROM Rezension r
            GROUP BY r.person_id 
                HAVING AVG(r.bewertung) <=  :maxRating)
        """;

        Query<Person> q = session.createQuery(hql,Person.class);
        q.setParameter("maxRating", maxRating);

        List<Person> result = q.getResultList();

        List<String> headers = new ArrayList<>();
        headers.add("Person_id");
        headers.add("Name");

        try {
            Tablefier.printTable(result, headers);
        } catch (Exception e) {
            System.out.println("Tablefier will nicht mehr!");
        }
        session.close();

        return result;
    };

    public List<Angebot> getOffers(String produkt_nr){
         Session session = sessionFactory.openSession();
        String hql = "FROM Angebot WHERE produkt_nr.produkt_nr = :produkt_nr";

        Query<Angebot> q = session.createQuery(hql,Angebot.class);
        q.setParameter("produkt_nr", produkt_nr);

        List<Angebot> result = q.getResultList();

        List<String> headers = new ArrayList<>();
        headers.add("angebot_id");
        headers.add("produkt_nr");
        headers.add("filiale_id");
        headers.add("preis");
        headers.add("zustand");

        try {
            Tablefier.printTable(result, headers);
        } catch (Exception e) {
            System.out.println("Tablefier will nicht mehr!");
        }
        session.close();

        return result;
    };


    // Helper functions
    public Person getPersonByName(String name){
        Session session = sessionFactory.openSession();
        String hql = "FROM Person WHERE name = :name";         
        Query<Person> q = session.createQuery(hql);
        q.setParameter("name",name);
        Person person = q.uniqueResult();

        return person;
    }
}
