package com.imagepop.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String name;


    protected User() {}

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, name='%s']",
                id, name);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
    	return this.id;
    }

    public String getName() {
    	return this.name;
    }
}