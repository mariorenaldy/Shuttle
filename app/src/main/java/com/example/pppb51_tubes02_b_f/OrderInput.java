package com.example.pppb51_tubes02_b_f;

public class OrderInput {
    private String source;
    private String destination;
    private String vehicle;
    private String date;
    private String hour;

    public OrderInput(String source, String destination, String vehicle, String date, String hour) {
        this.source = source;
        this.destination = destination;
        this.vehicle = vehicle;
        this.date = date;
        this.hour = hour;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }
}
