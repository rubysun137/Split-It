package com.ruby.splitmoney.objects;

public class Event {
    private String date;
    private String group;
    private String id;
    private double money;
    private String name;

    public Event(String name, String id, String group, double money, String date) {
        this.date = date;
        this.group = group;
        this.id = id;
        this.money = money;
        this.name = name;
    }

    public Event() {
    }

    public String getDate() {
        return date;
    }

    public String getGroup() {
        return group;
    }

    public String getId() {
        return id;
    }

    public double getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }
}