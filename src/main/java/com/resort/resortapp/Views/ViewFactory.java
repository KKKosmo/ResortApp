package com.resort.resortapp.Views;

import com.resort.resortapp.Controllers.EditController;
import com.resort.resortapp.Controllers.ViewController;
import com.resort.resortapp.Models.*;
import com.resort.resortapp.Rooms;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

public class ViewFactory {
    private AnchorPane escMenu;
    private ZonedDateTime today;
    private Text yearText;
    private Text monthText;
    private Text roomText;
    private FlowPane flowPane;
    private Stage stage;
    List<DayModel> dayModelList = new ArrayList<>();
    List<Set<String>> availableRoomsPerDayList;

//    TODO listview settings
    Border borderUnselected = new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, null, new BorderWidths(3)));
    Border normalBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(5)));
    public void setCalendarVariables(FlowPane flowPane, Text yearText, Text monthText, Text roomText) {
        this.flowPane = flowPane;
        this.yearText = yearText;
        this.monthText = monthText;
        this.roomText = roomText;
    }
    public AnchorPane getEscMenu(Pane parentPane){
        if (escMenu == null) {
            try {
                escMenu = FXMLLoader.load(getClass().getResource("/Fxml/EscMenu.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        parentPane.getChildren().add(escMenu);
        return escMenu;
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
//        if (create == null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Create.fxml"));
//                create = new Scene(loader.load());
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }
    public void setSceneMainMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/MainMenu.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setSceneSummary(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Summary.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertCalendar(Pane pane){
        try {
            AnchorPane childPane = FXMLLoader.load(getClass().getResource("/Fxml/Calendar.fxml"));
            pane.getChildren().add(childPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillFlowPaneMonths(Rooms rooms){
        flowPane.getChildren().clear();
        //TODO clear daymodel list here?
        setCalendarGrid(rooms);

        int monthMaxDate = Model.getInstance().getMonthMaxDate();
        int dateOffset = Model.getInstance().getDateOffset();
        if(rooms == Rooms.ALL_ROOMS){
            availableRoomsPerDayList = sqliteModel.getAvailableRoomsPerDayList();

            for(int i = 0; i < monthMaxDate; i++){
                Text temp = dayModelList.get(i + dateOffset).getRoomsText();
                StringBuilder desc = new StringBuilder();
                Set<String> set = availableRoomsPerDayList.get(i);
                for(String string : set){
                    desc.append(string).append("\n");
                }

                temp.setText(desc.toString());
                temp.setTextAlignment(TextAlignment.CENTER);
            }
        }
        else{
            List<String> slotsList = sqliteModel.getMonthSlots(rooms);

            for(int i = 0; i < monthMaxDate; i++){
                Text temp = dayModelList.get(i + dateOffset).getRoomsText();
                temp.setText(slotsList.get(i));
            }
        }
    }
    public void setClickable(){
        for(int i = 0; i < Model.getInstance().getMonthMaxDate(); i++){
            StackPane temp = dayModelList.get(i + Model.getInstance().getDateOffset()).getStackPane();
            temp.setOnMouseClicked(event -> {
                Text temp2 = (Text)(temp.getChildren().get(2));
                System.out.println(temp2.getText());
                temp.setStyle("-fx-background-color: red;");
            });
        }
    }
    public void colorize(Set<String> roomsCheckBoxes){
        if(roomsCheckBoxes.isEmpty()){
            for(int i = 0; i < Model.getInstance().getMonthMaxDate(); i++){
                StackPane temp = dayModelList.get(i + Model.getInstance().getDateOffset()).getStackPane();
                temp.setStyle("-fx-background-color: transparent;");
            }
        }
        else{
            for(int i = 0; i < Model.getInstance().getMonthMaxDate(); i++){
                StackPane temp = dayModelList.get(i + Model.getInstance().getDateOffset()).getStackPane();
                boolean available = true;
                for (String room : roomsCheckBoxes) {
                    if(!availableRoomsPerDayList.get(i).contains(room)){
                        available = false;
                        break;
                    }
                }
                if(available){
                    temp.setStyle("-fx-background-color: green;");
                }
                else{
                    temp.setStyle("-fx-background-color: red;");
                }
            }
        }
    }
    public void highlight(){
        for(int i = 0; i < 42; i++){
            StackPane temp = dayModelList.get(i).getStackPane();
            if(Model.getInstance().getSelected().contains(i - Model.getInstance().getDateOffset() + 1)){
                temp.setBorder(normalBorder);
            }
            else{
                temp.setBorder(borderUnselected);
            }
        }
    }
    public void clear(){
        for(int i = 0; i < 42; i++){
            StackPane temp = dayModelList.get(i).getStackPane();
            temp.setBorder(normalBorder);
            temp.setStyle("-fx-background-color: white;");
        }
    }
    private void setCalendarGrid(Rooms rooms){
        yearText.setText(String.valueOf(Model.getInstance().getYear()));
        monthText.setText(String.valueOf(Model.getInstance().getMonth()));
        roomText.setText(rooms.getDisplayName());

        double calendarWidth = flowPane.getPrefWidth();
        double calendarHeight = flowPane.getPrefHeight();
        double strokeWidth = 5;
        double spacingH = flowPane.getHgap();
        double spacingV = flowPane.getVgap();

        double boxWidth = (calendarWidth/7) - strokeWidth - 10 - spacingH;
        double boxHeight = (calendarHeight/6) - strokeWidth - 10 - spacingV;

        dayModelList.clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                DayModel dayModel = new DayModel(i, j);

                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.TRANSPARENT);
//                rectangle.setStrokeWidth(strokeWidth);
                rectangle.setWidth(boxWidth);
                rectangle.setHeight(boxHeight);
                stackPane.getChildren().add(rectangle);

                if(dayModel.isWithinMonth()){
                    Text dateText = new Text(String.valueOf(dayModel.getGridDate()));
                    dateText.setTranslateY(-(boxHeight / 2) * 0.75);
                    dateText.setTranslateX((boxWidth / 2) * 0.75);
                    dayModel.setDayText(dateText);

                    stackPane.getChildren().add(dateText);
                    Text totalText = new Text("");
                    totalText.setTranslateY(boxHeight * 0.1);
                    stackPane.getChildren().add(totalText);
                    dayModel.setRoomsText(totalText);
                }
                stackPane.setBorder(normalBorder);
                flowPane.getChildren().add(stackPane);
                dayModel.setStackPane(stackPane);
                dayModelList.add(dayModel);
            }
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void flowPaneSmall(){
        flowPane.setPrefHeight(330);
    }
    public void setSceneEdit(int id, String name, String pax, boolean vehicle, boolean pets, boolean videoke, String payment, LocalDate checkIn, LocalDate checkOut, String room){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Edit.fxml"));
            Parent root = loader.load();

            EditController editController = loader.getController();
            editController.setValues(id, name, pax, vehicle, pets, videoke, payment, checkIn, checkOut, room);

            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setSceneView(String insertedDate, String name, String pax, String vehicle, String pets, String videoke, String payment, String checkIn, String checkOut, String room){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/View.fxml"));
            Parent root = loader.load();

            ViewController viewController = loader.getController();
            viewController.setValues(insertedDate, name, pax, vehicle, pets, videoke, payment, checkIn, checkOut, room);

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
                if(showConfirmPopup("Are you sure you want to delete this row?")){
                    if(sqliteModel.deleteEntry(Integer.parseInt(temp.get(0)))){
                        gridPane.getChildren().retainAll(gridPane.getChildren().get(0));
                        insertListRows(gridPane, sqliteModel.queryViewList());
                    }
                }
            });


            viewButton.setOnAction(actionEvent ->{
                setSceneView(
                        temp.get(1),
                        temp.get(2),
                        temp.get(3),
                        temp.get(4),
                        temp.get(5),
                        temp.get(6),
                        temp.get(7),
                        temp.get(8),
                        temp.get(9),
                        temp.get(10)
                );
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
    public boolean showDialog(Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Alert!");
        alert.setContentText("This is an alert");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isEmpty()){
            return false;
        } else if(result.get() == ButtonType.OK){
            return true;
        } else if(result.get() == ButtonType.CANCEL){
            return false;
        }
        return false;
    }
    public void showSuccessPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public boolean showConfirmPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isEmpty()){
            return false;
        } else return result.get() == ButtonType.OK;
    }
}