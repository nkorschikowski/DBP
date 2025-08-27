package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "adressen")
public class Adresse{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int adress_id; //TODO: serial
    private String straße;
    private String hausnummer;
    private String zusatz;
    private String plz;
    private String stadt;


    public Adresse(){}
}