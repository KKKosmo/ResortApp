package com.resort.resortapp.Models;

import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarModel {

    private int maxPax = 32;
    private StackPane leftStackPane;
    private StackPane rightStackPane;
    private LocalDate leftDate;
    private LocalDate rightDate;
    private int leftDateInt;
    private int rightDateInt;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Set<Integer> selected;

    public int getLeftDateInt() {
        return leftDateInt;
    }


    public int getRightDateInt() {
        return rightDateInt;
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
        this.leftDate = leftDate;
    }

    public LocalDate getRightDate() {
        return rightDate;
    }

    public void setRightDate(LocalDate rightDate) {
        this.rightDate = rightDate;
    }
    public void setSelected(){
        Set<Integer> result = new HashSet<>();


        if (leftDate != null && rightDate != null) {
            if(leftDate.isBefore(rightDate)){
                LocalDate tempDate = leftDate;
                while (tempDate.isBefore(rightDate) || tempDate.isEqual(rightDate)) {
                    int day = tempDate.getDayOfMonth();
                    if (day <= Model.getInstance().getMonthMaxDate())
                        result.add(day);
                    tempDate = tempDate.plusDays(1);
                }
            }
            else{
                LocalDate tempDate = rightDate;
                while (tempDate.isBefore(leftDate) || tempDate.isEqual(leftDate)) {
                    int day = tempDate.getDayOfMonth();
                    if (day <= Model.getInstance().getMonthMaxDate())
                        result.add(day);
                    tempDate = tempDate.plusDays(1);
                }
            }
        }


        selected = result;
    }

    public Set<Integer> getSelected() {
        return selected;
    }
}
