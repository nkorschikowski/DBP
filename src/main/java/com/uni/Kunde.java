package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kunden")
public class Kunde{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int person_id;
    private int adress_id;
    private int kontonummer;
    //TODO: FK


    public Kunde(){}
}