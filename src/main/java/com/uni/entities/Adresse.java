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

    private int get_adress_id(){
        return adress_id;
    }
    private void set_adress_id(int adress_id){
        this.adress_id = adress_id;
    }
    private String get_straße(){
        return straße;
    }
    private void set_straße(String straße){
        this.straße = straße;
    }
    private String get_hausnummer(){
        return hausnummer;
    }
    private void set_hausnummer(String hausnummer){
        this.hausnummer = hausnummer;
    }
    private String get_zusatz(){
        return zusatz;
    }
    private void set_zusatz(String zusatz){
        this.zusatz = zusatz;
    }
    private String get_plz(){
        return plz;
    }
    private void set_plz(String plz){
        this.plz = plz;
    }
    private String get_stadt(){
        return stadt;
    }
    private void set_stadt(String stadt){
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
