package com.uni.entities;

public class ProduktKategorieId {
    private String produkt_nr;
    private String kategorie;

    public ProduktKategorieId() {}

    public ProduktKategorieId(String produkt_nr, String kategorie) {
        this.produkt_nr = produkt_nr;
        this.kategorie = kategorie;
    }

    private String getProdukt_nr() {
        return produkt_nr;
    }

    private void setProdukt_nr(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    private String getKategorie() {
        return kategorie;
    }

    private void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProduktKategorieId)) return false;
        ProduktKategorieId that = (ProduktKategorieId) o;
        return produkt_nr.equals(that.produkt_nr) && kategorie.equals(that.kategorie);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(produkt_nr, kategorie);
    }
}
