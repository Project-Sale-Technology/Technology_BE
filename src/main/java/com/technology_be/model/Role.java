package com.technology_be.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany
    @JoinTable(name = "user_role" , joinColumns = @JoinColumn(name = "user_id") ,
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<User> users;

    public Role() {}

    public Role(String name) {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}