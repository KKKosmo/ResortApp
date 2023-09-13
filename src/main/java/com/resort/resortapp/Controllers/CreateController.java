package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Rooms;
import com.resort.resortapp.Models.sqliteModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CreateController  implements Initializable{
    public RadioButton videokeYes_radio;
    public RadioButton videokeNo_radio;
    public RadioButton petsYes_radio;
    public RadioButton petsNo_radio;
    public RadioButton vehicleYes_radio;
    public RadioButton vehicleNo_radio;
    private String[] rooms = {
            Rooms.ROOM_G.getDisplayName(),
            Rooms.ROOM_J.getDisplayName(),
            Rooms.ATTIC.getDisplayName(),
            Rooms.KUBO_1.getDisplayName(),
            Rooms.KUBO_2.getDisplayName()
    };
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
        room_choiceBox.getItems().addAll(rooms);
        done_btn.setOnAction(actionEvent -> {
            insertRecord();
        });
        clr_btn.setOnAction(actionEvent -> {
            clearForm();
        });
        textFieldAddListener(pax_fld);
        textFieldAddListener(payment_fld);
    }

    private void insertRecord(){

        LocalDate currentDateLocalDate = currentDate_datePicker.getValue();
        String name = name_fld.getText();
        String paxString = pax_fld.getText();
        boolean vehicle = vehicleYes_radio.isSelected();
        boolean pets = petsYes_radio.isSelected();
        boolean videoke = videokeYes_radio.isSelected();
        String partial_paymentString = payment_fld.getText();
        LocalDate checkInLocalDate = checkIn_datePicker.getValue();
        LocalDate checkOutLocalDate = checkOut_datePicker.getValue();
        String roomUnformatted = room_choiceBox.getValue();


        if(currentDateLocalDate == null)
            System.out.println("current date empty");
        else if (name.isEmpty())
            System.out.println("name empty");
        else if (paxString.isEmpty())
            System.out.println("pax empty");
        else if(vehicleYes_radio.getToggleGroup().getSelectedToggle() == null)
            System.out.println("vehicle empty");
        else if(petsYes_radio.getToggleGroup().getSelectedToggle() == null)
            System.out.println("pets empty");
        else if(videokeYes_radio.getToggleGroup().getSelectedToggle() == null)
            System.out.println("videoke empty");
        else if(partial_paymentString.isEmpty())
            System.out.println("payment empty");
        else if(checkInLocalDate == null)
            System.out.println("checkin empty");
        else if(checkOutLocalDate == null)
            System.out.println("checkout empty");
        else if(roomUnformatted == null)
            System.out.println("room empty");
        else{
            String currentDate = currentDateLocalDate.toString();
            int paxInt = Integer.parseInt(paxString);
            double partial_paymentDouble = Double.parseDouble(partial_paymentString);
            String checkInString = checkIn_datePicker.getValue().toString();
            String checkOutString = checkOut_datePicker.getValue().toString();
            String roomFormatted = roomUnformatted.replace(" ", "_");
            roomFormatted = Rooms.valueOf(roomFormatted).getAbbreviatedName();
            sqliteModel.insertRecord(currentDate, name, paxInt, vehicle, pets, videoke, partial_paymentDouble, checkInString, checkOutString, roomFormatted);
//            System.out.println(currentDate);
//            System.out.println(name);
//            System.out.println(paxInt);
//            System.out.println(vehicle);
//            System.out.println(pets);
//            System.out.println(videoke);
//            System.out.println(partial_paymentDouble);
//            System.out.println(checkInString);
//            System.out.println(checkOutString);
//            System.out.println(roomFormatted);
//            System.out.println("CLEAR");
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
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("\\D", ""));
                }
            }
        });
    }
}