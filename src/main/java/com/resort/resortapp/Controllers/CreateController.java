package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import com.resort.resortapp.Rooms;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class CreateController  implements Initializable{
    public RadioButton videokeYes_radio;
    public RadioButton videokeNo_radio;
    public RadioButton petsYes_radio;
    public RadioButton petsNo_radio;
    public RadioButton vehicleYes_radio;
    public RadioButton vehicleNo_radio;
    public Button back_btn;
    public Button done_btn;
    public Button clr_btn;
    public TextField name_fld;
    public TextField pax_fld;
    public TextField payment_fld;
    public ChoiceBox<String> room_choiceBox;
    public DatePicker currentDate_datePicker;
    public DatePicker checkOut_datePicker;
    public DatePicker checkIn_datePicker;
    public FlowPane month_pane;
    Set<String> available;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentDate_datePicker.setValue(LocalDate.now());

        List<String> rooms = Rooms.getRoomDisplayNameList();

        room_choiceBox.getItems().addAll(rooms);
        done_btn.setOnAction(actionEvent -> {
            insertRecord();
        });
        clr_btn.setOnAction(actionEvent -> {
            clearForm();
        });

        back_btn.setOnAction(event ->
            {
                Model.getInstance().getViewFactory().setSceneMainMenu();
            });

        textFieldAddListener(pax_fld);
        textFieldAddListener(payment_fld);

        checkIn_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().getViewFactory().getCalendarModel().setLeftDate(newValue);
//            if(checkIn_datePicker.getValue() != null && checkOut_datePicker != null){
//                maxPax = Model.getInstance().getViewFactory().selectDays();
//            }
            if(checkIn_datePicker.getValue() != null && checkOut_datePicker.getValue() != null && (checkIn_datePicker.getValue().isBefore(checkOut_datePicker.getValue()) || checkIn_datePicker.getValue().equals(checkOut_datePicker.getValue()))){
                available = sqliteModel.getMonthAvailability(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());
            }
        });
        checkOut_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().getViewFactory().getCalendarModel().setRightDate(newValue);
//            if(checkIn_datePicker.getValue() != null && checkOut_datePicker != null){
//                maxPax = Model.getInstance().getViewFactory().selectDays();
//            }
            if(checkIn_datePicker.getValue() != null && checkOut_datePicker.getValue() != null && (checkIn_datePicker.getValue().isBefore(checkOut_datePicker.getValue()) || checkIn_datePicker.getValue().equals(checkOut_datePicker.getValue()))){
                available = sqliteModel.getMonthAvailability(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());
            }
        });


//        Model.getInstance().getViewFactory().flowPaneSmall();
        Model.getInstance().getViewFactory().insertCalendar(month_pane);
        Model.getInstance().getViewFactory().setClickable();
    }
    private void insertRecord(){
        if(sqliteModel.insertRecord(currentDate_datePicker, name_fld, pax_fld, vehicleYes_radio, petsYes_radio, videokeYes_radio, payment_fld, checkIn_datePicker, checkOut_datePicker, room_choiceBox, available)){
            Model.getInstance().getViewFactory().setSceneMainMenu();
        }
        else{
            //TODO error window
            System.out.println("ERROR");
        }

    }
    private void clearForm(){
        currentDate_datePicker.setValue(LocalDate.now());
        name_fld.clear();
        pax_fld.clear();
        videokeYes_radio.setSelected(false);
        videokeNo_radio.setSelected(false);
        petsYes_radio.setSelected(false);
        petsNo_radio.setSelected(false);
        vehicleYes_radio.setSelected(false);
        vehicleNo_radio.setSelected(false);
        payment_fld.clear();
        checkIn_datePicker.setValue(null);
        checkOut_datePicker.setValue(null);
        room_choiceBox.setValue(null);
    }
    private void textFieldAddListener(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }
}