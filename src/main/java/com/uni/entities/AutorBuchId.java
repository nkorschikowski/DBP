package com.uni.entities;

import java.io.Serializable;


public class AutorBuchId implements Serializable {
    private int person_id;
    private String produkt_nr;

    public AutorBuchId() {}

    public AutorBuchId(int person_id, String produkt_nr) {
        this.person_id = person_id;
        this.produkt_nr = produkt_nr;
    }

    public int get_Person_id() {
        return person_id;
    }

    public void set_Person_id(int person_id) {
        this.person_id = person_id;
    }

    public String get_Produkt_nr() {
        return produkt_nr;
    }

    public void set_Produkt_nr(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AutorBuchId)) return false;
        AutorBuchId that = (AutorBuchId) o;
        return person_id == that.person_id &&
               java.util.Objects.equals(produkt_nr, that.produkt_nr);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(person_id, produkt_nr);
    }

}
