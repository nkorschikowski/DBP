package com.uni.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "kauf_produkt")
public class KaufProdukt {
    @Id // TODO: composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kauf_id", referencedColumnName = "kauf_id", nullable = true)
    private Kauf kauf;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "angebot_id", referencedColumnName = "angebot_id", nullable = true)
    private Angebot angebot;

    private int anzahl;
    // private money einzelpreis;


    public Kauf getKauf() {
        return kauf;
    }

    public void setKauf(Kauf kauf) {
        this.kauf = kauf;
    }

    public Angebot getAngebot() {
        return angebot;
    }

    public void setAngebot(Angebot angebot) {
        this.angebot = angebot;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    // private money getEinzelpreis() {
    //     return einzelpreis;
    // }
    // private void setEinzelpreis(money preis) {
    //     this.einzelpreis = preis;
    // }

    public KaufProdukt(Kauf kauf, Angebot angebot, int anzahl) {
        this.kauf = kauf;
        this.angebot = angebot;
        this.anzahl = anzahl;
    }

    public KaufProdukt() {}
}
