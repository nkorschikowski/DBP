package com.uni;

import java.util.List;
import java.util.Locale.Category;

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
        System.out.println("Du hast das Produkt " + result.get_produkt_nr() + " gefunden!");
        return result; //was soll man mit return machen?
    };

    // public List<Produkt> getProducts(String pattern){

    // };

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
