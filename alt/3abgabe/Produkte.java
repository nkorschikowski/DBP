package com.example;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "Produkte")
public class Produkte implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "produkt_nr")
    private String produkt_nr;

    @Column(name = "titel")
    private String titel;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "verkaufsrang")
    private Integer verkaufsrang;

    @Column(name = "bild")
    private String bild;

    @Column(name = "produkttyp")
    private String produkttyp;

    public String getProdukt_nr() {
    return produkt_nr;
    }

    public void setProdukt_nr(String produkt_nr) {
    this.produkt_nr = produkt_nr;
    }

    public String getTitel() {
    return titel;
    }

    public void setTitel(String titel) {
    this.titel = titel;
    }

    public Float getRating() {
    return rating;
    }

    public void setRating(Float rating) {
    this.rating = rating;
    }

    public Integer getVerkaufsrang() {
    return verkaufsrang;
    }

    public void setVerkaufsrang(Integer verkaufsrang) {
    this.verkaufsrang = verkaufsrang;
    }

    public String getBild() {
    return bild;
    }

    public void setBild(String bild) {
    this.bild = bild;
    }

    public String getProdukttyp() {
    return produkttyp;
    }

    public void setProdukttyp(String produkttyp) {
    this.produkttyp = produkttyp;
    }

    public Produkte(){}
    public Produkte(String produkt_nr, 
    String titel, 
    Float rating, 
    Integer verkaufsrang, 
    String bild, 
    String produkttyp){
        this.produkt_nr = produkt_nr;
        this.titel = titel;
        this.rating = rating;
        this.verkaufsrang = verkaufsrang; 
        this.bild = bild; 
        this.produkttyp = produkttyp;
    }

}
