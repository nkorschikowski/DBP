package com.uni.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "personen")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int person_id;

    private String name;


    // Getter and Setter for person_id
    public int get_Person_id() {
        return person_id;
    }

    public void set_Person_id(int person_id) {
        this.person_id = person_id;
    }

    // Getter and Setter for name
    public String get_Name() {
        return name;
    }

    public void set_Name(String name) {
        this.name = name;
    }

    // Constructor
    public Person(int person_id, String name, Person person) {
        this.person_id = person_id;
        this.name = name;
    }

    // Default constructor for JPA
    public Person() {
    }
}
