package com.resort.resortapp.Views;

import com.resort.resortapp.Models.DayModel;
import com.resort.resortapp.Models.Rooms;
import com.resort.resortapp.Models.sqliteModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.time.ZonedDateTime;
import java.util.List;

public class ViewFactory {

    private Scene visualsMonth;
    private Scene login;
    private ZonedDateTime today;
    private Text yearText;
    private Text monthText;
    private Text roomText;
    private FlowPane flowPane;
    Rooms rooms = Rooms.ROOM_J;


    public void setCalendarVariables(FlowPane flowPane, Text yearText, Text monthText, Text roomText) {
        this.flowPane = flowPane;
        this.yearText = yearText;
        this.monthText = monthText;
        this.roomText = roomText;
        DayModel.setDateFocus();
    }

    public Scene getSceneVisualsMonth() {
        if (visualsMonth == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/VisualsMonth.fxml"));
                visualsMonth = new Scene(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return visualsMonth;
    }

    public Scene getSceneLogin() {
        if (login == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
                login = new Scene(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return login;
    }

    public void fillFlowPane(){
        DayModel.DayModelSetters();

        yearText.setText(String.valueOf(DayModel.getYear()));
        monthText.setText(String.valueOf(DayModel.getMonth()));
        roomText.setText(rooms.getDisplayName());

        double calendarWidth = flowPane.getPrefWidth();
        double calendarHeight = flowPane.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = flowPane.getHgap();
        double spacingV = flowPane.getVgap();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                DayModel date = new DayModel(i, j);

                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                if(date.isWithinMonth()){
                    Text dateText = new Text(String.valueOf(date.getGridDate()));
                    double dateTextTranslationY = - (rectangleHeight / 2) * 0.75;
                    dateText.setTranslateY(dateTextTranslationY);
                    stackPane.getChildren().add(dateText);
                    Text totalText = new Text("10");
                    double totalTextTranslationY =  rectangleHeight * 0.25;
                    totalText.setTranslateY(totalTextTranslationY);
                    stackPane.getChildren().add(totalText);
                }
                flowPane.getChildren().add(stackPane);
            }
        }

        List<Integer> slotsList = sqliteModel.getMonthSlots();

        for(int i = 0; i < DayModel.getMonthMaxDate(); i++){
            Text temp = (Text)((StackPane)flowPane.getChildren().get(i + DayModel.getDateOffset())).getChildren().get(2);
            temp.setText(slotsList.get(i).toString() + " Slots Free");
        }
    }



    public void nextMonth() {
        DayModel.nextMonth();
        flowPane.getChildren().clear();
        fillFlowPane();
    }

    public void prevMonth() {
        DayModel.prevMonth();
        flowPane.getChildren().clear();
        fillFlowPane();
    }
    public void prevRoom() {
        rooms = rooms.prev();
        flowPane.getChildren().clear();
        fillFlowPane();
    }
    public void nextRoom() {
        rooms = rooms.next();
        flowPane.getChildren().clear();
        fillFlowPane();
    }
}