package com.uni.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "musikcds")
public class MusikCD {
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt;
    private String label;
    //private date erscheinungsdatum; //TODO: date

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    //private date erscheinungsdatum(){return erscheinungsdatum;} //TODO: date
    //private void setErscheinungsdatum(date erscheinungsdatum){this.erscheinungsdatum = erscheinungsdatum;} //TODO: date

    public MusikCD(Produkt produkt, String label) {
        this.produkt = produkt;
        this.label = label;
    }

    public MusikCD() {
        // Default constructor for JPA
    }
}
