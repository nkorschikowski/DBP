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
@Table(name = "kuenstler_cds")
@IdClass(KuenstlerCDId.class)
public class KuenstlerCD {

    @Id //composite key (should use @EmbeddedId or @IdClass for real composite key)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = false)
    private Produkt produkt_nr;

    @Id //composite key (should use @EmbeddedId or @IdClass for real composite key)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false)
    private Person person_id;

    public KuenstlerCD() {}

    public KuenstlerCD(Produkt produkt_nr, Person person_id) {
        this.produkt_nr = produkt_nr;
        this.person_id = person_id;
    }

    public Produkt get_Produkt_nr() {
        return produkt_nr;
    }

    public void set_produkt_nr(Produkt produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    public Person get_Person_id() {
        return person_id;
    }

    public void set_Person_id(Person person_id) {
        this.person_id = person_id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KuenstlerCD)) return false;
        KuenstlerCD that = (KuenstlerCD) o;
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
