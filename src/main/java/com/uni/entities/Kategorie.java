package com.uni.entities;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "kategorien")
public class Kategorie{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private int kategorie_id;

    String name;

    @ManyToOne 
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="oberkategorie_id", referencedColumnName="kategorie_id")
    private Kategorie oberkategorie_id; 
    //TODO: self refferential
    

    @OneToMany(mappedBy = "oberkategorie_id") 
    private List<Kategorie> unterkategorien = new ArrayList<>();
    
    public int get_kategorie_id() {
        return kategorie_id;
    }
    public void set_kategorie_id(int kategorie_id) {
        this.kategorie_id = kategorie_id;
    }
    public String get_name(){
        return this.name;
    }
    public void set_name(String name){
        this.name = name;
    }
    public Kategorie get_oberkategorie_id() {
        return oberkategorie_id;
    }
    public void set_oberkategorie_id(Kategorie oberkategorie_id) {
        this.oberkategorie_id = oberkategorie_id;
    }
    public List<Kategorie> get_Unterkategorien(){
        return unterkategorien;
    }
    public void set_Unterkategorien(List<Kategorie> unterkategorien){
        this.unterkategorien = unterkategorien;
    }
    public Kategorie (){};
    
    public Kategorie(int kategorie_id,
     String name,
     Kategorie oberkategorie_id
      ){
        this.kategorie_id = kategorie_id;
        this.name = name;
        this.oberkategorie_id = oberkategorie_id;
    }

}
