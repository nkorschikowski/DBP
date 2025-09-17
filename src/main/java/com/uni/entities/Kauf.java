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
    private Filiale filiale_id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = true)
    private Person person_id;

    // TODO: Date kaufdatum

    public int get_Kauf_id() {
        return kauf_id;
    }

    public void set_Kauf_id(int kauf_id) {
        this.kauf_id = kauf_id;
    }

    public Filiale get_Filiale_id() {
        return filiale_id;
    }

    public void set_Filiale(Filiale filiale_id) {
        this.filiale_id = filiale_id;
    }

    public Person get_Person_id() {
        return person_id;
    }

    public void set_Person_id(Person person_id) {
        this.person_id = person_id;
    }

    public Kauf(int kauf_id, Filiale filiale_id, Person person_id) {
        this.kauf_id = kauf_id;
        this.filiale_id = filiale_id;
        this.person_id = person_id;
    }

    public Kauf() {
        // Default constructor for JPA
    }
}
