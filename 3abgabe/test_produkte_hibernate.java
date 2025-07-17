package tutorial.hibernate.postgresql;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Produkte")
public class Student implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "produkr_nr")
    private String produkr_nr;

    @Column(name = "titel")
    private String titel;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "verkaufsrang")
    private Integer verkaufsrang;

    @Column(name = "bild")
    private String bild;

    @Column(name = "produkttyp")
    private String produkttyp;

    public String getProdukr_nr() {
    return produkr_nr;
    }

    public void setProdukt_nr(String produkr_nr) {
    this.produkt_nr = produkr_nr;
    }

    public String getTitel() {
    return titel;
    }

    public void setTitel(String titel) {
    this.titel = titel;
    }

    public Float getRating() {
    return rating;
    }

    public void setRating(Float rating) {
    this.rating = rating;
    }

    public Integer getVerkaufsrang() {
    return verkaufsrang;
    }

    public void setVerkaufsrang(Integer verkaufsrang) {
    this.verkaufsrang = verkaufsrang;
    }

    public String getBild() {
    return bild;
    }

    public void setBild(String bild) {
    this.bild = bild;
    }

    public String getProdukttyp() {
    return produkttyp;
    }

    public void setProdukttyp(String produkttyp) {
    this.produkttyp = produkttyp;
    }

}