package com.example.lucaoliveira.caronauniversitaria.data;

/**
 * Created by lucaoliveira on 6/19/2016.
 */
public class User {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String university;
    private String accessType;
    private String addressOrigin;
    private String addressDestiny;
    private long id;

    //TODO : ADD TO DATABASE
    private int numberOfStudents;

    //TODO : ADD USER IMAGE
    private int thumbnail;

    public User() {
    }

    public User(String name, int numberOfStudents, String email, int thumbnail, String phoneNumber, String addressOrigin, String addressDestiny) {
        this.name = name;
        this.numberOfStudents = numberOfStudents;
        this.email = email;
        this.thumbnail = thumbnail;
        this.phoneNumber = phoneNumber;
        this.addressOrigin = addressOrigin;
        this.addressDestiny = addressDestiny;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getAddressOrigin() {
        return addressOrigin;
    }

    public void setAddressOrigin(String addressOrigin) {
        this.addressOrigin = addressOrigin;
    }

    public String getAddressDestiny() {
        return addressDestiny;
    }

    public void setAddressDestiny(String addressDestiny) {
        this.addressDestiny = addressDestiny;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
