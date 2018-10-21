package com.ruby.splitmoney.objects;


public class Friend {
    private String email;
    private String uid;
    private String name;
    private Double money;
    private String image;

    public Friend(String email, String uid, String name, Double money, String image) {
        this.email = email;
        this.uid = uid;
        this.name = name;
        this.money = money;
        this.image = image;
    }

    public Friend() {
    }

    public String getImage() {
        return image;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setImage(String image) {
        this.image = image;
    }
}