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
@Table(name = "dvd_personen")
@IdClass(DVDPersonId.class)
public class DVDPerson {
    @Id //composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt_nr;
    @Id //composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = true)
    private Person person_id;
    private String rolle;


    public Produkt get_produkt_id() {
        return produkt_nr;
    }
    public void set_produkt_id(Produkt produkt_nr) {
        this.produkt_nr = produkt_nr;
    }
    public Person get_person_id() {
        return person_id;
    }
    public void set_person_id(Person person_id) {
        this.person_id = person_id;
    }
    public String get_rolle() {
        return rolle;
    }
    public void set_rolle(String rolle) {
        this.rolle = rolle;
    }
    public DVDPerson(Produkt produkt_nr,
                     Person person_id,
                     String rolle
    ) {
        this.produkt_nr = produkt_nr;
        this.person_id = person_id;
        this.rolle = rolle;
    }
    public DVDPerson() {
        // Default constructor for JPA
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DVDPerson)) return false;
        DVDPerson that = (DVDPerson) o;
        return produkt_nr != null && produkt_nr.equals(that.produkt_nr) &&
               person_id != null && person_id.equals(that.person_id);    
    }
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (produkt_nr != null ? produkt_nr.hashCode() : 0);
        result = 31 * result + (person_id != null ? person_id.hashCode() : 0);
        return result;
    }
}
