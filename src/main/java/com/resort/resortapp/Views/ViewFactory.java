package com.resort.resortapp.Views;

import com.resort.resortapp.Models.*;
import com.resort.resortapp.Rooms;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

public class ViewFactory {

    private Scene mainMenu;
    private Scene create;
    private Scene login;
    private Scene calendar;
    private ZonedDateTime today;
    private Text yearText;
    private Text monthText;
    private Text roomText;
    private FlowPane flowPane;
    private Stage stage;

    public CalendarModel calendarModel = new CalendarModel();

    public void setCalendarVariables(FlowPane flowPane, Text yearText, Text monthText, Text roomText) {
        this.flowPane = flowPane;
        this.yearText = yearText;
        this.monthText = monthText;
        this.roomText = roomText;
    }


    public void setSceneLogin(){
        if (login == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
                login = new Scene(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stage.setScene(login);
    }
    public void setSceneCreate(){
        if (create == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Create.fxml"));
                create = new Scene(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stage.setScene(create);
    }
    public void setSceneMainMenu(){
        if (mainMenu == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/MainMenu.fxml"));
                mainMenu = new Scene(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stage.setScene(mainMenu);
    }
    public void insertCalendar(AnchorPane anchorPane){
        if (calendar == null) {
            try {
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/Fxml/Calendar.fxml"));
                anchorPane.getChildren().setAll(pane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void fillFlowPaneMonths(Rooms rooms){
        flowPane.getChildren().clear();
        setCalendarGrid(rooms);

        if(rooms == Rooms.ALL_ROOMS){
            List<Integer> slotsList = sqliteModel.getMonthSlots();

            for(int i = 0; i < Model.getMonthMaxDate(); i++){
                Text temp = (Text)((StackPane)flowPane.getChildren().get(i + Model.getDateOffset())).getChildren().get(2);
                temp.setText(slotsList.get(i).toString() + " Slots Free");
            }
        }
        else{
            List<String> slotsList = sqliteModel.getMonthSlots(rooms);

            for(int i = 0; i < Model.getMonthMaxDate(); i++){
                Text temp = (Text)((StackPane)flowPane.getChildren().get(i + Model.getDateOffset())).getChildren().get(2);
                temp.setText(slotsList.get(i));
            }
        }
    }
    public void setClickable(){
        for(int i = 0; i < Model.getMonthMaxDate(); i++){
            StackPane temp = (StackPane) flowPane.getChildren().get(i + Model.getDateOffset());
            temp.setOnMouseClicked(event -> {
                Text temp2 = (Text)(temp.getChildren().get(2));
                System.out.println(temp2.getText());
            });
        }
    }
    private void setCalendarGrid(Rooms rooms){
        yearText.setText(String.valueOf(Model.getYear()));
        monthText.setText(String.valueOf(Model.getMonth()));
        roomText.setText(rooms.getDisplayName());

        double calendarWidth = flowPane.getPrefWidth();
        double calendarHeight = flowPane.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = flowPane.getHgap();
        double spacingV = flowPane.getVgap();

        double boxWidth = (calendarWidth/7) - strokeWidth - spacingH;
        double boxHeight = (calendarHeight/6) - strokeWidth - spacingV;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                DayModel dayModel = new DayModel(i, j);

                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                rectangle.setWidth(boxWidth);
                rectangle.setHeight(boxHeight);
                stackPane.getChildren().add(rectangle);

                if(dayModel.isWithinMonth()){
                    Text dateText = new Text(String.valueOf(dayModel.getGridDate()));
                    double dateTextTranslationY = - (boxHeight / 2) * 0.75;
                    dateText.setTranslateY(dateTextTranslationY);
                    stackPane.getChildren().add(dateText);
                    Text totalText = new Text("AVAILABLE");
                    double totalTextTranslationY =  boxHeight * 0.25;
                    totalText.setTranslateY(totalTextTranslationY);
                    stackPane.getChildren().add(totalText);
                }
                flowPane.getChildren().add(stackPane);
            }
        }
    }

    public void turnGlowing(int day){
        StackPane temp = (StackPane) flowPane.getChildren().get(day - 1 + Model.getDateOffset());
        Border redBorder = new Border(
                new BorderStroke(
                        Color.RED,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(5), // You can adjust the corner radii as needed
                        new BorderWidths(2) // You can adjust the border width as needed
                )
        );

        temp.setBorder(redBorder);
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public CalendarModel getCalendarModel() {
        return calendarModel;
    }
}