package com.uni.entities;

public class KaufProduktId {
    private int kauf;
    private int angebot;

    public KaufProduktId() {}

    public KaufProduktId(int kauf, int angebot) {
        this.kauf = kauf;
        this.angebot = angebot;
    }

    public int getKauf() {
        return kauf;
    }

    public void setKauf(int kauf) {
        this.kauf = kauf;
    }

    public int getAngebot() {
        return angebot;
    }

    public void setAngebot(int angebot) {
        this.angebot = angebot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KaufProduktId)) return false;
        KaufProduktId that = (KaufProduktId) o;
        return kauf == that.kauf && angebot == that.angebot;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(kauf, angebot);
    }
}
