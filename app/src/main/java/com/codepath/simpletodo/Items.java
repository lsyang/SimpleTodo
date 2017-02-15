package com.codepath.simpletodo;

import java.util.Calendar;

public class Items {
    public Long _id; // for cupboard
    public String body;
    public int year;
    public int month;
    public int day;
    public int priority;


    public Items() {
        this.body = "";
        this.priority = 0;
    }

    public Items(String body) {
        this.body = body;
        this.priority = 0;
        setToCurrentDate();
    }

    public Items(String body, int priority) {
        this.body = body;
        this.priority = priority;
        setToCurrentDate();
    }

    public Items(String body, int priority, int year, int month, int day) {
        this.body = body;
        this.priority = priority;
        setDate(year, month, day);
    }

    public void setBody(String body){
        this.body = body;
    }

    public void setDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setToCurrentDate(){
        final Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
