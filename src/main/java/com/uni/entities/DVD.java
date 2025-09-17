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
@Table(name = "dvds")
@IdClass(DVDId.class)
public class DVD{
    
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt_nr;
    private String format;
    //private time laufzeit; //TODO: time
    private int region_code; //TODO: small int?

    public Produkt get_produkt_nr() {
        return produkt_nr;
    }
    public void set_produkt_nr(Produkt produkt_nr) {
        this.produkt_nr = produkt_nr;
    }
    public String get_format() {
        return format;
    }
    public void set_format(String format) {
        this.format = format;
    }
    public int get_region_code() {
        return region_code;
    }
    public void set_region_code(int region_code) {
        this.region_code = region_code;
    }

    public DVD(Produkt produkt_nr,
    String format,
    int region_code) {
        this.produkt_nr = produkt_nr;
        this.format = format;
        this.region_code = region_code;
    }
    public DVD() {
        // Default constructor for JPA
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DVD)) return false;
        DVD that = (DVD) o;
        return produkt_nr != null && produkt_nr.equals(that.produkt_nr);
    }
    @Override
    public int hashCode() {
        return 31 + (produkt_nr != null ? produkt_nr.hashCode() : 0); 
    }

}
