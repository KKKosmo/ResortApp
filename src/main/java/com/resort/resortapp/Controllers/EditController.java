package com.resort.resortapp.Controllers;

import com.resort.resortapp.Rooms;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    public DatePicker currentDate_datePicker;
    public TextField name_fld;
    public TextField pax_fld;
    public RadioButton vehicleNo_radio;
    public RadioButton vehicleYes_radio;
    public RadioButton petsNo_radio;
    public RadioButton petsYes_radio;
    public RadioButton videokeNo_radio;
    public RadioButton videokeYes_radio;
    public TextField payment_fld;
    public DatePicker checkIn_datePicker;
    public DatePicker checkOut_datePicker;
    public ChoiceBox room_choiceBox;
    public Button done_btn;
    public Button back_btn;
    public FlowPane month_pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] rooms = {
                Rooms.ROOM_G.getDisplayName(),
                Rooms.ROOM_J.getDisplayName(),
                Rooms.ATTIC.getDisplayName(),
                Rooms.KUBO_1.getDisplayName(),
                Rooms.KUBO_2.getDisplayName()
        };
        room_choiceBox.getItems().addAll(rooms);
        done_btn.setOnAction(actionEvent -> {
//            insertRecord();
        });


        //viewFactory.edit(values)






    }

    public void setValues(LocalDate insertedDate, String name, String pax, boolean vehicle, boolean pets, boolean videoke, String payment, LocalDate checkIn, LocalDate checkOut, String room) {
        currentDate_datePicker.setValue(insertedDate);
        name_fld.setText(name);
        pax_fld.setText(pax);
        (vehicle ? vehicleYes_radio : vehicleNo_radio).setSelected(true);
        (pets ? petsYes_radio : petsNo_radio).setSelected(true);
        (videoke ? videokeYes_radio : videokeNo_radio).setSelected(true);
        payment_fld.setText(payment);
        checkIn_datePicker.setValue(checkIn);
        checkOut_datePicker.setValue(checkOut);
        room_choiceBox.setValue(room);
    }
}
