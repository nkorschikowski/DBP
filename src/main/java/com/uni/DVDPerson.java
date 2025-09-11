package com.uni;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "dvd_personen")
public class DVDPerson {
    @Id //TODO: composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt;
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = true)
    private Person person;
    private String rolle;


    private Produkt get_produkt() {
        return produkt;
    }
    private void set_produkt(Produkt produkt) {
        this.produkt = produkt;
    }
    private Person get_person() {
        return person;
    }
    private void set_person(Person person) {
        this.person = person;
    }
    private String get_rolle() {
        return rolle;
    }
    private void set_rolle(String rolle) {
        this.rolle = rolle;
    }
    private DVDPerson(Produkt produkt,
                     Person person,
                     String rolle
    ) {
        this.produkt = produkt;
        this.person = person;
        this.rolle = rolle;
    }
}
