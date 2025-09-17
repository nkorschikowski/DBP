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
@Table(name = "musikcds")
@IdClass(MusikCDId.class)
public class MusikCD {
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt_id;
    private String label;
    //private date erscheinungsdatum; //TODO: date

    public Produkt get_Produkt_id() {
        return produkt_id;
    }

    public void set_Produkt_id(Produkt produkt_id) {
        this.produkt_id = produkt_id;
    }

    public String get_Label() {
        return label;
    }

    public void set_Label(String label) {
        this.label = label;
    }
    //private date erscheinungsdatum(){return erscheinungsdatum;} //TODO: date
    //private void setErscheinungsdatum(date erscheinungsdatum){this.erscheinungsdatum = erscheinungsdatum;} //TODO: date

    public MusikCD(Produkt produkt_id, String label) {
        this.produkt_id = produkt_id;
        this.label = label;
    }

    public MusikCD() {
        // Default constructor for JPA
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusikCD)) return false;
        MusikCD that = (MusikCD) o;
        return produkt_id != null && produkt_id.equals(that.produkt_id);
    }
    @Override
    public int hashCode() {
        return 31 + (produkt_id != null ? produkt_id.hashCode() : 0); 
    }
}
