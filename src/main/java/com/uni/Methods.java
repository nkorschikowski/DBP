package com.uni;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
// import java.util.Locale.Category;
// import com.uni.Tablefier;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
// import org.hibernate.boot.MetadataSources;
// import org.hibernate.boot.registry.StandardServiceRegistry;
// import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

// import com.uni.HibernateUtil;

import org.hibernate.query.Query;

import com.uni.entities.Angebot;
import com.uni.entities.Kategorie;
import com.uni.entities.Person;
import com.uni.entities.Produkt;
import com.uni.entities.Rezension;

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
        Query<Produkt> q = session.createQuery(hql,Produkt.class);
        q.setParameter("produkt_nr", produkt_nr);

        Produkt result = q.uniqueResult();
        List<Produkt> results = new ArrayList<>(); // Tablefier.printTable braucht es als Liste. // TODO: printTable für einzelne Ergebnisse implementieren?
        results.add(result);

        List<String> headers = new ArrayList<>();
        headers.add("produkt_nr");
        headers.add("titel");
        headers.add("rating");
        headers.add("verkaufsrang");
        headers.add("bild");
        headers.add("produkttyp");

        try {
            Tablefier.printTable(results, headers);
        } catch (Exception e) {
            System.out.println("Tablefier will nicht mehr!");
        }
        session.close();
        return result; //TODO: was soll man mit return machen?
    };

    public List<Produkt> getProducts(String pattern){
        Session session = sessionFactory.openSession();
        String hql = "from Produkt where titel LIKE :pattern";
        Query<Produkt> q = session.createQuery(hql,Produkt.class);
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
        
        Session session = sessionFactory.openSession();
        String hql = "from Kategorie where oberkategorie_id is Null";
        Query<Kategorie> q = session.createQuery(hql, Kategorie.class);
        Kategorie root = new Kategorie();
        List<Kategorie> result = q.getResultList();
        root.set_Unterkategorien(result);
        for (Kategorie x : root.get_Unterkategorien()){
            set_knoten_unterkategorien(x);
        }
        session.close();

        return root;
        
    }; //TODO: soll ein Tree werden // Parameter = Wurzelknoten?

    public void set_knoten_unterkategorien(Kategorie oberkategorie
    ){
        Session session = sessionFactory.openSession();

        String qkat = "from Kategorie where oberkategorie_id.kategorie_id = :id";
        Query<Kategorie> qk = session.createQuery(qkat);
        qk.setParameter("id",oberkategorie.get_kategorie_id());
        List<Kategorie> unterkategorien = qk.getResultList();
        oberkategorie.set_Unterkategorien(unterkategorien);
        
        try {
            for (Kategorie x : oberkategorie.get_Unterkategorien()) {

                if(unterkategorien != null && !unterkategorien.isEmpty()){
                    set_knoten_unterkategorien(x);
                }
            }
        } catch(Exception e){
            System.err.println("Hupsala" + e.getMessage());
        } finally{
            session.close();
        }   
    }

        public List<Produkt> getProductsByCategoryPath(
    Kategorie knoten, String input
    ){
        //Kategorie knoten = getCategoryTree();
        String regex = "[\\\\/,\\.#_-]";
        String[] knoten_pfad = input.split(regex);

        
        for (String knot : knoten_pfad) {
            if(!knot.isEmpty()){
                List<Kategorie> unterkategorien = knoten.get_Unterkategorien();
                int kat_postion = 0;
                for (Kategorie x : unterkategorien){
                    if (x.get_name().equals(knot)){
                        knoten = x;
                        System.out.println("visiting... " + knoten.get_name());
                        break;
                    }
                }
            }
        }
        System.out.println("Zielkategorie erreicht... ");
        System.out.println("Produkte für Zielkatergorie suchen...");

//knoten trversieren und kategorieids speichern 
        List<Produkt> produkte = getAlleProdukte(knoten);
        Set<Produkt> produkteSet = new HashSet<>(produkte);
        produkte = new ArrayList<>(produkteSet);
        
        List<String> headers = new ArrayList<>();
        headers.add("produkt_nr");
        headers.add("titel");
        headers.add("rating");
        headers.add("verkaufsrang");
        headers.add("bild");
        headers.add("produkttyp");

        try {
            Tablefier.printTable(produkte, headers);
        } catch (Exception e) {
            System.out.println("Tablefier will nicht mehr!");
        }


        return produkte;
    };


