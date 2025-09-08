package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "produkte")
public class Produkt {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private String produkt_nr;
    private String titel;
    private double rating;
    private int verkaufsrang;
    private String bild;
    private String produkttyp;

    public Produkt(){}
}