package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rezensionen")
public class Rezension{
    @Id //TODO: composite key
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int person_id;
    private String produkt_nr;
    private date date; //TODO: date
    private String summary;
    private int bewertung; //TODO: small int
    private String content;
    //TODO: FK


    public Rezension(){}
}