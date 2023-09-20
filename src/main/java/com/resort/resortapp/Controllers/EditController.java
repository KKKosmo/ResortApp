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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class EditController implements Initializable {
    public TextField name_fld;
    public TextField pax_fld;
    public RadioButton petsNo_radio;
    public RadioButton petsYes_radio;
    public RadioButton videokeNo_radio;
    public RadioButton videokeYes_radio;
    public TextField payment_fld;
    public DatePicker checkIn_datePicker;
    public DatePicker checkOut_datePicker;
    public Button done_btn;
    public FlowPane month_pane;
    public Button burger_btn;
    public AnchorPane parentPane;
    public CheckBox roomG_ChkBox;
    public CheckBox roomJ_ChkBox;
    public CheckBox kubo1_ChkBox;
    public CheckBox attic_ChkBox;
    public CheckBox kubo2_ChkBox;
    public TextField vehicle_textFld;
    private Set<String> available;
    private int id;


    public AnchorPane escMenu;
    List<CheckBox> roomCheckBoxes = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roomCheckBoxes.add(roomJ_ChkBox);
        roomCheckBoxes.add(roomG_ChkBox);
        roomCheckBoxes.add(attic_ChkBox);
        roomCheckBoxes.add(kubo1_ChkBox);
        roomCheckBoxes.add(kubo2_ChkBox);
        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
        burger_btn.setOnAction(actionEvent -> {
            escMenu.setVisible(true);
        });

        done_btn.setOnAction(actionEvent -> {
            updateRecord();
        });

        textFieldAddListener(pax_fld);
        textFieldAddListener(payment_fld);


        Model.getInstance().getViewFactory().insertCalendar(month_pane);
    }

    public void updateRecord(){
        //TODO AVAILABLE SHOULD BE SET IN DATEPICKER ON CHANGE
        if(sqliteModel.updateRecord(id, name_fld, pax_fld, vehicle_textFld, petsYes_radio, videokeYes_radio, payment_fld, checkIn_datePicker, checkOut_datePicker, roomCheckBoxes, available)){
            Model.getInstance().getViewFactory().setSceneList();
        }
    }

    private void textFieldAddListener(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }
    public void setAvailable(Set<String> available) {
        this.available = available;
    }

    public void setValues(int id, String name, String pax, String vehicle, boolean pets, boolean videoke, String payment, LocalDate checkIn, LocalDate checkOut, String room) {
        this.id = id;
        name_fld.setText(name);
        pax_fld.setText(pax);
        vehicle_textFld.setText(vehicle);
        (pets ? petsYes_radio : petsNo_radio).setSelected(true);
        (videoke ? videokeYes_radio : videokeNo_radio).setSelected(true);
        payment_fld.setText(String.valueOf(Math.round(Double.parseDouble(payment))));
        checkIn_datePicker.setValue(checkIn);
        checkOut_datePicker.setValue(checkOut);
        Rooms.tickCheckboxes(room, roomCheckBoxes);

        available = Model.getInstance().getAvailableInRangeInit(checkIn_datePicker.getValue(), checkOut_datePicker.getValue(), roomCheckBoxes);

        Model.getInstance().setLeftDate(checkIn);
        Model.getInstance().setRightDate(checkOut);
        Model.getInstance().setSelected();
        Model.getInstance().getViewFactory().colorize(Rooms.manageCheckboxesSetAbbreviatedName(roomCheckBoxes));


        checkIn_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setLeftDate(newValue);
                if(checkOut_datePicker.getValue() != null){
                    if(checkIn_datePicker.getValue().isBefore(checkOut_datePicker.getValue()) || checkIn_datePicker.getValue().equals(checkOut_datePicker.getValue())){
//                        available = sqliteModel.getAvailableRoomsPerDayList(checkIn_datePicker.getValue(), checkOut_datePicker.getValue(), id);
                        available = Model.getInstance().getAvailableInRange(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());
                    }
                    Model.getInstance().setSelected();
                }
            }
        });
        checkOut_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setRightDate(newValue);
                if(checkIn_datePicker.getValue() != null){
                    if(checkIn_datePicker.getValue().isBefore(checkOut_datePicker.getValue()) || checkIn_datePicker.getValue().equals(checkOut_datePicker.getValue())){
//                        available = sqliteModel.getAvailableRoomsPerDayList(checkIn_datePicker.getValue(), checkOut_datePicker.getValue(), id);
                        available = Model.getInstance().getAvailableInRange(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());
                    }
                    Model.getInstance().setSelected();
                }
            }
        });

        for (CheckBox checkBox : roomCheckBoxes){
            checkBoxAddListener(checkBox);
        }

    }

    private void checkBoxAddListener(CheckBox checkBox){
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().getViewFactory().colorize(Rooms.manageCheckboxesSetAbbreviatedName(roomCheckBoxes));
        });
    }
}