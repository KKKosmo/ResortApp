package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.RecordModel;
import com.resort.resortapp.Models.sqliteModel;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.time.LocalDate;
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
    public Button back_btn;
    public TextField partialPayment_fld;
    public CheckBox e_ChkBox;
    Set<String> available;
    public AnchorPane escMenu;
    List<CheckBox> roomCheckBoxes = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roomCheckBoxes.add(roomJ_ChkBox);
        roomCheckBoxes.add(roomG_ChkBox);
        roomCheckBoxes.add(attic_ChkBox);
        roomCheckBoxes.add(kubo1_ChkBox);
        roomCheckBoxes.add(kubo2_ChkBox);
        roomCheckBoxes.add(e_ChkBox);
        Model.getInstance().getViewFactory().setRoomCheckBoxes(roomCheckBoxes);

        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
        burger_btn.setOnAction(actionEvent -> escMenu.setVisible(!escMenu.isVisible()));

        parentPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                burger_btn.fire();
            }
        });


        done_btn.setOnAction(actionEvent -> insertRecord());
        back_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().setSceneTable(false));
        clr_btn.setOnAction(actionEvent -> {
            if(Model.getInstance().getViewFactory().showConfirmPopup("Are you sure you want to clear the values?"))
                clearForm();
        });
        textFieldAddListener(pax_fld);
        textFieldAddListener(partialPayment_fld);
        textFieldAddListener(fullPayment_fld);
        textFieldAddListener(vehicle_textFld);

        textFieldAddEsc(name_fld);
        textFieldAddEsc(pax_fld);
        textFieldAddEsc(vehicle_textFld);
        textFieldAddEsc(partialPayment_fld);
        textFieldAddEsc(fullPayment_fld);

        checkIn_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setSelectedLeftDate(String.valueOf(newValue));
                Model.getInstance().setCalendarLeftDate(newValue);
                Model.getInstance().autoTurnMonth(Model.getInstance().getCalendarLeftDate());
                LocalDate checkOutValue = checkOut_datePicker.getValue();

                if(checkOutValue != null){
                    if(newValue.isBefore(checkOutValue) || newValue.equals(checkOutValue)){
                        available = sqliteModel.getAvailablesForFunction(newValue, checkOutValue);
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
                LocalDate checkInValue = checkIn_datePicker.getValue();

                if(checkInValue != null){
                    if(checkInValue.isBefore(newValue) || checkInValue.equals(newValue)){
                        available = sqliteModel.getAvailablesForFunction(checkInValue, newValue);
                    }
                    Model.getInstance().setSelected();
                }
            }
        });

        Model.getInstance().getViewFactory().insertCalendar(month_pane);
        if(Model.getInstance().getSelectedLocalDates() != null)
            Model.getInstance().getSelectedLocalDates().clear();
        checkBoxAddListener();
        e_ChkBox.selectedProperty().addListener(exclusiveCheckBoxListener);
        parentPane.requestFocus();
        Model.getInstance().autoTurnMonth(LocalDate.now());
    }
    private void insertRecord(){
        if(sqliteModel.insertRecord(newRecordModel(), available)){
            Model.getInstance().getViewFactory().setSceneTable(true);
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
        partialPayment_fld.clear();
        fullPayment_fld.clear();
        checkIn_datePicker.setValue(null);
        checkOut_datePicker.setValue(null);
        for (CheckBox checkBox: roomCheckBoxes) {
            checkBox.setSelected(false);
        }
    }
    private void textFieldAddListener(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }
    private void textFieldAddEsc(TextField textField){
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
                parentPane.requestFocus();
                event.consume();
            }
        });
    }

    ChangeListener<Boolean> checkBoxListener = (observable, oldValue, newValue) -> {
        if(newValue){
            untoggleExclusive();
        }
        Model.getInstance().getViewFactory().colorize();
    };

    public void untoggleExclusive(){
        e_ChkBox.selectedProperty().removeListener(exclusiveCheckBoxListener);
        e_ChkBox.setSelected(false);
        e_ChkBox.selectedProperty().addListener(exclusiveCheckBoxListener);
    }
    ChangeListener<Boolean> exclusiveCheckBoxListener = (observable, oldValue, newValue) -> {
        if(newValue){
            checkBoxRemoveListener();
            roomJ_ChkBox.setSelected(false);
            roomG_ChkBox.setSelected(false);
            attic_ChkBox.setSelected(false);
            kubo1_ChkBox.setSelected(false);
            kubo2_ChkBox.setSelected(false);
            checkBoxAddListener();
        }
        Model.getInstance().getViewFactory().colorize();
    };


    private void checkBoxAddListener(){
        roomJ_ChkBox.selectedProperty().addListener(checkBoxListener);
        roomG_ChkBox.selectedProperty().addListener(checkBoxListener);
        attic_ChkBox.selectedProperty().addListener(checkBoxListener);
        kubo1_ChkBox.selectedProperty().addListener(checkBoxListener);
        kubo2_ChkBox.selectedProperty().addListener(checkBoxListener);
    }
    private void checkBoxRemoveListener(){
        roomJ_ChkBox.selectedProperty().removeListener(checkBoxListener);
        roomG_ChkBox.selectedProperty().removeListener(checkBoxListener);
        attic_ChkBox.selectedProperty().removeListener(checkBoxListener);
        kubo1_ChkBox.selectedProperty().removeListener(checkBoxListener);
        kubo2_ChkBox.selectedProperty().removeListener(checkBoxListener);
    }
    private RecordModel newRecordModel(){
        return new RecordModel(name_fld, pax_fld, vehicle_textFld, petsYes_radio, videokeYes_radio,
                partialPayment_fld, fullPayment_fld, checkIn_datePicker, checkOut_datePicker, roomCheckBoxes);
    }
}