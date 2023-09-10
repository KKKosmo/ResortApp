package com.resort.resortapp.Models;

import java.time.Month;
import java.time.ZonedDateTime;

public class DayModel {
    int gridSlot;
    boolean withinMonth;
    int gridDate;
    static int dateOffset;
    static int monthMaxDate;
    static int year;
    static int monthValue;
    static Month month;

    static ZonedDateTime dateFocus;

    public DayModel(int i, int j){
        gridSlot = (7*i)+(j+1);
        gridDate = gridSlot - dateOffset;
        withinMonth = gridSlot > dateOffset && gridDate <= monthMaxDate;
    }

    public static int getDateOffset() {
        return dateOffset;
    }

    public static void DayModelSetters(){
        year = dateFocus.getYear();
        monthValue = dateFocus.getMonthValue();
        month =  dateFocus.getMonth();
        monthMaxDate = month.maxLength();
        if(year % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        dateOffset = ZonedDateTime.of(year, monthValue, 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();
        if(dateOffset >= 7){
            dateOffset = 0;
        }
    }


    public static int getMonthMaxDate() {
        return monthMaxDate;
    }

    public boolean isWithinMonth() {
        return withinMonth;
    }

    public int getGridDate() {
        return gridDate;
    }


    public static int getYear() {
        return year;
    }

    public static Month getMonth() {
        return month;
    }

    public static ZonedDateTime getDateFocus() {
        return dateFocus;
    }

    public static void setDateFocus() {
        DayModel.dateFocus = ZonedDateTime.now();
    }

    public static void nextMonth() {
        dateFocus = dateFocus.plusMonths(1);
    }
    public static void prevMonth() {
        dateFocus = dateFocus.minusMonths(1);
    }
}