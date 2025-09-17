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
@Table(name = "filialen")
public class Filiale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int filiale_id; 
    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "adress_id", referencedColumnName = "adress_id", nullable = true)
    private Adresse adresse;

    // Getter and Setter for filiale_id
    public int get_Filiale_id() {
        return filiale_id;
    }

    public void set_Filiale_id(int filiale_id) {
        this.filiale_id = filiale_id;
    }

    // Getter and Setter for name
    public String get_Name() {
        return name;
    }

    public void set_Name(String name) {
        this.name = name;
    }

    // Getter and Setter for adresse
    public Adresse get_Adresse() {
        return adresse;
    }

    public void set_Adresse(Adresse adresse) {
        this.adresse = adresse;
    }

    // Constructor
    public Filiale(int filiale_id, String name, Adresse adresse) {
        this.filiale_id = filiale_id;
        this.name = name;
        this.adresse = adresse;
    }

    // Default constructor (required by JPA)
    public Filiale() {
    }
}
