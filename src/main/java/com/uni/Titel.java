package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "titel")
public class Titel{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int titel_id; //TODO: serial
    private String name;
    private String produkt_nr; 
    //TODO: FK


    public Titel(){}
}