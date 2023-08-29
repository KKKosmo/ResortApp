package com.resort.resortapp.Views;

import java.time.ZonedDateTime;

public class Date {
    int total = 0;

    public Date(ZonedDateTime dateFocus, String day){
        String dateString;
        int year = dateFocus.getYear();
        int month = dateFocus.getMonthValue();

        String formattedMonth = (month < 10) ? "0" + month : String.valueOf(month);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year).append("-").append(formattedMonth).append("-").append(day);

        dateString = stringBuilder.toString();

        //if(sql.dateString.roomJ){total+=6}
    }
}