package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kauf_produkt")
public class KaufProdukt{
    @Id //TODO: composite key
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int kauf_id;
    private int angebot_id;
    private int anzahl;
    //private money einzelpreis;
    //TODO: FK


    public KaufProdukt(){}
}