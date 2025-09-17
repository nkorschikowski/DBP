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
@Table(name = "kunden")
public class Kunde {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = true)
    private Person person_id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "adress_id", referencedColumnName = "adress_id", nullable = true)
    private Adresse adresse_id;

    private int kontonummer;

    public Kunde() {}

    public Kunde(Person person_id, Adresse adresse_id, int kontonummer) {
        this.person_id = person_id;
        this.adresse_id = adresse_id;
        this.kontonummer = kontonummer;
    }

    public Person get_Person_id() {
        return person_id;
    }

    public void set_Person(Person person_id) {
        this.person_id = person_id;
    }

    public Adresse get_Adresse_id() {
        return adresse_id;
    }

    public void set_Adresse_id(Adresse adresse_id) {
        this.adresse_id = adresse_id;
    }

    public int get_Kontonummer() {
        return kontonummer;
    }

    public void set_Kontonummer(int kontonummer) {
        this.kontonummer = kontonummer;
    }
}
