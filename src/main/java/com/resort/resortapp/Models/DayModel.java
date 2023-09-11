package com.resort.resortapp.Models;

public class DayModel {
    int gridSlot;
    boolean withinMonth;
    int gridDate;

    public DayModel(int i, int j){
        gridSlot = (7*i)+(j+1);
        gridDate = gridSlot - Model.getDateOffset();
        withinMonth = gridSlot > Model.getDateOffset() && gridDate <= Model.getMonthMaxDate();
    }

    public boolean isWithinMonth() {
        return withinMonth;
    }

    public int getGridDate() {
        return gridDate;
    }
}