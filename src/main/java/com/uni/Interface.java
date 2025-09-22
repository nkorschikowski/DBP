package com.uni;

import com.uni.entities.*;
import java.util.List;

public interface Interface {
    void init();
    // Hier sollte die Datenbankverbindung für die anderen Methodenaufrufe erstellt,
    // sowie weitere Aktionen, die zur Initialisierung notwendig sind, ausgeführt werden.
    // Alle notwendigen Parameter sollen aus dem übergebenen Property-Objekt entnommen werden.

    void finish();
    // Damit die Mittelschicht alle Ressourcen kontrolliert wieder freigeben kann, 
    // wird diese Methode bei Beendigung der Anwendung aufgerufen. 
    // Hier sollten speziell die Datenbankobjekte wieder freigegeben werden.

    Produkt getProduct(String produkt_nr);
    // Für eine bestimmte Produkt-Id werden mit dieser Methode die Detailinformationen des Produkts ermittelt.

    List<Produkt> getProducts(String pattern);
    // Diese Methode soll eine Liste der in der Datenbank enthaltenen Produkte, 
    // deren Titel mit dem übergebenen Pattern übereinstimmen, zurückliefern. 
    // Beachten Sie, dass im Falle von pattern=null die komplette Liste zurückgeliefert wird. 
    // Das Pattern kann SQL-Wildcards enthalten.
    // Hinweis: der Patternvergleich kann mittels des SQL-Operators like durchgeführt werden.

    Kategorie getCategoryTree(); //TODO: soll ein Tree werden // Parameter = Wurzelknoten?
    // Diese Methode ermittelt den kompletten Kategorienbaum durch Rückgabe des Wurzelknotens. 
    // Jeder Knoten ist dabei vom Typ Category und kann eine Liste von Unterknoten (d.h. Unterkategorien) enthalten.


    List<Produkt> getProductsByCategoryPath(Kategorie categoryPath);
    // Nach Angabe einer Kategorie (definiert durch den Pfad von der Wurzel zu sich selbst) soll die Liste
    // der zugeordneten Produkte ermittelt werden. Die Angabe des Pfades ist notwendig, da der Kategorienname 
    // allein nicht eindeutig ist.

    List<Produkt> getTopProducts(int k);
    // Diese Methode liefert eine Liste aller Produkte zurück, die unter den Top k sind basierend auf dem Rating.

    List<Produkt> getSimilarCheaperProduct(String produkt_nr);
    // Diese Methode liefert für ein Produkt(Id) eine List von Produkten, die ähnlich und billiger sind als das spezifizierte.

    void addNewReview();
    // Die Rahmenapplikation erlaubt sowohl das Ansehen als auch Hinzufügen von Reviews. 
    // Mit Hilfe der Methode wird ein neues Review in der Datenbank gespeichert.

    List<Person> getTrolls(double maxRating);
    // Die Methode soll eine Liste von Nutzern ausgeben, deren Durchschnittsbewertung unter einem spezifizierten Rating ist.

    List<Angebot> getOffers(String produkt_nr);
    // Für das übergegebene Produkt(Id) werden alle verfügbaren Angebote zurückgeliefert.
}
