package com.uni.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "buecher")
public class Buch{
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt;
    private int seitenzahl;
    //private date erscheinungsdatum; //TODO: Datum
    private String isbn;
    private String verlag;


    private Produkt get_produkt(){
        return produkt;
    }
    private void set_produkt(Produkt produkt){
        this.produkt = produkt;
    }
    private int get_seitenzahl(){
        return seitenzahl;
    }
    private void set_seitenzahl(int seitenzahl){
        this.seitenzahl = seitenzahl;
    }
    private String get_isbn(){
        return isbn;
    }
    private void set_isbn(String isbn){
        this.isbn = isbn;
    }
    private String get_verlag(){
        return verlag;
    }
    private void set_verlag(String verlag){
        this.verlag = verlag;
    }

    public Buch(Produkt produkt, int seitenzahl, String isbn, String verlag){
        this.produkt = produkt;
        this.seitenzahl = seitenzahl;
        this.isbn = isbn;
        this.verlag = verlag;   
    }
}
