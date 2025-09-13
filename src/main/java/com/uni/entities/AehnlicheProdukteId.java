package com.uni.entities;

import java.io.Serializable;


public class AehnlicheProdukteId implements Serializable {
    private String produkt_nr1;
    private String produkt_nr2;

    public AehnlicheProdukteId() {}

    public AehnlicheProdukteId(String produkt_nr1, String produkt_nr2) {
        this.produkt_nr1 = produkt_nr1;
        this.produkt_nr2 = produkt_nr2;
    }
    private String get_produkt_nr1(){
        return produkt_nr1;
    }
    private void set_produkt_nr1(String produkt_nr1){
        this.produkt_nr1 = produkt_nr1;
    }
    private String get_produkt_nr2(){
        return produkt_nr2;
    }
    private void set_produkt_nr2(String produkt_nr2){
        this.produkt_nr2 = produkt_nr2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AehnlicheProdukteId)) return false;
        AehnlicheProdukteId that = (AehnlicheProdukteId) o;
        return produkt_nr1.equals(that.produkt_nr1) && produkt_nr2.equals(that.produkt_nr2);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(produkt_nr1, produkt_nr2);
    } 
}