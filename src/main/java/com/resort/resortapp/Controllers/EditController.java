package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.RecordModel;
import com.resort.resortapp.Models.sqliteModel;
import com.resort.resortapp.Rooms;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.*;

public class EditController implements Initializable {
    public TextField name_fld;
    public TextField pax_fld;
    public RadioButton petsNo_radio;
    public RadioButton petsYes_radio;
    public RadioButton videokeNo_radio;
    public RadioButton videokeYes_radio;
    public TextField partialPayment_fld;
    public TextField fullPayment_fld;
    public RadioButton paidYes_radio;
    public RadioButton paidNo_radio;
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
    private boolean payStatus;
    private RecordModel initRecordModel;

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
        textFieldAddListener(partialPayment_fld);
        textFieldAddListener(fullPayment_fld);


        Model.getInstance().getViewFactory().insertCalendar(month_pane);
    }

    public void updateRecord(){
//        initRecordModel.printStringFields();
//        newRecordModel().printStringFields();
        RecordModel newRecordModel = newRecordModel();
        if(Model.getInstance().getViewFactory().showConfirmPopup("Are you sure you want to edit this record?" + initRecordModel.checkDifferences(newRecordModel))){
            if(sqliteModel.updateRecord(newRecordModel, available)){
                Model.getInstance().getViewFactory().setSceneList();
            }
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

    public void setValues(RecordModel recordModel) {
        id = recordModel.fillInFields(name_fld, pax_fld, vehicle_textFld, petsYes_radio, petsNo_radio, videokeYes_radio, videokeNo_radio, partialPayment_fld, fullPayment_fld, paidYes_radio, paidNo_radio, checkIn_datePicker, checkOut_datePicker, roomCheckBoxes);


        available = Model.getInstance().getAvailableInRangeInit(checkIn_datePicker.getValue(), checkOut_datePicker.getValue(), roomCheckBoxes);

        Model.getInstance().setLeftDate(recordModel.getCheckIn());
        Model.getInstance().setRightDate(recordModel.getCheckOut());
        Model.getInstance().setSelected();
        Model.getInstance().getViewFactory().colorize(Rooms.manageCheckboxesSetAbbreviatedName(roomCheckBoxes));


        checkIn_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setLeftDate(String.valueOf(newValue));
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
                Model.getInstance().setRightDate(String.valueOf(newValue));
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

        initRecordModel = newRecordModel();

    }

    private void checkBoxAddListener(CheckBox checkBox){
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().getViewFactory().colorize(Rooms.manageCheckboxesSetAbbreviatedName(roomCheckBoxes));
        });
    }


    private RecordModel newRecordModel(){
        return new RecordModel(id, payStatus, name_fld, pax_fld, vehicle_textFld, petsYes_radio, videokeYes_radio,
                partialPayment_fld, fullPayment_fld, paidYes_radio, checkIn_datePicker, checkOut_datePicker, roomCheckBoxes);
    }
}