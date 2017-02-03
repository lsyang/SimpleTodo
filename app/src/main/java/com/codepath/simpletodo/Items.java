package com.codepath.simpletodo;

public class Items {
    public Long _id; // for cupboard
    public String body;
    public int priority;


    public Items() {
        this.body = "";
        this.priority = 0;
    }

    public Items(String body) {
        this.body = body;
        this.priority = 0;
    }

    public Items(String body, int priority) {
        this.body = body;
        this.priority = priority;
    }

    public void setBody(String body){
        this.body = body;
    }

}
