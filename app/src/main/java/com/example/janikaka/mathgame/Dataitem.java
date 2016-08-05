package com.example.janikaka.mathgame;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by janikaka on 5.8.2016.
 */
public class Dataitem {
    private String key;
    private int value;
    private String startDatetime;
    private String endDatetime;
    private String username;
    private DateFormat dateFormat;

    public Dataitem(String key, String user) {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.startDatetime = dateFormat.format(new Date());
        this.username = user;
        this.key = key;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setEndDatetime() {
        this.endDatetime = dateFormat.format(new Date());
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public String getUsername() {
        return username;
    }
}

