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
@Table(name = "kauf")
public class Kauf {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int kauf_id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "filiale_id", referencedColumnName = "filiale_id", nullable = true)
    private Filiale filiale;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = true)
    private Person person;

    // TODO: Date kaufdatum

    public int getKauf_id() {
        return kauf_id;
    }

    public void setKauf_id(int kauf_id) {
        this.kauf_id = kauf_id;
    }

    public Filiale getFiliale() {
        return filiale;
    }

    public void setFiliale(Filiale filiale) {
        this.filiale = filiale;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Kauf(int kauf_id, Filiale filiale, Person person) {
        this.kauf_id = kauf_id;
        this.filiale = filiale;
        this.person = person;
    }

    public Kauf() {
        // Default constructor for JPA
    }
}
