package com.resort.resortapp.Models;

import com.resort.resortapp.Views.ViewFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.time.Month;
import java.time.ZonedDateTime;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    Rooms rooms = Rooms.ALL_ROOMS;
    static int dateOffset;
    static int monthMaxDate;
    static int year;
    static int monthValue;
    static Month month;
    static ZonedDateTime dateFocus;


    private Model(){
        this.viewFactory = new ViewFactory();
    }
    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }
    public ViewFactory getViewFactory(){
        return  viewFactory;
    }
    public void setCalendarVariables(FlowPane flowPane, Text year, Text month, Text roomText) {
        viewFactory.setCalendarVariables(flowPane, year, month, roomText);
        setDateFocus();
    }
    public void fillFlowPaneMonths(){
        DayModelSetters();
        viewFactory.fillFlowPaneMonths(rooms);
    }
    public void nextMonth() {
        dateFocus = dateFocus.plusMonths(1);
        fillFlowPaneMonths();
    }
    public void prevMonth() {
        dateFocus = dateFocus.minusMonths(1);
        fillFlowPaneMonths();
    }
    public void nextRoom() {
        rooms = rooms.next();
        fillFlowPaneMonths();
    }
    public void prevRoom() {
        rooms = rooms.prev();
        fillFlowPaneMonths();
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
        dateFocus = ZonedDateTime.now();
    }
}