package com.resort.resortapp.Views;

import com.resort.resortapp.Models.DateModel;
import com.resort.resortapp.SqliteConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.ZonedDateTime;

public class ViewFactory {

    private Scene visualsMonth;
    private Scene login;

    private ZonedDateTime dateFocus;
    private ZonedDateTime today;
    private Text yearText;
    private Text monthText;
    private FlowPane flowPane;

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

                DateModel date = new DateModel(dateFocus, i, j, year, monthMaxDate, monthValue);

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
                    Text totalText = new Text(String.valueOf(date.getTotal()) + " Slots");
                    double totalTextTranslationY =  rectangleHeight * 0.25;
                    totalText.setTranslateY(totalTextTranslationY);
                    stackPane.getChildren().add(totalText);
                }
                flowPane.getChildren().add(stackPane);
            }
        }

//        Text temp = (Text)((StackPane)flowPane.getChildren().get(10)).getChildren().get(2);
//        System.out.println(temp);
//        temp.setText("test");
//        System.out.println(temp);


        String sql = "SELECT * FROM main";
        try {
            PreparedStatement pStmt = SqliteConnection.openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int pax = resultSet.getInt("pax");
                boolean vehicle = resultSet.getBoolean("vehicle");
                boolean pets = resultSet.getBoolean("pets");
                boolean videoke = resultSet.getBoolean("videoke");
                double partial_payment = resultSet.getDouble("partial_payment");
                java.util.Date checkInDate;
                java.util.Date checkOutDate;
                String checkInString;
                String checkOutString;
                String room = resultSet.getString("room");
                try {
                    checkInDate = dateFormat.parse(resultSet.getString("checkIn"));
                    checkOutDate = dateFormat.parse(resultSet.getString("checkOut"));
                    checkInString = dateFormat.format(checkInDate);
                    checkOutString = dateFormat.format(checkOutDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                System.out.println();
                System.out.println(id + ", " + name + ", " + pax + ", " + vehicle + ", " +
                        pets + ", " + videoke + ", " + partial_payment + ", "
                        + checkInString + ", " + checkOutString + ", " + room);

            }
            resultSet.close();
            SqliteConnection.closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
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