package com.example.lucaoliveira.caronauniversitaria.model;

/**
 * Created by lucaoliveira on 19/10/16.
 */

public class Notification {
    private String customer, thumbnailUrl;
    private int year;
    private String address;
    private String notificationText;

    public Notification() {
    }

    public Notification(String customer, String thumbnailUrl, int year, String address,
                 String notificationText) {
        this.customer = customer;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.address = address;
        this.notificationText = notificationText;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }
}
