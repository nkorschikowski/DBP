package com.uni.entities;

public class ProduktKategorieId {
    private String produkt_nr;
    private int kategorie_id;

    public ProduktKategorieId() {}

    public ProduktKategorieId(String produkt_nr, int kategorie_id) {
        this.produkt_nr = produkt_nr;
        this.kategorie_id = kategorie_id;
    }

    public String get_Produkt_nr() {
        return produkt_nr;
    }

    public void set_Produkt_nr(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    public int get_Kategorie_id() {
        return kategorie_id;
    }

    public void set_Kategorie_id(int kategorie_id) {
        this.kategorie_id = kategorie_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProduktKategorieId)) return false;
        ProduktKategorieId that = (ProduktKategorieId) o;
        return produkt_nr.equals(that.produkt_nr) && kategorie_id == that.kategorie_id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(produkt_nr, kategorie_id);
    }
}
