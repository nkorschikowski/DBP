package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kategorien")
public class Kategorie{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int kategorie_id; //TODO: serial
    private int oberkategorie_id; 
    //TODO: self refferential
    //TODO: FK


    public Kategorie(){}
}