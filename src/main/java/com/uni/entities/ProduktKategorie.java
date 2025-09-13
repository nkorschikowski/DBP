package com.uni.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produkt_kategorie")
public class ProduktKategorie {

    @Id // TODO: composite key (should use @EmbeddedId or @IdClass for real composite key)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = false)
    private Produkt produkt;

    @Id // TODO: composite key (should use @EmbeddedId or @IdClass for real composite key)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kategorie_id", referencedColumnName = "kategorie_id", nullable = false)
    private Kategorie kategorie;

    public ProduktKategorie() {}

    public ProduktKategorie(Produkt produkt, Kategorie kategorie) {
        this.produkt = produkt;
        this.kategorie = kategorie;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public void setProdukt(Produkt produkt) {
        this.produkt = produkt;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }
}
