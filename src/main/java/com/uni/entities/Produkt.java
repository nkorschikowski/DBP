package com.uni.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Produkte")
public class Produkt {
    @Id
    private String produkt_nr;
    private String titel;
    private double rating;
    private int verkaufsrang;
    private String bild;
    private String produkttyp;
    
    public String get_produkt_nr(){
        return produkt_nr;
    }
    private void set_produkt_nr(String produkt_nr){
        this.produkt_nr = produkt_nr;
    }
    private String get_titel(){
        return titel;
    }
    private void set_titel(String titel){
        this.titel = titel;
    }
    private double get_rating(){
        return rating;
    }
    private void set_rating(double rating){
        this.rating = rating;
    }
    private int get_verkaufsrang(){
        return verkaufsrang;
    }
    private void set_verkaufsrang(int verkaufsrang){
        this.verkaufsrang = verkaufsrang;
    }
    private String get_bild(){
        return bild;
    }
    private void set_bild(String bild){
        this.bild = bild;
    }
    private String get_produkttyp(){
        return produkttyp;
    }
    private void set_produkttyp(String produkttyp){
        this.produkttyp = produkttyp;
    }

    public Produkt(){};

    public Produkt(String produkt_nr, 
    String titel,
    double rating,
    int verkaufsrang,
    String bild,
    String produkttyp
    ){
        this.produkt_nr = produkt_nr;
        this.titel = titel;
        this.rating = rating;
        this.verkaufsrang = verkaufsrang;
        this.bild = bild;
        this.produkttyp = produkttyp;
    }

}