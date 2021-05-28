package com.example.alertclock02.timeModel;

public class TimeID {

    int id;
    String time;

    public TimeID(int id, String time) {
        this.id = id;
        this.time = time;
    }
    public TimeID() {}

    public int getId() {

        return id;
    }
    public void setId(int id) {

        this.id = id;
    }

    public String getName() {

        return time;
    }

    public void setTime(String time) {

        this.time = time;
    }
}
