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
    public Button back_btn;
    public CheckBox e_ChkBox;
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
        roomCheckBoxes.add(e_ChkBox);
        Model.getInstance().getViewFactory().setRoomCheckBoxes(roomCheckBoxes);

        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
        burger_btn.setOnAction(actionEvent -> escMenu.setVisible(!escMenu.isVisible()));


        parentPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                burger_btn.fire();
            }
        });

        done_btn.setOnAction(actionEvent -> updateRecord());
        back_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().setSceneTable(false));

        textFieldAddListener(pax_fld);
        textFieldAddListener(partialPayment_fld);
        textFieldAddListener(fullPayment_fld);
        textFieldAddListener(vehicle_textFld);

        textFieldAddEsc(name_fld);
        textFieldAddEsc(pax_fld);
        textFieldAddEsc(vehicle_textFld);
        textFieldAddEsc(partialPayment_fld);
        textFieldAddEsc(fullPayment_fld);
    }

    public void updateRecord(){
        RecordModel newRecordModel = newRecordModel();
        String changes = initRecordModel.checkDifferences(newRecordModel);
        if(Model.getInstance().getViewFactory().showConfirmPopup("Are you sure you want to edit this record?\n\n\nChanges:\n" + changes)){
            if(changes.equals("\nThere are no changes")){
                Model.getInstance().getViewFactory().setSceneTable(false);
            }
            else{
                if(sqliteModel.updateRecord(newRecordModel, available, changes.trim().replace("\n", ", "))){
                    Model.getInstance().getViewFactory().setSceneTable(false);
                }
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
    private void textFieldAddEsc(TextField textField){
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
                parentPane.requestFocus();
                event.consume();
            }
        });
    }

    public void setValues(RecordModel recordModel) {
        id = recordModel.fillInFields(name_fld, pax_fld, vehicle_textFld, petsYes_radio, petsNo_radio, videokeYes_radio, videokeNo_radio, partialPayment_fld, fullPayment_fld, paidYes_radio, paidNo_radio, checkIn_datePicker, checkOut_datePicker, roomCheckBoxes);

//        Model.getInstance().setDateFocus(LocalDate.parse(recordModel.getCheckIn()).withDayOfMonth(1));
        Model.getInstance().getViewFactory().insertCalendar(month_pane);
        Model.getInstance().autoTurnMonth(LocalDate.parse(recordModel.getCheckIn()).withDayOfMonth(1));

        available = sqliteModel.getAvailablesForFunction(checkIn_datePicker.getValue(), checkOut_datePicker.getValue(), id);

        //System.out.println(available);

        Model.getInstance().setSelectedLeftDate(recordModel.getCheckIn());
        Model.getInstance().setSelectedRightDate(recordModel.getCheckOut());
        Model.getInstance().setSelected();
        Model.getInstance().getViewFactory().colorize();
        
        checkIn_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Model.getInstance().setSelectedLeftDate(String.valueOf(newValue));
                Model.getInstance().setCalendarLeftDate(newValue);
                Model.getInstance().autoTurnMonth(Model.getInstance().getCalendarLeftDate());
                LocalDate checkOutValue = checkOut_datePicker.getValue();

                if(checkOutValue != null){
                    if(newValue.isBefore(checkOutValue) || newValue.equals(checkOutValue)){
                        available = sqliteModel.getAvailablesForFunction(newValue, checkOutValue, id);
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
                        available = sqliteModel.getAvailablesForFunction(checkInValue, newValue, id);
                    }
                    Model.getInstance().setSelected();
                }
            }
        });

        checkBoxAddListener();
        e_ChkBox.selectedProperty().addListener(exclusiveCheckBoxListener);

        initRecordModel = newRecordModel();
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
        return new RecordModel(id, payStatus, name_fld, pax_fld, vehicle_textFld, petsYes_radio, videokeYes_radio,
                partialPayment_fld, fullPayment_fld, paidYes_radio, checkIn_datePicker, checkOut_datePicker, roomCheckBoxes);
    }

}