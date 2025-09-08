package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dvds")
public class DVD{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private String produkt_nr;
    private String format;
    //private time laufzeit; //TODO: time
    private int region_code; //TODO: small int?
    //TODO: FK


    public DVD(){}
}