package com.ruby.splitmoney.objects;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private String id;
    private ArrayList<String> members;
    private ArrayList<String> events;

    public Group(String name, String id, ArrayList<String> members, ArrayList<String> events) {
        this.name = name;
        this.id = id;
        this.members = members;
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public ArrayList<String> getEvents() {
        return events;
    }
}
