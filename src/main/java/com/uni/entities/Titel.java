package com.uni.entities;

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
@Table(name = "titel")
public class Titel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int titel_id;
    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = false)
    private Produkt produkt_nr;

    public Titel() {
        // Default constructor for JPA
    }

    public Titel(int titel_id, String name, Produkt produkt_nr) {
        this.titel_id = titel_id;
        this.name = name;
        this.produkt_nr = produkt_nr;
    }

    public int get_Titel_id() {
        return titel_id;
    }

    public void set_Titel_id(int titel_id) {
        this.titel_id = titel_id;
    }

    public String get_Name() {
        return name;
    }

    public void set_Name(String name) {
        this.name = name;
    }

    public Produkt get_Produkt_nr() {
        return produkt_nr;
    }

    public void set_Produkt_nr(Produkt produkt_nr) {
        this.produkt_nr = produkt_nr;
    }
}
