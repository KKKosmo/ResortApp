package com.resort.resortapp.Models;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DayModel {
    int gridSlot;
    boolean withinMonth;
    int gridDate;
    boolean j, g, attic, kubo1, kubo2;
    StackPane stackPane;
    int freeSlots;
    Text slotsText;
    String available;
    String notAvailable;

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