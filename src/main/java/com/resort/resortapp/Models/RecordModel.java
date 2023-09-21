package com.resort.resortapp.Models;

import com.resort.resortapp.Rooms;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.util.List;

public class RecordModel {

    String name, pax, vehicle, pets, videoke, payment, checkIn, checkOut, rooms;
    TextField name_fld;
    TextField pax_fld;
    TextField vehicle_fld;
    RadioButton petsYes_radio;
    RadioButton videokeYes_radio;
    TextField payment_fld;
    DatePicker checkIn_datePicker;
    DatePicker checkOut_datePicker;
    List<CheckBox> roomCheckBoxes;

    public RecordModel(TextField name_fld, TextField pax_fld, TextField vehicle_textFld, RadioButton petsYes_radio, RadioButton videokeYes_radio, TextField payment_fld, DatePicker checkIn_datePicker, DatePicker checkOut_datePicker, List<CheckBox> roomCheckBoxes) {
        this.name_fld = name_fld;
        this.pax_fld = pax_fld;
        this.vehicle_fld = vehicle_textFld;
        this.petsYes_radio = petsYes_radio;
        this.videokeYes_radio = videokeYes_radio;
        this.payment_fld = payment_fld;
        this.checkIn_datePicker = checkIn_datePicker;
        this.checkOut_datePicker = checkOut_datePicker;
        this.roomCheckBoxes = roomCheckBoxes;
    }

    public void setStrings() {
        this.name = name_fld.getText();
        this.pax = pax_fld.getText();
        this.vehicle = vehicle_fld.getText();
        this.pets = petsYes_radio.isSelected() ? "YES" : "NO";
        this.videoke = videokeYes_radio.isSelected() ? "YES" : "NO";
        this.payment = payment_fld.getText();
        this.checkIn = checkIn_datePicker.getValue().toString();
        this.checkOut = checkOut_datePicker.getValue().toString();
        this.rooms = Rooms.manageCheckboxesString(roomCheckBoxes);
    }

    public String checkDifferences(RecordModel arg){
        this.setStrings();
        arg.setStrings();


        StringBuilder sb = new StringBuilder();
        if(!this.name.equals(arg.name)){
            sb.append(this.name).append(" -> ").append(arg.name).append("\n");
        }
        if (!this.pax.equals(arg.pax)) {
            sb.append("Pax: ").append(this.pax).append(" -> ").append(arg.pax).append("\n");
        }
        if (!this.vehicle.equals(arg.vehicle)) {
            sb.append("Vehicle: ").append(this.vehicle).append(" -> ").append(arg.vehicle).append("\n");
        }
        if (!this.pets.equals(arg.pets)) {
            sb.append("Pets: ").append(this.pets).append(" -> ").append(arg.pets).append("\n");
        }
        if (!this.videoke.equals(arg.videoke)) {
            sb.append("Videoke: ").append(this.videoke).append(" -> ").append(arg.videoke).append("\n");
        }
        if (!this.payment.equals(arg.payment)) {
            sb.append("Payment: ").append(this.payment).append(" -> ").append(arg.payment).append("\n");
        }
        if (!this.checkIn.equals(arg.checkIn)) {
            sb.append("Check-In: ").append(this.checkIn).append(" -> ").append(arg.checkIn).append("\n");
        }
        if (!this.checkOut.equals(arg.checkOut)) {
            sb.append("Check-Out: ").append(this.checkOut).append(" -> ").append(arg.checkOut).append("\n");
        }
        if (!this.rooms.equals(arg.rooms)) {
            sb.append("Rooms: ").append(this.rooms).append(" -> ").append(arg.rooms).append("\n");
        }
        if (sb.isEmpty()) {
            return "No changes";
        }
        return sb.toString().trim();
    }
}