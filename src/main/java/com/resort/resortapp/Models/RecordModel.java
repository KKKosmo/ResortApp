package com.resort.resortapp.Models;

import com.resort.resortapp.Rooms;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecordModel {

    private String id, dateInserted, name, pax, vehicle, pets, videoke, partialPayment, fullPayment, balance, payStatus, checkIn, checkOut, rooms, user;
//    private TextField name_fld;
//    private TextField pax_fld;
//    private TextField vehicle_fld;
    private RadioButton petsYes_radio;
    private RadioButton videokeYes_radio;
    private RadioButton paidYes_radio;
//    private TextField payment_fld;
//    private DatePicker checkIn_datePicker;
//    private DatePicker checkOut_datePicker;
    private List<CheckBox> roomCheckBoxes;
    private List<String> list = new ArrayList<>();

    private int idInt;
    private double balanceDouble;
//    private int paxInt;
//    private int vehicleInt;
    private boolean petsBool;
    private boolean videokeBool;
    private boolean payStatusBool;
//    private double partial_paymentDouble;
//    private LocalDate checkInLD;
//    private LocalDate checkOutLD;

    //for getting from sql then inserting to list, needs both strings and normal types
    public RecordModel(int id, String dateInserted, String name, int pax, int vehicle, boolean pets, boolean videoke, double partialPayment, double fullPayment, double balance, boolean payStatusBool, LocalDate checkIn, LocalDate checkOut, String room, String user) {

        this.id = Integer.toString(id);
        this.payStatusBool = payStatusBool;
        this.payStatus = payStatusBool ? "PAID" : "UNPAID";
        this.dateInserted = dateInserted;
        this.name = name;
        this.pax = Integer.toString(pax);
        this.vehicle = Integer.toString(vehicle);
        this.pets = pets ? "YES" : "NO";
        this.videoke = videoke ? "YES" : "NO";
        this.partialPayment = Double.toString(partialPayment);
        this.fullPayment = Double.toString(fullPayment);
        this.balanceDouble = balance;
        this.balance = String.valueOf(balanceDouble);
        this.checkIn = checkIn.toString();
        this.checkOut = checkOut.toString();
        this.rooms = room;
        this.user = user;

        this.idInt = id;
//        this.paxInt = pax;
//        this.vehicleInt = vehicle;
        this.petsBool = pets;
        this.videokeBool = videoke;
//        this.partial_paymentDouble = partialPayment;
//        this.checkInLD = checkIn;
//        this.checkOutLD = checkOut;
//        System.out.println(this.dateInserted);
        this.dateInserted = LocalDateTime.parse(dateInserted, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a"));
//        System.out.println(this.dateInserted);

        list.add(this.id);
        list.add(this.dateInserted);
        list.add(this.name);
        list.add(this.pax);
        list.add(this.vehicle);
        list.add(this.pets);
        list.add(this.videoke);
        list.add(this.partialPayment);
        list.add(this.balance);
        list.add(this.payStatus);
        list.add(this.checkIn);
        list.add(this.checkOut);
        list.add(this.rooms);
        list.add(this.user);
        list.add(this.fullPayment);
    }

    //for creating
    public RecordModel(TextField name_fld, TextField pax_fld, TextField vehicle_textFld, RadioButton petsYes_radio, RadioButton videokeYes_radio, TextField partialPayment_fld, TextField fullPayment_fld, DatePicker checkIn_datePicker, DatePicker checkOut_datePicker, List<CheckBox> roomCheckBoxes) {

        this.name = name_fld.getText();
        this.pax = pax_fld.getText();
        this.vehicle = vehicle_textFld.getText();
        this.pets = petsYes_radio.isSelected() ? "YES" : "NO";
        this.videoke = videokeYes_radio.isSelected() ? "YES" : "NO";
        this.partialPayment = partialPayment_fld.getText();
        this.fullPayment = fullPayment_fld.getText();
        this.rooms = Rooms.manageCheckboxesString(roomCheckBoxes);

        this.roomCheckBoxes = roomCheckBoxes;
        this.petsYes_radio = petsYes_radio;
        this.videokeYes_radio = videokeYes_radio;


//        if (pax.isEmpty()) {
//            this.paxInt = 0;
//        } else {
//            this.paxInt = Integer.parseInt(pax);
//        }
//        if (vehicle.isEmpty()) {
//            vehicleInt = 0;
//        } else {
//            vehicleInt = Integer.parseInt(vehicle);
//        }
//        if (payment.isEmpty()) {
//            partial_paymentDouble = 0;
//        } else {
//            partial_paymentDouble = Integer.parseInt(payment);
//        }
        if(checkIn_datePicker.getValue() == null){
            checkIn = "";
        } else{
            this.checkIn = checkIn_datePicker.getValue().toString();
        }
        if(checkOut_datePicker.getValue() == null){
            checkOut = "";
        } else{
            this.checkOut = checkOut_datePicker.getValue().toString();
        }



        this.petsBool = petsYes_radio.isSelected();
        this.videokeBool = videokeYes_radio.isSelected();
//        this.checkInLD = LocalDate.parse(checkIn);
//        this.checkOutLD = LocalDate.parse(checkOut);
    }

    public List<String> getList() {
        return list;
    }

    //for editing
    public RecordModel(int id, boolean payStatusBool, TextField name_fld, TextField pax_fld, TextField vehicle_textFld, RadioButton petsYes_radio, RadioButton videokeYes_radio, TextField payment_fld, TextField fullPayment_fld, RadioButton paidYes_radio, DatePicker checkIn_datePicker, DatePicker checkOut_datePicker, List<CheckBox> roomCheckBoxes) {
        this.idInt = id;
        this.payStatusBool = payStatusBool;

        this.name = name_fld.getText();
        this.pax = pax_fld.getText();
        this.vehicle = vehicle_textFld.getText();
        this.pets = petsYes_radio.isSelected() ? "YES" : "NO";
        this.videoke = videokeYes_radio.isSelected() ? "YES" : "NO";
        this.partialPayment = payment_fld.getText();
        this.fullPayment = fullPayment_fld.getText();
        this.payStatus = paidYes_radio.isSelected() ? "PAID" : "UNPAID";
        this.rooms = Rooms.manageCheckboxesString(roomCheckBoxes);

        this.roomCheckBoxes = roomCheckBoxes;
        this.petsYes_radio = petsYes_radio;
        this.videokeYes_radio = videokeYes_radio;
        this.paidYes_radio = paidYes_radio;
//        if (pax.isEmpty()) {
//            this.paxInt = 0;
//        } else {
//            this.paxInt = Integer.parseInt(pax);
//        }
//        if (vehicle.isEmpty()) {
//            vehicleInt = 0;
//        } else {
//            vehicleInt = Integer.parseInt(vehicle);
//        }
//        if (payment.isEmpty()) {
//            partial_paymentDouble = 0;
//        } else {
//            partial_paymentDouble = Integer.parseInt(payment);
//        }
        if(checkIn_datePicker.getValue() == null){
            checkIn = "";
        } else{
            this.checkIn = checkIn_datePicker.getValue().toString();
        }
        if(checkOut_datePicker.getValue() == null){
            checkOut = "";
        } else{
            this.checkOut = checkOut_datePicker.getValue().toString();
        }

        this.petsBool = petsYes_radio.isSelected();
        this.videokeBool = videokeYes_radio.isSelected();
        this.payStatusBool = paidYes_radio.isSelected();
//        this.checkInLD = LocalDate.parse(checkIn);
//        this.checkOutLD = LocalDate.parse(checkOut);
    }



    public void printStringFields() {
        System.out.println("Name: " + this.name);
        System.out.println("Pax: " + this.pax);
        System.out.println("Vehicle: " + this.vehicle);
        System.out.println("Pets: " + this.pets);
        System.out.println("Videoke: " + this.videoke);
        System.out.println("Payment: " + this.partialPayment);
        System.out.println("Check-In: " + this.checkIn);
        System.out.println("Check-Out: " + this.checkOut);
        System.out.println("Rooms: " + this.rooms);
    }
    public String checkDifferences(RecordModel arg){

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
        if (!this.partialPayment.equals(arg.partialPayment)) {
            sb.append("\n").append("Partial Payment: ").append(this.partialPayment).append(" -> ").append(arg.partialPayment);
        }
        if (!this.fullPayment.equals(arg.fullPayment)) {
            sb.append("\n").append("Full Payment: ").append(this.fullPayment).append(" -> ").append(arg.fullPayment);
        }
        if (!this.payStatus.equals(arg.payStatus)) {
            sb.append("\n").append("Pay Status: ").append(this.payStatus).append(" -> ").append(arg.payStatus);
        }
//        if (!this.payStatus.equals(arg.payStatus)) {
//            sb.append("\n").append("Pay Status: ").append(this.payStatus).append(" -> ").append(arg.payStatus);
//        }
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

    public int fillInFields(TextField nameFld, TextField paxFld, TextField vehicleTextFld, RadioButton petsYesRadio, RadioButton videokeYesRadio, RadioButton videokeYes_radio, RadioButton videokeNo_radio, TextField partialPaymentFld, TextField fullPaymentFld, RadioButton paidYes_radio, RadioButton paidNo_radio, DatePicker checkInDatePicker, DatePicker checkOutDatePicker, List<CheckBox> roomCheckBoxes) {
        nameFld.setText(name);
        paxFld.setText(pax);
        vehicleTextFld.setText(vehicle);
        (pets.equals("YES") ? petsYesRadio : videokeYesRadio).setSelected(true);
        (videoke.equals("YES") ? videokeYes_radio : videokeNo_radio).setSelected(true);
        partialPaymentFld.setText(String.valueOf(Math.round(Double.parseDouble(partialPayment))));
        fullPaymentFld.setText(String.valueOf(Math.round(Double.parseDouble(fullPayment))));
        (payStatus.equals("PAID") ? paidYes_radio : paidNo_radio).setSelected(true);
        checkInDatePicker.setValue(LocalDate.parse(checkIn));
        checkOutDatePicker.setValue(LocalDate.parse(checkOut));
        Rooms.tickCheckboxes(rooms, roomCheckBoxes);

        return Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getVideoke() {
        return videoke;
    }

    public void setVideoke(String videoke) {
        this.videoke = videoke;
    }

//    public LocalDate getCheckInLD() {
//        return checkInLD;
//    }
//
//    public LocalDate getCheckOutLD() {
//        return checkOutLD;
//    }

    public int getIdInt() {
        return idInt;
    }

    public String getPax() {
        return pax;
    }

    public String getPartialPayment() {
        return partialPayment;
    }


    public String getCheckIn() {
        return checkIn;
    }


    public String getCheckOut() {
        return checkOut;
    }


    public String getRooms() {
        return rooms;
    }

    public RadioButton getPetsYes_radio() {
        return petsYes_radio;
    }

    public RadioButton getVideokeYes_radio() {
        return videokeYes_radio;
    }

    public List<CheckBox> getRoomCheckBoxes() {
        return roomCheckBoxes;
    }

    public boolean isPetsBool() {
        return petsBool;
    }

    public boolean isVideokeBool() {
        return videokeBool;
    }


    public String getFullPayment() {
        return fullPayment;
    }

    public void setFullPayment(String fullPayment) {
        this.fullPayment = fullPayment;
    }

    public boolean isPayStatusBool() {
        return payStatusBool;
    }

    public void setPayStatusBool(boolean payStatusBool) {
        this.payStatusBool = payStatusBool;
    }

    public RadioButton getPaidYes_radio() {
        return paidYes_radio;
    }
}