package com.example.pppb51_tubes02_b_f;

import android.graphics.RectF;

public class Seat {
    private String number;
    private RectF rectangle;
    private boolean selected;
    private boolean selectable;

    public Seat(String number, RectF rectangle){
        this.number = number;
        this.rectangle = rectangle;
        selected = false;
        selectable = true;
    }
    public Seat(String number, RectF rectangle, boolean selectable){
        this.number = number;
        this.rectangle = rectangle;
        selected = false;
        this.selectable = selectable;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RectF getRectangle() {
        return rectangle;
    }

    public void setRectangle(RectF rectangle) {
        this.rectangle = rectangle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
