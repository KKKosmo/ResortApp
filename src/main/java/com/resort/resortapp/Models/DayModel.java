package com.resort.resortapp.Models;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.time.LocalDate;

public class DayModel {
    int gridSlot;
    boolean withinMonth;
    int gridDate;
    String notAvailable;
    StackPane stackPane;
    Text roomsText;
    Text dayText;
    public DayModel(int i, int j){
        int dateOffset = Model.getInstance().getDateOffset();
        gridSlot = (7*i)+(j+1);
        gridDate = gridSlot - dateOffset;
        withinMonth = gridSlot > dateOffset && gridDate <= Model.getInstance().getMonthMaxDate();
    }

    public boolean isWithinMonth() {
        return withinMonth;
    }

    public int getGridDate() {
        return gridDate;
    }

    public String getNotAvailable() {
        return notAvailable;
    }

    public void setNotAvailable(String notAvailable) {
        this.notAvailable = notAvailable;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public Text getRoomsText() {
        return roomsText;
    }

    public void setRoomsText(Text roomsText) {
        this.roomsText = roomsText;
    }

    public Text getDayText() {
        return dayText;
    }

    public void setDayText(Text dayText) {
        this.dayText = dayText;
    }


}