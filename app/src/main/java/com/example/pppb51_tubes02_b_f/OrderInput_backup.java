package com.example.pppb51_tubes02_b_f;

public class OrderInput_backup {
    private String course_id;
    private String seats;

    public OrderInput_backup(String course_id, String seats) {
        this.course_id = course_id;
        this.seats = seats;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }
}
