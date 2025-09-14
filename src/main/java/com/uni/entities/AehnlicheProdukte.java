package com.uni.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "aehnliche_produkte")
public class AehnlicheProdukte {
    @Id //TODO: composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt;
    @Id
    private String produkt_nr2;

    public Produkt get_produkt(){
        return produkt;
    }
    public void set_produkt(Produkt produkt_nr1){
        this.produkt = produkt;
    }
    public String get_produkt_nr2(){
        return produkt_nr2;
    }
    public void set_produkt_nr2(String produkt_nr2){
        this.produkt_nr2 = produkt_nr2;
    }

    public AehnlicheProdukte(Produkt produkt_nr1,
    String produkt_nr2){
        this.produkt = produkt;
        this.produkt_nr2 = produkt_nr2; 
    }
}
