package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "angebote")
public class Angebot{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int angebot_id; //TODO: serial
    private String produkt_nr;
    private int filiale_id;
    private money preis; //TODO: money
    private String zustand;
    //TODO: FK


    public Angebot(){}
}