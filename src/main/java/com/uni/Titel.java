package com.uni;

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
    private Produkt produkt;

    public Titel() {
        // Default constructor for JPA
    }

    public Titel(int titel_id, String name, Produkt produkt) {
        this.titel_id = titel_id;
        this.name = name;
        this.produkt = produkt;
    }

    public int getTitel_id() {
        return titel_id;
    }

    public void setTitel_id(int titel_id) {
        this.titel_id = titel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }
}
