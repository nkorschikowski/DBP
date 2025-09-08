package com.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "personen")
public class Person{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int person_id;
    private String name;
    //TODO: FK


    public Person(){}
}