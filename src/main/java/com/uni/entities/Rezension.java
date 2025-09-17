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
@Table(name = "rezensionen")
@IdClass(RezensionId.class)
public class Rezension {

    @Id //composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false)
    private Person person_id;

    @Id //composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = false)
    private Produkt produkt_nr;

    // private date date; //TODO: date
    private String summary;
    private int bewertung; //TODO: small int
    private String content;

    public Person get_Person_id() {
        return person_id;
    }

    public void set_Person_id(Person person_id) {
        this.person_id = person_id;
    }

    public Produkt get_Produkt_nr() {
        return produkt_nr;
    }

    public void set_Produkt_nr(Produkt produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    public String get_Summary() {
        return summary;
    }

    public void set_Summary(String summary) {
        this.summary = summary;
    }

    public int get_Bewertung() {
        return bewertung;
    }

    public void set_Bewertung(int bewertung) {
        this.bewertung = bewertung;
    }

    public String get_Content() {
        return content;
    }

    public void set_Content(String content) {
        this.content = content;
    }

    public Rezension() {
    }

    public Rezension(Person person_id, Produkt produkt_nr, String summary, int bewertung, String content) {
        this.person_id = person_id;
        this.produkt_nr = produkt_nr;
        this.summary = summary;
        this.bewertung = bewertung;
        this.content = content;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rezension)) return false;
        Rezension that = (Rezension) o;
        return person_id != null && produkt_nr != null &&
               person_id.equals(that.person_id) &&
               produkt_nr.equals(that.produkt_nr);
    }
    @Override
    public int hashCode() {
        int result = (person_id != null ? person_id.hashCode() : 0);
        result = 31 * result + (produkt_nr != null ? produkt_nr.hashCode() : 0);
        return result;
    }
}
