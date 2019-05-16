package com.loker.consumvehicle.model;

public class User {

    private String UID;
    private String name;
    private String firstLastName;
    private String secondLastName;
    private String email;
    private String password;

    public User() {
    }

    public User(String UID, String email) {
        this.UID = UID;
        this.email = email;
    }


    public User(String UID, String name, String firstLastName, String secondLastName, String email) {
        this.UID = UID;
        this.name = name;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
