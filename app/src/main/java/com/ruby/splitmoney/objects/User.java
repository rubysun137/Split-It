package com.ruby.splitmoney.objects;

import java.util.ArrayList;

public class User {
    private String email;
    private String name;
    private String uid;
    private String image;

    public User(String email, String name, String uid, String image) {
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.image = image;
    }


    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getImage() {
        return image;
    }
}