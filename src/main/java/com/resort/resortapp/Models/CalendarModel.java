package com.resort.resortapp.Models;

import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarModel {

    private int maxPax = 32;
    private StackPane leftStackPane;
    private StackPane rightStackPane;
    private LocalDate leftDate;
    private LocalDate rightDate;
    private int leftDateInt;
    private int rightDateInt;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public int getLeftDateInt() {
        return leftDateInt;
    }

    public void setLeftDateInt(int leftDateInt) {
        this.leftDateInt = leftDateInt;
    }

    public int getRightDateInt() {
        return rightDateInt;
    }

    public void setRightDateInt(int rightDateInt) {
        this.rightDateInt = rightDateInt;
    }

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
//        leftDate = LocalDate.parse(leftDate.toString(), formatter);
        String day = leftDate.toString().substring(leftDate.toString().length() - 2);
        setLeftDateInt(Integer.parseInt(day));
        this.leftDate = leftDate;
    }

    public LocalDate getRightDate() {
        return rightDate;
    }

    public void setRightDate(LocalDate rightDate) {
//        rightDate = LocalDate.parse(rightDate.toString(), formatter);
        String day = rightDate.toString().substring(rightDate.toString().length() - 2);
        setRightDateInt(Integer.parseInt(day));
        this.rightDate = rightDate;
    }
}
