package com.uni;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "dvds")
public class DVD{
    
    @Id
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "produkt_nr", referencedColumnName = "produkt_nr", nullable = true)
    private Produkt produkt;
    private String format;
    //private time laufzeit; //TODO: time
    private int region_code; //TODO: small int?

    private Produkt get_produkt() {
        return produkt;
    }
    private void set_produkt_nr(Produkt produkt) {
        this.produkt = produkt;
    }
    private String get_format() {
        return format;
    }
    private void set_format(String format) {
        this.format = format;
    }
    private int get_region_code() {
        return region_code;
    }
    private void set_region_code(int region_code) {
        this.region_code = region_code;
    }

    public DVD(Produkt produkt,
    String format,
    int region_code) {
        this.produkt = produkt;
        this.format = format;
        this.region_code = region_code;
    }

}
