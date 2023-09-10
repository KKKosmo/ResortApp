package com.resort.resortapp.Models;

import com.resort.resortapp.Views.ViewFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    Rooms rooms = Rooms.ALL_ROOMS;
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
        DayModel.setDateFocus();
    }
    public void fillFlowPaneMonths(){
        DayModel.DayModelSetters();
        viewFactory.fillFlowPaneMonths(rooms);
    }
    public void fillFlowPaneRooms(){
        viewFactory.fillFlowPaneRooms(rooms);
    }
    public void nextMonth() {
        DayModel.nextMonth();
        viewFactory.clearFlowPane();
        fillFlowPaneMonths();
    }

    public void prevMonth() {
        DayModel.prevMonth();
        viewFactory.clearFlowPane();
        fillFlowPaneMonths();
    }
    public void nextRoom() {
        rooms = rooms.next();
        viewFactory.clearFlowPane();
        if(rooms == Rooms.ALL_ROOMS){
            fillFlowPaneMonths();
        }
        else{
            fillFlowPaneRooms();
        }
    }
    public void prevRoom() {
        rooms = rooms.prev();
        viewFactory.clearFlowPane();
        if(rooms == Rooms.ALL_ROOMS){
            fillFlowPaneMonths();
        }
        else{
            fillFlowPaneRooms();
        }
    }
}