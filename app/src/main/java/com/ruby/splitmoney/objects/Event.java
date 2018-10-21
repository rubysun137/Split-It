package com.ruby.splitmoney.objects;

import java.util.Date;

public class Event {
    private String date;
    private String group;
    private String id;
    private double money;
    private String name;
    private Date time;
    private boolean settleUp;
    private String payBy;

    public Event(String name, String id, String group, double money, String date, Date time, boolean settleUp, String payBy) {
        this.date = date;
        this.group = group;
        this.id = id;
        this.money = money;
        this.name = name;
        this.time = time;
        this.settleUp = settleUp;
        this.payBy = payBy;
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

    public Date getTime() {
        return time;
    }

    public boolean isSettleUp() {
        return settleUp;
    }

    public String getPayBy() {
        return payBy;
    }
}