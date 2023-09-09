package com.resort.resortapp.Views;

import com.resort.resortapp.Models.DayModel;
import com.resort.resortapp.Models.sqliteModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.List;

public class ViewFactory {

    private Scene visualsMonth;
    private Scene login;

    private ZonedDateTime dateFocus;
    private ZonedDateTime today;
    private Text yearText;
    private Text monthText;
    private FlowPane flowPane;
    private List<Integer> slotsList;

    public void setCalendarVariables(FlowPane flowPane, Text yearText, Text monthText) {
        this.flowPane = flowPane;
        this.yearText = yearText;
        this.monthText = monthText;
        dateFocus = ZonedDateTime.now();
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
        int year = dateFocus.getYear();
        Month month = dateFocus.getMonth();
        int monthValue = dateFocus.getMonthValue();
        int monthMaxDate = month.maxLength();

        yearText.setText(String.valueOf(year));
        monthText.setText(String.valueOf(month));

        double calendarWidth = flowPane.getPrefWidth();
        double calendarHeight = flowPane.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = flowPane.getHgap();
        double spacingV = flowPane.getVgap();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {

                DayModel date = new DayModel(dateFocus, i, j, year, monthMaxDate, monthValue);

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
        slotsList =  sqliteModel.getMonthSlots(dateFocus, monthMaxDate, year, monthValue);

        for(int i = 0; i < monthMaxDate; i++){
            Text temp = (Text)((StackPane)flowPane.getChildren().get(i + slotsList.get(0))).getChildren().get(2);
            temp.setText(slotsList.get(1 + i).toString() + " Slots Free");
        }
    }



    public void nextMonth() {
        dateFocus = dateFocus.plusMonths(1);
        flowPane.getChildren().clear();
        fillFlowPane();
    }

    public void prevMonth() {
        dateFocus = dateFocus.minusMonths(1);
        flowPane.getChildren().clear();
        fillFlowPane();
    }
}