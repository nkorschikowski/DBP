package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aehnliche_produkte")
public class AehnlicheProdukte {
    @Id //TODO: composite key
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private String produkt_nr1;
    private String produkt_nr2;
    //TODO: FK

    public AehnlicheProdukte(){}
}