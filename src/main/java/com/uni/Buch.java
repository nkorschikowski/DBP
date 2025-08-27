package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "buecher")
public class Buch{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private String produkt_nr;
    private int seitenzahl;
    private date erscheinungsdatum; //TODO: Datum
    private String isbn;
    private String verlag;
    //TODO: FK


    public Buch(){}
}