package com.ruby.splitmoney.objects;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private String id;
    private List<String> members;
    private List<String> events;

    public Group(String name, String id, List<String> members, List<String> events) {
        this.name = name;
        this.id = id;
        this.members = members;
        this.events = events;
    }

    public Group() {
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getMembers() {
        return members;
    }

    public List<String> getEvents() {
        return events;
    }
}
