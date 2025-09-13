package com.uni.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personen")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int person_id;

    private String name;


    // Getter and Setter for person_id
    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
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
