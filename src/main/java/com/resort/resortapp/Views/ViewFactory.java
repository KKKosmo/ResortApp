package com.resort.resortapp.Views;

import com.resort.resortapp.Controllers.EditController;
import com.resort.resortapp.Models.*;
import com.resort.resortapp.Rooms;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ViewFactory {
    private Scene create;
    private ZonedDateTime today;
    private Text yearText;
    private Text monthText;
    private Text roomText;
    private FlowPane flowPane;
    private Stage stage;

    public CalendarModel calendarModel = new CalendarModel();

        Border selectedBorder = new Border(
                new BorderStroke(
                        Color.BLUE,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(5), // You can adjust the corner radii as needed
                        new BorderWidths(2) // You can adjust the border width as needed
                )
        );

    public void setCalendarVariables(FlowPane flowPane, Text yearText, Text monthText, Text roomText) {
        this.flowPane = flowPane;
        this.yearText = yearText;
        this.monthText = monthText;
        this.roomText = roomText;
    }

    public void setSceneList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/List.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSceneLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/MainMenu.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void insertCalendar(Pane pane){
        try {
            AnchorPane childPane = FXMLLoader.load(getClass().getResource("/Fxml/Calendar.fxml"));
            pane.getChildren().setAll(childPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillFlowPaneMonths(Rooms rooms){
        flowPane.getChildren().clear();
        setCalendarGrid(rooms);

        if(rooms == Rooms.ALL_ROOMS){
            List<Integer> slotsList = sqliteModel.getMonthSlots();
            sqliteModel.getMonthAvailability();

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
    public int selectDays(){
        int result = 32;
        for (int i = calendarModel.getLeftDateInt() - 1; i < calendarModel.getRightDateInt(); i++){
            StackPane temp = (StackPane) flowPane.getChildren().get(i + Model.getDateOffset());
            temp.setBorder(selectedBorder);
            Text temp2 = (Text)(temp.getChildren().get(2));
            result = Math.min(result, Integer.parseInt(temp2.getText().substring(0, 2)));
        }
        System.out.println(result);
        return result;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void flowPaneSmall(){
        flowPane.setPrefHeight(330);
    }
    public CalendarModel getCalendarModel() {
        return calendarModel;
    }


    public void setSceneEdit(int id, LocalDate insertedDate, String name, String pax, boolean vehicle, boolean pets, boolean videoke, String payment, LocalDate checkIn, LocalDate checkOut, String room){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Edit.fxml"));
            Parent root = loader.load();

            EditController editController = loader.getController();
            editController.setValues(id, insertedDate, name, pax, vehicle, pets, videoke, payment, checkIn, checkOut, room);

            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertListRows(GridPane gridPane, List<List<String>> list){

        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(0).size(); j++){
                Label label = new Label();
                label.setAlignment(Pos.CENTER);
                label.setText(list.get(i).get(j));
                label.setTextAlignment(TextAlignment.CENTER);
                GridPane.setRowIndex(label, i);
                GridPane.setColumnIndex(label, j);
                gridPane.getChildren().add(label);
            }

            Button viewButton = new Button("View");
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            List<String> temp = list.get(i);
            editButton.setOnAction(actionEvent -> {
                setSceneEdit(
                    Integer.parseInt(temp.get(0)),
                    LocalDate.parse(temp.get(1)),
                    temp.get(2),
                    temp.get(3),
                    temp.get(4).equals("Yes"),
                    temp.get(5).equals("Yes"),
                    temp.get(6).equals("Yes"),
                    temp.get(7),
                    LocalDate.parse(temp.get(8)),
                    LocalDate.parse(temp.get(9)),
                    temp.get(10)
                    );
            });

            deleteButton.setOnAction(actionEvent -> {
                if(sqliteModel.deleteEntry(Integer.parseInt(temp.get(0)))){
//                    Node node = gridPane.getChildren().get(0);
//                    gridPane.getChildren().clear();
//                    gridPane.getChildren().add(0,node);
                    gridPane.getChildren().retainAll(gridPane.getChildren().get(0));
                    insertListRows(gridPane, sqliteModel.queryViewList());
                }
//                else {
                    //error window popup, idk the best way to implement it yet
//                }

            });

            GridPane.setColumnIndex(viewButton, 12);
            GridPane.setColumnIndex(editButton, 13);
            GridPane.setColumnIndex(deleteButton, 14);

            GridPane.setRowIndex(viewButton, i);
            GridPane.setRowIndex(editButton, i);
            GridPane.setRowIndex(deleteButton, i);

            gridPane.getChildren().addAll(viewButton, editButton, deleteButton);
        }
    }
}