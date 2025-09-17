package com.uni.entities;

import java.io.Serializable;
import java.util.Objects;

public class KuenstlerCDId implements Serializable {
    private String produkt_nr;  
    private int person_id;   

    public KuenstlerCDId() {}

    public KuenstlerCDId(String produkt_nr, int person_id) {
        this.produkt_nr = produkt_nr;
        this.person_id = person_id;
    }

    public String get_produkt_nr() {
        return produkt_nr;
    }
    public void set_produkt_nr(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }
    public int get_person_id() {
        return person_id;
    }
    public void set_person_id(int person_id) {
        this.person_id = person_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KuenstlerCDId)) return false;
        KuenstlerCDId that = (KuenstlerCDId) o;
        return Objects.equals(produkt_nr, that.produkt_nr) &&
               Objects.equals(person_id, that.person_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produkt_nr, person_id);
    }
}
