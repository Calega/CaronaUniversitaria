package com.lucaoliveira.unicaroneiro.model;

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
    private int numberOfStudentsAllowed;
    private String studentRegister;
    private String image;
    private double valueForRent;

    public User() {
    }

    public User(String name, int numberOfStudentsAllowed, String email, String image, String phoneNumber, String addressOrigin,
                String addressDestiny, String studentRegister, double valueForRent) {
        this.name = name;
        this.numberOfStudentsAllowed = numberOfStudentsAllowed;
        this.email = email;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.addressOrigin = addressOrigin;
        this.addressDestiny = addressDestiny;
        this.studentRegister = studentRegister;
        this.valueForRent = valueForRent;
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

    public int getNumberOfStudentsAllowed() {
        return numberOfStudentsAllowed;
    }

    public void setNumberOfStudentsAllowed(int numberOfStudentsAllowed) {
        this.numberOfStudentsAllowed = numberOfStudentsAllowed;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStudentRegister() {
        return studentRegister;
    }

    public void setStudentRegister(String studentRegister) {
        this.studentRegister = studentRegister;
    }

    public double getValueForRent() {
        return valueForRent;
    }

    public void setValueForRent(double valueForRent) {
        this.valueForRent = valueForRent;
    }
}
