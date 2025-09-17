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
@Table(name = "kauf_produkt")
@IdClass(KaufProduktId.class)
public class KaufProdukt {
    @Id //composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kauf_id", referencedColumnName = "kauf_id", nullable = true)
    private Kauf kauf_id;

    @Id //composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "angebot_id", referencedColumnName = "angebot_id", nullable = true)
    private Angebot angebot_id;

    private int anzahl;
    // private money einzelpreis;


    public Kauf get_Kauf_id() {
        return kauf_id;
    }

    public void set_Kauf_id(Kauf kauf_id) {
        this.kauf_id = kauf_id;
    }

    public Angebot get_Angebot_id() {
        return angebot_id;
    }

    public void set_Angebot_id(Angebot angebot_id) {
        this.angebot_id = angebot_id;
    }

    public int get_Anzahl() {
        return anzahl;
    }

    public void set_Anzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    // private money getEinzelpreis() {
    //     return einzelpreis;
    // }
    // private void setEinzelpreis(money preis) {
    //     this.einzelpreis = preis;
    // }

    public KaufProdukt(Kauf kauf_id, Angebot angebot_id, int anzahl) {
        this.kauf_id = kauf_id;
        this.angebot_id = angebot_id;
        this.anzahl = anzahl;
    }

    public KaufProdukt() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KaufProdukt)) return false;
        KaufProdukt that = (KaufProdukt) o;
        return kauf_id != null && kauf_id.equals(that.kauf_id) &&
               angebot_id != null && angebot_id.equals(that.angebot_id);
    }
    @Override
    public int hashCode() {
        int result = kauf_id != null ? kauf_id.hashCode() : 0;
        result = 31 * result + (angebot_id != null ? angebot_id.hashCode() : 0);
        return result;
    }
}
