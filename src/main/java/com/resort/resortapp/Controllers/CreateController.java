package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Rooms;
import com.resort.resortapp.Models.sqliteModel;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CreateController  implements Initializable{
    public ChoiceBox<String> room_choiceBox;
    public DatePicker currentDate_datePicker;
    public DatePicker checkOut_datePicker;
    public DatePicker checkIn_datePicker;
    private String[] rooms = {
            Rooms.ROOM_G.getDisplayName(),
            Rooms.ROOM_J.getDisplayName(),
            Rooms.ATTIC.getDisplayName(),
            Rooms.KUBO_1.getDisplayName(),
            Rooms.KUBO_2.getDisplayName()
    };
    public CheckBox videoke_chkBox;
    public CheckBox pets_chkBox;
    public CheckBox vehicle_chkBox;
    public Button done_btn;
    public Button clr_btn;
    public TextField payment_fld;
    public TextField pax_fld;
    public TextField name_fld;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        currentDate_datePicker.setValue(LocalDate);

        room_choiceBox.getItems().addAll(rooms);
        done_btn.setOnAction(actionEvent -> {
            insertRecord();
        });
    }


    private void insertRecord(){
        String name = name_fld.getText();
        int pax = Integer.parseInt(pax_fld.getText());
        boolean vehicle = vehicle_chkBox.isSelected();
        boolean pets = pets_chkBox.isSelected();
        boolean videoke = videoke_chkBox.isSelected();
        double partial_payment = Double.parseDouble(payment_fld.getText());
        String checkIn = checkIn_datePicker.getValue().toString();
        String checkOut = checkOut_datePicker.getValue().toString();
        String room = room_choiceBox.getValue().replace(" ", "_");
        room = Rooms.valueOf(room).getAbbreviatedName();

        sqliteModel.insertRecord(name, pax, vehicle, pets, videoke, partial_payment, checkIn, checkOut, room);
    }

}
