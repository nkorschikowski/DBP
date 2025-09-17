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
@Table(name = "produkt_kategorie")
@IdClass(ProduktKategorieId.class)
public class ProduktKategorie {

    @Id // composite key (should use @EmbeddedId or @IdClass for real composite key)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = false)
    private Produkt produkt_nr;

    @Id // composite key (should use @EmbeddedId or @IdClass for real composite key)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "kategorie_id", referencedColumnName = "kategorie_id", nullable = false)
    private Kategorie kategorie;

    public ProduktKategorie() {}

    public ProduktKategorie(Produkt produkt_nr, Kategorie kategorie) {
        this.produkt_nr = produkt_nr;
        this.kategorie = kategorie;
    }

    public Produkt get_Produkt_nr() {
        return produkt_nr;
    }

    public void set_Produkt_nr(Produkt produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    public Kategorie get_Kategorie() {
        return kategorie;
    }

    public void set_Kategorie_id(Kategorie kategorie) {
        this.kategorie = kategorie;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProduktKategorie)) return false;
        ProduktKategorie that = (ProduktKategorie) o;
        return produkt_nr != null && produkt_nr.equals(that.produkt_nr) &&
               kategorie != null && kategorie.equals(that.kategorie);
    }
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (produkt_nr != null ? produkt_nr.hashCode() :  0);
        result = 31 * result + (kategorie != null ? kategorie.hashCode() : 0);
        return result;  
    }  
    
}
