package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import com.resort.resortapp.Rooms;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

public class EditController implements Initializable {
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
    public FlowPane month_pane;
    public Button burger_btn;
    public AnchorPane parentPane;
    private Set<String> available;
    private int id;


    public AnchorPane escMenu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
        burger_btn.setOnAction(actionEvent -> {
            escMenu.setVisible(true);
        });
        String[] rooms = {
                Rooms.ROOM_G.getDisplayName(),
                Rooms.ROOM_J.getDisplayName(),
                Rooms.ATTIC.getDisplayName(),
                Rooms.KUBO_1.getDisplayName(),
                Rooms.KUBO_2.getDisplayName()
        };
        room_choiceBox.getItems().addAll(rooms);

        done_btn.setOnAction(actionEvent -> {
            available = sqliteModel.getAvailableRoomsPerDayList(checkIn_datePicker.getValue(), checkOut_datePicker.getValue(), id);
            if(sqliteModel.updateRecord(id, name_fld, pax_fld, vehicleYes_radio, petsYes_radio, videokeYes_radio, payment_fld, checkIn_datePicker, checkOut_datePicker, room_choiceBox, available)){
                Model.getInstance().getViewFactory().setSceneList();
            }
        });
    }

    public void setAvailable(Set<String> available) {
        this.available = available;
    }

    public void setValues(int id, String name, String pax, boolean vehicle, boolean pets, boolean videoke, String payment, LocalDate checkIn, LocalDate checkOut, String room) {
        this.id = id;
        name_fld.setText(name);
        pax_fld.setText(pax);
        (vehicle ? vehicleYes_radio : vehicleNo_radio).setSelected(true);
        (pets ? petsYes_radio : petsNo_radio).setSelected(true);
        (videoke ? videokeYes_radio : videokeNo_radio).setSelected(true);
        payment_fld.setText(payment);
        checkIn_datePicker.setValue(checkIn);
        checkOut_datePicker.setValue(checkOut);
        room_choiceBox.setValue(Rooms.abbvToDisplay(room));
    }
}
