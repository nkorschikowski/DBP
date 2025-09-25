package com.uni.entities;

import java.io.Serializable;

public class RezensionId implements Serializable {
    private String produkt_nr;
    private int person_id;

    public RezensionId() {}

    public RezensionId(String produkt_nr, int person_id) {
        this.produkt_nr = produkt_nr;
        this.person_id = person_id;
    }

    public String get_Produkt_nr() {
        return produkt_nr;
    }

    public void set_Produkt_nr(String produkt_nr) {
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
        if (!(o instanceof RezensionId)) return false;
        RezensionId that = (RezensionId) o;
        return person_id == that.person_id && produkt_nr.equals(that.produkt_nr);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(produkt_nr, person_id);
    }
}
