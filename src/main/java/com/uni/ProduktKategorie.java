package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "produkt_kategorie")
public class ProduktKategorie {
    @Id //TODO: composite key
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private String produkt_nr;
    private int kategorie_id; 
    //TODO: FK
    //TODO: self refferential

    public ProduktKategorie(){}
}