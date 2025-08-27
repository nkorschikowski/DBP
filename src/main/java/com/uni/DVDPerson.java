package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dvd_personen")
public class DVDPerson{
    @Id //TODO: composite key
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private String produkt_nr;
    private int person_id;
    private String rolle;
    //TODO: FK


    public DVDPerson(){}
}