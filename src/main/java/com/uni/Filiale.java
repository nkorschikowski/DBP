package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "filialen")
public class Filiale{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int filiale_id; //TODO: serial
    private String name;
    private int adress_id;
    //TODO: FK


    public Filiale(){}
}