package com.uni.entities;

import java.math.BigDecimal;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "angebote")
public class Angebot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int angebot_id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "filiale_id", referencedColumnName = "filiale_id", nullable = true)
    private Filiale filiale;

    private BigDecimal preis;
    private String zustand;


    public int get_angebot_id() {
        return angebot_id;
    } 
    public void set_angebot_id(int angebot_id) {
        this.angebot_id = angebot_id;
    } 
    public Produkt get_produkt() {
        return produkt;
    }
    public void set_produkt(Produkt produkt_nr) {
        this.produkt = produkt;
    }
    public Filiale get_filiale() {
        return filiale;
    }
    public void set_filiale(Filiale filiale) {
        this.filiale = filiale;
    }

    public Angebot(int angebot_id, 
    Produkt produkt, 
    Filiale filiale, 
    String zustand
    ){
        this.angebot_id = angebot_id;
        this.produkt = produkt;
        this.filiale = filiale;
        //this.preis = preis;
        this.zustand = zustand;
    }

}
