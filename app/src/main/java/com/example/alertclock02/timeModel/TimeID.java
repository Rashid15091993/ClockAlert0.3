package com.example.alertclock02.timeModel;

public class TimeID {

    int id;
    int hour;
    int minute;
    String time;

    public TimeID(int id, int hour, int minute, String time) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.time = time;

    }
    public TimeID() {}

    public int getId() {

        return id;
    }
    public void setId(int id) {

        this.id = id;
    }

    public int getHour() {

        return hour;
    }
    public void setHour(int hour) {

        this.hour = hour;
    }

    public int getMinute() {

        return minute;
    }
    public void setMinute(int minute) {

        this.minute = minute;
    }

    public String getName() {

        return time;
    }

    public void setTime(String time) {

        this.time = time;
    }
}
