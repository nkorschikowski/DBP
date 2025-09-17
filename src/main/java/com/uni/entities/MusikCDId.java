package com.uni.entities;

import java.io.Serializable;
import java.util.Objects;

public class MusikCDId implements Serializable {
    private String produkt_nr;  
 
    public MusikCDId() {}

    public MusikCDId(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    public String get_produkt_nr() {
        return produkt_nr;
    }
    public void set_produkt_nr(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusikCDId)) return false;
        MusikCDId that = (MusikCDId) o;
        return Objects.equals(produkt_nr, that.produkt_nr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produkt_nr);
    }
}
