package com.resort.resortapp.Models;

import javafx.scene.layout.StackPane;

import java.time.LocalDate;

public class CalendarModel {

    private int maxPax = 32;
    private StackPane leftStackPane;
    private StackPane rightStackPane;
    private LocalDate leftDate;
    private LocalDate rightDate;

    public int getMaxPax() {
        return maxPax;
    }

    public void setMaxPax(int maxPax) {
        this.maxPax = maxPax;
    }

    public StackPane getLeftStackPane() {
        return leftStackPane;
    }

    public void setLeftStackPane(StackPane leftStackPane) {
        this.leftStackPane = leftStackPane;
    }

    public StackPane getRightStackPane() {
        return rightStackPane;
    }

    public void setRightStackPane(StackPane rightStackPane) {
        this.rightStackPane = rightStackPane;
    }

    public LocalDate getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(LocalDate leftDate) {
        this.leftDate = leftDate;
    }

    public LocalDate getRightDate() {
        return rightDate;
    }

    public void setRightDate(LocalDate rightDate) {
        this.rightDate = rightDate;
    }
}
