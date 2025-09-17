package com.uni.entities;

public class KaufProduktId {
    private int kauf_id;
    private int angebot_id;

    public KaufProduktId() {}

    public KaufProduktId(int kauf_id, int angebot_id) {
        this.kauf_id = kauf_id;
        this.angebot_id = angebot_id;
    }

    public int get_Kauf_id() {
        return kauf_id;
    }

    public void set_Kauf_id(int kauf_id) {
        this.kauf_id = kauf_id;
    }

    public int get_Angebot_id() {
        return angebot_id;
    }

    public void set_Angebot_id(int angebot_id) {
        this.angebot_id = angebot_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KaufProduktId)) return false;
        KaufProduktId that = (KaufProduktId) o;
        return kauf_id == that.kauf_id && angebot_id == that.angebot_id;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(kauf_id, angebot_id);
    }
}
