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
@Table(name = "aehnliche_produkte")
@IdClass(AehnlicheProdukteId.class)
public class AehnlicheProdukte {
    @Id //composite key
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr1", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt_nr1;
    @Id
    private Produkt produkt_nr2;

    public Produkt get_produkt_nr1(){
        return produkt_nr1;
    }
    public void set_produkt_nr1(Produkt produkt_nr1){
        this.produkt_nr1 = produkt_nr1;
    }
    public Produkt get_produkt_nr2(){
        return produkt_nr2;
    }
    public void set_produkt_nr2(Produkt produkt_nr2){
        this.produkt_nr2 = produkt_nr2;
    }

    public AehnlicheProdukte(Produkt produkt_nr1,
    Produkt produkt_nr2){
        this.produkt_nr1 = produkt_nr1;
        this.produkt_nr2 = produkt_nr2; 
    }
    public AehnlicheProdukte(){
        // Default constructor for JPA
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AehnlicheProdukte)) return false;
        AehnlicheProdukte that = (AehnlicheProdukte) o;
        return produkt_nr1 != null && produkt_nr1.equals(that.produkt_nr1) &&
               produkt_nr2 != null && produkt_nr2.equals(that.produkt_nr2);
    }
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (produkt_nr1 != null ? produkt_nr1.hashCode() : 0);
        result = 31 * result + (produkt_nr2 != null ? produkt_nr2.hashCode() : 0);
        return result;
    }
}
