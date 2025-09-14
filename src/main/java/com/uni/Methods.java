package com.uni;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;
import com.uni.TableFormatter;
import com.uni.Tablefier;

import com.uni.entities.*;

import com.uni.HibernateUtil;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
        headers.add("verkaufsrank");
        headers.add("bild");
        headers.add("produkttyp");

        try {
            Tablefier.printTable(result, headers);
        } catch (Exception e) {
            System.out.println("Sheeeeeeeeeeeeeeeeeeeeeeeeeeeeeesh  da lief was schieeeeeeeeeeeeeeeeeeeeeef");
        }

        return result;
    };

    // public Kategorie getCategoryTree(){

    // }; //TODO: soll ein Tree werden // Parameter = Wurzelknoten?

    // public List<Produkt> getProductsByCategoryPath(String categoryPath){

    // };

    // public List<Produkt> getTopProducts(int k){

    // };

    // public List<Produkt> getSimilarCheaperProduct(String produkt_nr){

    // };

    // public void addNewReview(Rezension review){

    // };

    // public List<Person> getTrolls(double minRating){

    // };

    // public List<Angebot> getOffers(String produkt_nr){

    // };
}
