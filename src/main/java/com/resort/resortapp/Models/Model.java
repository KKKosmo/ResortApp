package com.resort.resortapp.Models;

import com.resort.resortapp.Views.ViewFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.time.ZonedDateTime;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;

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


    public void setCalendarVariables(FlowPane flowPane, Text year, Text month) {
        viewFactory.setCalendarVariables(flowPane, year, month);
    }
    public void fillFlowPane(){
        viewFactory.fillFlowPane();
    }
}