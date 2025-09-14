package com.uni.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "adressen")
public class Adresse{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private int adress_id; 
    private String straße;
    private String hausnummer;
    private String zusatz;
    private String plz;
    private String stadt;

    public int get_adress_id(){
        return adress_id;
    }
    public void set_adress_id(int adress_id){
        this.adress_id = adress_id;
    }
    public String get_straße(){
        return straße;
    }
    public void set_straße(String straße){
        this.straße = straße;
    }
    public String get_hausnummer(){
        return hausnummer;
    }
    public void set_hausnummer(String hausnummer){
        this.hausnummer = hausnummer;
    }
    public String get_zusatz(){
        return zusatz;
    }
    public void set_zusatz(String zusatz){
        this.zusatz = zusatz;
    }
    public String get_plz(){
        return plz;
    }
    public void set_plz(String plz){
        this.plz = plz;
    }
    public String get_stadt(){
        return stadt;
    }
    public void set_stadt(String stadt){
        this.stadt = stadt;
    }

    public Adresse(int adress_id,
    String straße,
    String hausnummer,
    String zusatz,
    String plz,
    String stadt){
        this.adress_id = adress_id;
        this.straße = straße;
        this.hausnummer = hausnummer;
        this.zusatz = zusatz;
        this.plz = plz;
        this.stadt = stadt;
    }
}
