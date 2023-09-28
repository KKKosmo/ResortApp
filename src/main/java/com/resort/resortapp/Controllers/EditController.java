package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.RecordModel;
import com.resort.resortapp.Models.sqliteModel;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.time.LocalDate;
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
        Model.getInstance().getViewFactory().setRoomCheckBoxes(roomCheckBoxes);


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


    }

    public void updateRecord(){
//        initRecordModel.printStringFields();
//        newRecordModel().printStringFields();
        RecordModel newRecordModel = newRecordModel();
        String changes = initRecordModel.checkDifferences(newRecordModel);
        if(Model.getInstance().getViewFactory().showConfirmPopup("Are you sure you want to edit this record?" + changes)){
            if(sqliteModel.updateRecord(newRecordModel, available, changes.trim().replace("\n", ", "))){
                Model.getInstance().getViewFactory().setSceneTable();
//                Model.getInstance().getViewFactory().notEditing();
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

        Model.getInstance().getViewFactory().insertCalendar(month_pane);
        Model.getInstance().autoTurnMonth(LocalDate.parse(recordModel.getCheckIn()));

        Model.getInstance().setAvailablesForVisual(sqliteModel.getAvailableRoomsPerDayList(id));
        available = Model.getInstance().getAvailableInRange(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());

        System.out.println(available);

        Model.getInstance().setSelectedLeftDate(recordModel.getCheckIn());
        Model.getInstance().setSelectedRightDate(recordModel.getCheckOut());
        Model.getInstance().setSelected();
        Model.getInstance().getViewFactory().colorize();


        checkIn_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setSelectedLeftDate(String.valueOf(newValue));
                Model.getInstance().setCalendarLeftDate(newValue);
                Model.getInstance().setEdgeLeftDate(Model.getInstance().getCalendarLeftDate());
                Model.getInstance().autoTurnMonth(Model.getInstance().getCalendarLeftDate());

                if(checkOut_datePicker.getValue() != null){
                    if(checkIn_datePicker.getValue().isBefore(checkOut_datePicker.getValue()) || checkIn_datePicker.getValue().equals(checkOut_datePicker.getValue())){
                        Model.getInstance().setAvailablesForVisual(sqliteModel.getAvailableRoomsPerDayList(id));
                        available = Model.getInstance().getAvailableInRange(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());
                    }
                    Model.getInstance().setSelected();
                }
            }
        });
        checkOut_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setSelectedRightDate(String.valueOf(newValue));
                Model.getInstance().setCalendarRightDate(newValue);
                Model.getInstance().setEdgeRightDate(Model.getInstance().getCalendarRightDate());
                Model.getInstance().autoTurnMonth(Model.getInstance().getCalendarRightDate());

                if(checkIn_datePicker.getValue() != null){
                    if(checkIn_datePicker.getValue().isBefore(checkOut_datePicker.getValue()) || checkIn_datePicker.getValue().equals(checkOut_datePicker.getValue())){
                        Model.getInstance().setAvailablesForVisual(sqliteModel.getAvailableRoomsPerDayList(id));
                        available = Model.getInstance().getAvailableInRange(checkIn_datePicker.getValue(), checkOut_datePicker.getValue());
                        System.out.println(available);
                    }
                    Model.getInstance().setSelected();
                }
            }
        });

        for (CheckBox checkBox : roomCheckBoxes){
            checkBoxAddListener(checkBox);
        }

        initRecordModel = newRecordModel();

//        Model.getInstance().setEdgeLeftDate(checkIn_datePicker.getValue().withDayOfMonth(1));
//
//        Model.getInstance().setEdgeRightDate(checkOut_datePicker.getValue().withDayOfMonth(checkOut_datePicker.getValue().lengthOfMonth()));
    }

    private void checkBoxAddListener(CheckBox checkBox){
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().getViewFactory().colorize();
        });
    }


    private RecordModel newRecordModel(){
        return new RecordModel(id, payStatus, name_fld, pax_fld, vehicle_textFld, petsYes_radio, videokeYes_radio,
                partialPayment_fld, fullPayment_fld, paidYes_radio, checkIn_datePicker, checkOut_datePicker, roomCheckBoxes);
    }

}