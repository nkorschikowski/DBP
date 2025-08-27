package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "musikcds")
public class MusikCD{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private String produkt_nr;
    private String label;
    private date erscheinungsdatum; //TODO: date
    //TODO: FK


    public MusikCD(){}
}