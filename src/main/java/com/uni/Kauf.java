package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kauf")
public class Kauf{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int kauf_id;
    private int filiale_id;
    private int person_id;
    //private date kaufdatum; //TODO: Date
    //TODO: FK


    public Kauf(){}
}