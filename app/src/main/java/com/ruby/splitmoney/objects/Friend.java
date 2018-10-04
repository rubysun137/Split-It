package com.ruby.splitmoney.objects;

public class Friend {
    private String email;
    private String uid;
    private String name;

    public Friend(String email, String uid, String name) {
        this.email = email;
        this.uid = uid;
        this.name = name;
    }

    public Friend() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}