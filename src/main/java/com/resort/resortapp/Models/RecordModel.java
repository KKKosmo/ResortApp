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
        setStrings();
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

    public void printStringFields() {
        System.out.println("Name: " + this.name);
        System.out.println("Pax: " + this.pax);
        System.out.println("Vehicle: " + this.vehicle);
        System.out.println("Pets: " + this.pets);
        System.out.println("Videoke: " + this.videoke);
        System.out.println("Payment: " + this.payment);
        System.out.println("Check-In: " + this.checkIn);
        System.out.println("Check-Out: " + this.checkOut);
        System.out.println("Rooms: " + this.rooms);
    }
    public String checkDifferences(RecordModel arg){
//        this.setStrings();
//        arg.setStrings();
//        this.printStringFields();
//        arg.printStringFields();

        StringBuilder sb = new StringBuilder();
        if(!this.name.equals(arg.name)){
            sb.append("\n").append(this.name).append(" -> ").append(arg.name);
        }
        if (!this.pax.equals(arg.pax)) {
            sb.append("\n").append("Pax: ").append(this.pax).append(" -> ").append(arg.pax);
        }
        if (!this.vehicle.equals(arg.vehicle)) {
            sb.append("\n").append("Vehicle: ").append(this.vehicle).append(" -> ").append(arg.vehicle);
        }
        if (!this.pets.equals(arg.pets)) {
            sb.append("\n").append("Pets: ").append(this.pets).append(" -> ").append(arg.pets);
        }
        if (!this.videoke.equals(arg.videoke)) {
            sb.append("\n").append("Videoke: ").append(this.videoke).append(" -> ").append(arg.videoke);
        }
        if (!this.payment.equals(arg.payment)) {
            sb.append("\n").append("Payment: ").append(this.payment).append(" -> ").append(arg.payment);
        }
        if (!this.checkIn.equals(arg.checkIn)) {
            sb.append("\n").append("Check-In: ").append(this.checkIn).append(" -> ").append(arg.checkIn);
        }
        if (!this.checkOut.equals(arg.checkOut)) {
            sb.append("\n").append("Check-Out: ").append(this.checkOut).append(" -> ").append(arg.checkOut);
        }
        if (!this.rooms.equals(arg.rooms)) {
            sb.append("\n").append("Rooms: ").append(this.rooms).append(" -> ").append(arg.rooms);
        }
        if (sb.isEmpty()) {
            return "\nThere are no changes";
        }
        return sb.toString();
    }
}