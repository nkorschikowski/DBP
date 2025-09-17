package com.uni.entities;


import java.io.Serializable;
import java.util.Objects;

public class BuchId implements Serializable {
    private String produkt_nr;   // must match the type of Produkt’s @Id

    public String get_produkt_nr() {
        return produkt_nr;
    }
    public void set_produkt_nr(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    public BuchId() {}

    public BuchId(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BuchId)) return false;
        BuchId that = (BuchId) o;
        return Objects.equals(produkt_nr, that.produkt_nr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produkt_nr);
    }
}
