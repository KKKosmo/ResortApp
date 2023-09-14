package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.CalendarModel;
import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Rooms;
import com.resort.resortapp.Models.sqliteModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CreateController  implements Initializable{
    public RadioButton videokeYes_radio;
    public RadioButton videokeNo_radio;
    public RadioButton petsYes_radio;
    public RadioButton petsNo_radio;
    public RadioButton vehicleYes_radio;
    public RadioButton vehicleNo_radio;
    public Button back_btn;
    public AnchorPane month_anchorPane;
    public Button done_btn;
    public Button clr_btn;
    public TextField name_fld;
    public TextField pax_fld;
    public TextField payment_fld;
    public ChoiceBox<String> room_choiceBox;
    public DatePicker currentDate_datePicker;
    public DatePicker checkOut_datePicker;
    public DatePicker checkIn_datePicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        currentDate_datePicker.setValue(LocalDate);

        currentDate_datePicker.setValue(LocalDate.now());


        String[] rooms = {
                Rooms.ROOM_G.getDisplayName(),
                Rooms.ROOM_J.getDisplayName(),
                Rooms.ATTIC.getDisplayName(),
                Rooms.KUBO_1.getDisplayName(),
                Rooms.KUBO_2.getDisplayName()
        };
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
        datePickerAddListener(checkIn_datePicker);

        Model.getInstance().getViewFactory().insertCalendar(month_anchorPane);
        Model.getInstance().getViewFactory().setClickable();
    }
    private void insertRecord(){
        if(sqliteModel.insertRecord(currentDate_datePicker, name_fld, pax_fld, vehicleYes_radio, petsYes_radio, videokeYes_radio, payment_fld, checkIn_datePicker, checkOut_datePicker, room_choiceBox)){
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

    private void datePickerAddListener(DatePicker datePicker){
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            String test = newValue.toString().substring(newValue.toString().length() - 2);

            Model.getInstance().getViewFactory().turnGlowing(Integer.parseInt(test));



//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate localDate = LocalDate.parse(newValue.toString(), formatter);
//            if (localDate.isAfter(Model.getInstance().getViewFactory().getCalendarModel().getLeftDate())) {
//                System.out.println("IS GLOWING");
//            }
        });
    }
}