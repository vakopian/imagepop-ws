package com.imagepop.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.EnumSet;
import java.util.HashSet;

@Entity
public class User {
    public enum Role {
        USER, ADMINISTRATOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private EnumSet<Role> roles;


    protected User() {
    }

    public User(String name) {
        this.firstName = name;
        this.lastName = "a";
        this.password = "a";
        this.email = "a";
    }


    @Override
    public String toString() {
        return String.format(
                "User[id=%d, name='%s', email='%s', password='%s]",
                id, firstName, email, password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EnumSet<Role> getRoles() {
        return roles;
    }

    public void setRoles(EnumSet<Role> roles) {
        this.roles = roles;
    }

    public void addRole (Role role) {
        this.roles.add(role);
    }

    public boolean removeRole (Role role) {
        return this.roles.remove(role);
    }

}