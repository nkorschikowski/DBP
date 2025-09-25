package com.uni.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Produkte")
public class Produkt {
    @Id
    private String produkt_nr;
    private String titel;
    private double rating;
    private int verkaufsrang;
    @Column(columnDefinition = "TEXT", nullable = true)
    private String bild;
    private String produkttyp;
    
    public String get_produkt_nr(){
        return produkt_nr;
    }
    public void set_produkt_nr(String produkt_nr){
        this.produkt_nr = produkt_nr;
    }
    public String get_titel(){
        return titel;
    }
    public void set_titel(String titel){
        this.titel = titel;
    }
    public double get_rating(){
        return rating;
    }
    public void set_rating(double rating){
        this.rating = rating;
    }
    public int get_verkaufsrang(){
        return verkaufsrang;
    }
    public void set_verkaufsrang(int verkaufsrang){
        this.verkaufsrang = verkaufsrang;
    }
    public String get_bild(){
        return bild;
    }
    public void set_bild(String bild){
        this.bild = bild;
    }
    public String get_produkttyp(){
        return produkttyp;
    }
    public void set_produkttyp(String produkttyp){
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produkt)) return false;
        Produkt produkt = (Produkt) o;
        return Double.compare(produkt.rating, rating) == 0 &&
               verkaufsrang == produkt.verkaufsrang &&
               Objects.equals(produkt_nr, produkt.produkt_nr) &&
               Objects.equals(titel, produkt.titel) &&
               Objects.equals(bild, produkt.bild) &&
               Objects.equals(produkttyp, produkt.produkttyp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produkt_nr, titel, rating, verkaufsrang, bild, produkttyp);
    }

}
