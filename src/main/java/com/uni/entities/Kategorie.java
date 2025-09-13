package com.uni.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "kategorien")
public class Kategorie{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private int kategorie_id;
    @ManyToOne 
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="oberkategorie_id", referencedColumnName="kategorie_id")
    private Kategorie oberkategorie; 
    //TODO: self refferential

    private int get_kategorie_id() {
        return kategorie_id;
    }
    private void set_kategorie_id(int kategorie_id) {
        this.kategorie_id = kategorie_id;
    }
    private Kategorie get_oberkategorie() {
        return oberkategorie;
    }
    private void set_oberkategorie_id(Kategorie oberkategorie_id) {
        this.oberkategorie = oberkategorie_id;
    }
    private Kategorie(int kategorie_id,
     Kategorie oberkategorie
      ){
        this.kategorie_id = kategorie_id;
        this.oberkategorie = oberkategorie;
    }

}
