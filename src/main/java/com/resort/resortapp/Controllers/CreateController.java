package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.RecordModel;
import com.resort.resortapp.Models.sqliteModel;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class CreateController  implements Initializable{
    public RadioButton videokeYes_radio;
    public RadioButton videokeNo_radio;
    public RadioButton petsYes_radio;
    public RadioButton petsNo_radio;
    public Button done_btn;
    public Button clr_btn;
    public TextField name_fld;
    public TextField pax_fld;
    public TextField payment_fld;
    public DatePicker checkOut_datePicker;
    public DatePicker checkIn_datePicker;
    public FlowPane month_pane;
    public Button burger_btn;
    public AnchorPane parentPane;
    public CheckBox roomG_ChkBox;
    public CheckBox roomJ_ChkBox;
    public CheckBox attic_ChkBox;
    public CheckBox kubo1_ChkBox;
    public CheckBox kubo2_ChkBox;
    public TextField vehicle_textFld;
    public TextField fullPayment_fld;
    Set<String> available;
    public AnchorPane escMenu;
    List<CheckBox> roomCheckBoxes = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Model.getInstance().getViewFactory().getOnScreenCalendarDayModels().clear();
        roomCheckBoxes.add(roomJ_ChkBox);
        roomCheckBoxes.add(roomG_ChkBox);
        roomCheckBoxes.add(attic_ChkBox);
        roomCheckBoxes.add(kubo1_ChkBox);
        roomCheckBoxes.add(kubo2_ChkBox);
        Model.getInstance().getViewFactory().setRoomCheckBoxes(roomCheckBoxes);


        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
        burger_btn.setOnAction(actionEvent -> {
            escMenu.setVisible(true);
        });

        done_btn.setOnAction(actionEvent -> {
            insertRecord();
        });
        clr_btn.setOnAction(actionEvent -> {
            if(Model.getInstance().getViewFactory().showConfirmPopup("Are you sure you want to clear the values?"))
                clearForm();
        });
        textFieldAddListener(pax_fld);
        textFieldAddListener(payment_fld);
        textFieldAddListener(fullPayment_fld);

        checkIn_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setSelectedLeftDate(String.valueOf(newValue));
                Model.getInstance().setCalendarLeftDate(newValue);
                Model.getInstance().autoTurnMonth(Model.getInstance().getCalendarLeftDate());

                if(checkOut_datePicker.getValue() != null){
                    if(checkIn_datePicker.getValue().isBefore(checkOut_datePicker.getValue()) || checkIn_datePicker.getValue().equals(checkOut_datePicker.getValue())){
//                        Model.getInstance().setAvailablesForVisual(sqliteModel.getAvailableRoomsPerDayList());
                        available = sqliteModel.getAvailablesForFunction(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());
                    }
                    Model.getInstance().setSelected();
                }
            }
        });
        checkOut_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setSelectedRightDate(String.valueOf(newValue));
                Model.getInstance().setCalendarLeftDate(newValue);
                Model.getInstance().autoTurnMonth(Model.getInstance().getCalendarLeftDate());

                if(checkIn_datePicker.getValue() != null){
                    if(checkIn_datePicker.getValue().isBefore(checkOut_datePicker.getValue()) || checkIn_datePicker.getValue().equals(checkOut_datePicker.getValue())){
//                        Model.getInstance().setAvailablesForVisual(sqliteModel.getAvailableRoomsPerDayList());
                        available = sqliteModel.getAvailablesForFunction(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());
                    }
                    Model.getInstance().setSelected();
                }
            }
        });


        Model.getInstance().getViewFactory().insertCalendar(month_pane);
        if(Model.getInstance().getSelectedLocalDates()!= null)
            Model.getInstance().getSelectedLocalDates().clear();
        for (CheckBox checkBox : roomCheckBoxes){
            checkBoxAddListener(checkBox);
        }
    }
    private void insertRecord(){
        if(sqliteModel.insertRecord(newRecordModel(), available)){
            Model.getInstance().getViewFactory().setSceneTable();
        }
    }
    private void clearForm(){
        name_fld.clear();
        pax_fld.clear();
        videokeYes_radio.setSelected(false);
        videokeNo_radio.setSelected(false);
        petsYes_radio.setSelected(false);
        petsNo_radio.setSelected(false);
        vehicle_textFld.clear();
        payment_fld.clear();
        fullPayment_fld.clear();
        checkIn_datePicker.setValue(null);
//        Model.getInstance().setSelectedLeftDate(null);
        checkOut_datePicker.setValue(null);
//        Model.getInstance().setSelectedRightDate(null);
        for (CheckBox checkBox: roomCheckBoxes) {
            checkBox.setSelected(false);
        }
//        Model.getInstance().getViewFactory().clear();
    }
    private void textFieldAddListener(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    private void checkBoxAddListener(CheckBox checkBox){
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().getViewFactory().colorize();
        });
    }
    private RecordModel newRecordModel(){
        return new RecordModel(name_fld, pax_fld, vehicle_textFld, petsYes_radio, videokeYes_radio,
                payment_fld, fullPayment_fld, checkIn_datePicker, checkOut_datePicker, roomCheckBoxes);
    }
}