public List<Produkt> getAlleProdukte(Kategorie kat){
    List<Produkt> summeUnterkategorie = new ArrayList<>();

    // add products of current category
    summeUnterkategorie.addAll(getProductByCategory(kat));

    // recurse into children
    for (Kategorie ukat : kat.get_Unterkategorien()) {
        summeUnterkategorie.addAll(getAlleProdukte(ukat));
    }

    return summeUnterkategorie;
    }
        
    public List<Produkt> getProductByCategory(Kategorie kat){
    Session session = sessionFactory.openSession();
            Kategorie Kat = session.get(Kategorie.class,kat.get_kategorie_id());
            String hql = "FROM Produkt p WHERE p IN (SELECT produkt_nr FROM ProduktKategorie WHERE kategorie_id.kategorie_id = :kat_id)";
            Query<Produkt> q = session.createQuery(hql,Produkt.class);
            q.setParameter("kat_id", kat.get_kategorie_id());

            List<Produkt> produkte = q.getResultList();
            session.close();
            return produkte;
    }

    public List<Produkt> getTopProductsRANKING(int k){
        Session session = sessionFactory.openSession();
        String hql = "from Produkt where verkaufsrang < :k AND verkaufsrang != (-1) ORDER BY verkaufsrang ASC"; // TODO: ist das sauber? ^^
        Query<Produkt> q = session.createQuery(hql,Produkt.class);
        q.setParameter("k",k);
        
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

    public List<Produkt> getTopProductsRATING(int k){
        Session session = sessionFactory.openSession();
        String hql = """
            
            FROM Produkt p JOIN Rezension r ON p.produkt_nr = r.produkt_nr.produkt_nr
            GROUP BY p.produkt_nr 
            ORDER BY p.rating DESC, count(r.person_id) DESC, p.titel ASC LIMIT :k  
                """;
        Query<Produkt> q = session.createQuery(hql,Produkt.class);
        q.setParameter("k",k);
        
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

    public List<Produkt> getSimilarCheaperProduct(String produkt_nr){
        Session session = sessionFactory.openSession();

        // HQL akzeptiert kein SELECT von einer Subquery und unterstützt wohl auch kein UNION
        // DAS IST DIE ALTE VARIANTE DIE FUNKTIONIERT ABER UNCOOL IST
        // // String hql = """
        // // FROM Produkt p
        // // WHERE p.produkt_nr IN (
        // //     SELECT a.produkt_nr.produkt_nr
        // //     FROM Angebot a
        // //     WHERE 
        // //         (a.produkt_nr.produkt_nr IN (
        // //             SELECT ap.produkt_nr2.produkt_nr FROM AehnlicheProdukte ap WHERE ap.produkt_nr1.produkt_nr = :produkt_nrA
        // //         ) 
        // //         OR a.produkt_nr.produkt_nr IN (
        // //             SELECT ap.produkt_nr1.produkt_nr FROM AehnlicheProdukte ap WHERE ap.produkt_nr2.produkt_nr = :produkt_nrB
        // //         ))
        // //         AND a.preis < (
        // //             SELECT MAX(a2.preis)
        // //             FROM Angebot a2
        // //             WHERE a2.produkt_nr.produkt_nr = :produkt_nrC
        // //         )
        // // )
        // // """;

        Produkt unserProdukt = session.get(Produkt.class, produkt_nr);
        String hql = """
        FROM Produkt p
        WHERE p IN (
            SELECT a.produkt_nr
            FROM Angebot a
            WHERE 
                (a.produkt_nr IN (
                    SELECT ap.produkt_nr2 FROM AehnlicheProdukte ap WHERE ap.produkt_nr1 = :produkt_nrA
                ) 
                OR a.produkt_nr IN (
                    SELECT ap.produkt_nr1 FROM AehnlicheProdukte ap WHERE ap.produkt_nr2 = :produkt_nrB
                ))
                AND a.preis < (
                    SELECT MAX(a2.preis)
                    FROM Angebot a2
                    WHERE a2.produkt_nr = :produkt_nrC
                )
        )
        """;

        Query<Produkt> q = session.createQuery(hql,Produkt.class);
        q.setParameter("produkt_nrA", unserProdukt);
        q.setParameter("produkt_nrB", unserProdukt);
        q.setParameter("produkt_nrC", unserProdukt);
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

        return result;
    };

    public void addNewReview(){
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        Scanner sc = new Scanner(System.in);
        Rezension rezension = new Rezension();
        System.out.println("Wie lautet der (genaue) Name der Person?");
        // rezension.set_Person_id(getPersonByName(sc.nextLine())); // PROD
        rezension.set_Person_id(getPersonByName("Va")); // TESTING
        System.out.println("Wie lautet die Produktnummer?");
        // rezension.set_Produkt_nr(getProduct(sc.nextLine())); // PROD
        rezension.set_Produkt_nr(getProduct("B0000668PG")); // TESTING
        System.out.println("Wie lautet die Kurzbeschreibung?");
        rezension.set_Summary(sc.nextLine());
        System.out.println("Welche Wertung von 1 bis 5?");
        rezension.set_Bewertung(Integer.parseInt(sc.nextLine()));
        System.out.println("Inhalt der Rezension?");
        rezension.set_Content(sc.nextLine());
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
        Query<Person> q = session.createQuery(hql,Person.class);
        q.setParameter("name",name);
        Person person = q.uniqueResult();
        session.close();

        return person;
    }
}
