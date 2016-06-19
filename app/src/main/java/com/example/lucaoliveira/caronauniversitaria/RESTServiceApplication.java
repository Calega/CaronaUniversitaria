package com.example.lucaoliveira.caronauniversitaria;

import android.app.Application;

import com.example.lucaoliveira.caronauniversitaria.data.User;

/**
 * Created by lucaoliveira on 6/19/2016.
 */
public class RESTServiceApplication extends Application {
    private static RESTServiceApplication instance;
    private User user;
    private String accessToken;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        user = new User();
    }

    public static RESTServiceApplication getInstance() {
        return instance;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
