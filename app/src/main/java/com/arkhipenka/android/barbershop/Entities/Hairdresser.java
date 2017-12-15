package com.arkhipenka.android.barbershop.Entities;

import java.io.Serializable;

public class Hairdresser implements Accessable, Serializable {

    public static final String SPECIALITY_MAN = "man";
    public static final String SPECIALITY_WOMAN = "female";

    private long id;
    private String photoUrl;
    private String lastName;
    private String firstName;
    private String clientGender;
    private String password;

    public Hairdresser(String photoUrl, String lastName, String firstName, String clientGender, String password) {
        this.photoUrl = photoUrl;
        this.lastName = lastName;
        this.firstName = firstName;
        this.clientGender = clientGender;
        this.password = password;
    }

    public Hairdresser() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPermissions() {
        return Accessable.TYPE_HAIRDRESSER;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getClientGender() {
        return clientGender;
    }

    public void setClientGender(String clientGender) {
        this.clientGender = clientGender;
    }

}
