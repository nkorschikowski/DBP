package com.uni.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "buecher")
@IdClass(BuchId.class)
public class Buch{
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt_nr;
    private int seitenzahl;
    //private date erscheinungsdatum; //TODO: Datum
    private String isbn;
    private String verlag;


    public Produkt get_produkt_nr(){
        return produkt_nr;
    }
    public void set_produkt_nr(Produkt produkt_nr){
        this.produkt_nr = produkt_nr;
    }
    public int get_seitenzahl(){
        return seitenzahl;
    }
    public void set_seitenzahl(int seitenzahl){
        this.seitenzahl = seitenzahl;
    }
    public String get_isbn(){
        return isbn;
    }
    public void set_isbn(String isbn){
        this.isbn = isbn;
    }
    public String get_verlag(){
        return verlag;
    }
    public void set_verlag(String verlag){
        this.verlag = verlag;
    }

    public Buch(Produkt produkt_nr, int seitenzahl, String isbn, String verlag){
        this.produkt_nr = produkt_nr;
        this.seitenzahl = seitenzahl;
        this.isbn = isbn;
        this.verlag = verlag;   
    }
    public Buch(){
        // Default constructor for JPA
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Buch)) return false;
        Buch buch = (Buch) o;
        return produkt_nr != null && produkt_nr.equals(buch.produkt_nr);
    }

    @Override
    public int hashCode() {
        return produkt_nr != null ? produkt_nr.hashCode() : 0;
    }
}
