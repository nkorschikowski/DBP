package com.uni;

public class RezensionId {
    private String produkt_nr;
    private int kunde_id;

    public RezensionId() {}

    public RezensionId(String produkt_nr, int kunde_id) {
        this.produkt_nr = produkt_nr;
        this.kunde_id = kunde_id;
    }

    private String getProdukt_nr() {
        return produkt_nr;
    }

    private void setProdukt_nr(String produkt_nr) {
        this.produkt_nr = produkt_nr;
    }

    private int getKunde_id() {
        return kunde_id;
    }

    private void setKunde_id(int kunde_id) {
        this.kunde_id = kunde_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RezensionId)) return false;
        RezensionId that = (RezensionId) o;
        return kunde_id == that.kunde_id && produkt_nr.equals(that.produkt_nr);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(produkt_nr, kunde_id);
    }
}
