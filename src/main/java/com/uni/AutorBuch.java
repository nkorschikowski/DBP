package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "autoren_buecher")
public class AutorBuch{
    @Id //TODO: composite key
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private String produkt_nr;
    private int person_id;
    
    //TODO: FK


    public AutorBuch(){}
}