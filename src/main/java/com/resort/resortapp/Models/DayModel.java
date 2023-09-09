package com.resort.resortapp.Models;

import java.time.ZonedDateTime;
import java.util.Random;

public class DayModel {
    int total = 0;
    int gridSlot;
    int dateOffset;
    boolean withinMonth;
    int gridDate;

    public DayModel(ZonedDateTime dateFocus, int i, int j, int year, int monthMaxDate, int monthValue){
        gridSlot = (7*i)+(j+1);
        dateOffset = ZonedDateTime.of(year, monthValue, 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();
        if(dateOffset >= 7){
            dateOffset = 0;
        }

        //Check for leap year
        if(year % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }


        gridDate = gridSlot - dateOffset;
        withinMonth = gridSlot > dateOffset && gridDate <= monthMaxDate;

        String dateString;


        String formattedMonth = (monthValue < 10) ? "0" + monthValue : String.valueOf(monthValue);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year).append("-").append(formattedMonth).append("-").append(gridSlot);

        dateString = stringBuilder.toString();

        //if(sql.dateString.roomJ){total+=6}

    }

    public int getDateOffset() {
        return dateOffset;
    }

    public boolean isWithinMonth() {
        return withinMonth;
    }

    public int getGridDate() {
        return gridDate;
    }
}