package com.boolck.authdemo.model;

import com.boolck.authdemo.security.Token;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String name;
    private Token token;
    private Set<Role> roles;

    public User(String name, Token token){
        this.name = name;
        this.token = token;
        this.roles = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role){
        roles.add(role);
    }

    public void removeRole(Role role){
        roles.remove(role);
    }

    public boolean containsRole(Role role) {
        return roles.contains(role);
    }
}
