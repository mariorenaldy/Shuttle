package com.example.pppb51_tubes02_b_f;

public class CoursesInput {
    private String source;
    private String destination;
    private String vehicle;
    private String date;
    private String hour;

    public CoursesInput(String source, String destination, String vehicle, String date, String hour) {
        this.source = source;
        this.destination = destination;
        this.vehicle = vehicle;
        this.date = date;
        this.hour = hour;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
