package com.resort.resortapp.Models;

import com.resort.resortapp.Rooms;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class sqliteModel {
    private static Connection con = null;
    private static Connection openDB() {
        try {
            String url = "jdbc:sqlite:src/sqlite.db";
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    private static void closeDB(){
        if(con != null){
            try{
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    public static List<Integer> getMonthSlots(){
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < Model.getMonthMaxDate(); i++) {
            result.add(32);
        }
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        String twoDigitMonth = Model.getDateFocus().format(monthFormatter);

        String monthStart = Model.getDateFocus().getYear() + "-" + twoDigitMonth + "-01";
        String monthEnd = Model.getDateFocus().getYear() + "-" + twoDigitMonth + "-" + Model.getMonthMaxDate();
        String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + monthEnd + "' AND checkOut >= '" + monthStart + "';";
//        System.out.println("sql = " + sql);
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");

                String roomValue = resultSet.getString("room");
                Rooms rooms = Rooms.ALL_ROOMS;

                if ("g".equals(roomValue)) {
                    rooms = Rooms.ROOM_G;
                } else if ("j".equals(roomValue)) {
                    rooms = Rooms.ROOM_J;
                } else if ("attic".equals(roomValue)) {
                    rooms = Rooms.ATTIC;
                } else if ("k1".equals(roomValue)) {
                    rooms = Rooms.KUBO_1;
                } else if ("k2".equals(roomValue)) {
                    rooms = Rooms.KUBO_2;
                }

                int startDate = Integer.parseInt(checkInString.substring(8));
                int daysCount = Integer.parseInt(checkOutString.substring(8)) - startDate + 1;


                for(int i = startDate - 1; i < daysCount + startDate - 1; i++){
                    result.set(i, result.get(i) - rooms.getPax());
                }

//                System.out.println("startDate = " + startDate);
//                System.out.println("daysCount = " + daysCount);
//                System.out.println(result);
//                System.out.println(checkInString + ", " + checkOutString + ", " + rooms.getDisplayName());
            }
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result;
    }
    public static List<String> getMonthSlots(Rooms rooms){
        List<String> result = new ArrayList<>();
//        System.out.println(dateOffset);

        for (int i = 0; i < Model.getMonthMaxDate(); i++) {
            result.add("AVAILABLE");
        }

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        String twoDigitMonth = Model.getDateFocus().format(monthFormatter);

        String monthStart = Model.getDateFocus().getYear() + "-" + twoDigitMonth + "-01";
        String monthEnd = Model.getDateFocus().getYear() + "-" + twoDigitMonth + "-" + Model.getMonthMaxDate();
        String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + monthEnd + "' AND checkOut >= '" + monthStart + "' AND room = '" + rooms.getAbbreviatedName() + "';";

        System.out.println("sql = " + sql);
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");
                String room = resultSet.getString("room");

                int startDate = Integer.parseInt(checkInString.substring(8));
                int daysCount = Integer.parseInt(checkOutString.substring(8)) - startDate + 1;

                for(int i = startDate - 1; i < daysCount + startDate - 1; i++){
                    if(!result.get(i).equals("NOT AVAILABLE"))
                        result.set(i, "NOT AVAILABLE");
                }

                System.out.println("startDate = " + startDate);
                System.out.println("daysCount = " + daysCount);
                System.out.println(result);
                System.out.println(checkInString + ", " + checkOutString + ", " + room);
            }
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result;
    }
    public static List<Set<String>> getMonthAvailability(){

        List<Set<String>> result = new ArrayList<>();

        for (int i = 0; i < Model.getMonthMaxDate(); i++) {
            Set<String> set = new HashSet<>();
            set.add("j");
            set.add("g");
            set.add("attic");
            set.add("k1");
            set.add("k2");
            result.add(set);
        }

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        String twoDigitMonth = Model.getDateFocus().format(monthFormatter);

        String monthStart = Model.getDateFocus().getYear() + "-" + twoDigitMonth + "-01";
        String monthEnd = Model.getDateFocus().getYear() + "-" + twoDigitMonth + "-" + Model.getMonthMaxDate();
        String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + monthEnd + "' AND checkOut >= '" + monthStart + "';";
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");

                int startDate = Integer.parseInt(checkInString.substring(8));
                int daysCount = Integer.parseInt(checkOutString.substring(8)) - startDate + 1;

                String roomValue = resultSet.getString("room");

                for(int i = startDate - 1; i < daysCount + startDate - 1; i++){
                    result.get(i).remove(roomValue);
                }

            }
            resultSet.close();
            closeDB();

            for(int i = 0; i < result.size(); i++){
                System.out.println();
                System.out.println(i + 1);
                System.out.println(result.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result;
    }




    public static boolean insertRecord(DatePicker currentDate_datePicker, TextField name_fld,
                                       TextField pax_fld, RadioButton vehicleYes_radio, RadioButton petsYes_radio,
                                       RadioButton videokeYes_radio,
                                       TextField payment_fld, DatePicker checkIn_datePicker,
                                       DatePicker checkOut_datePicker, ChoiceBox<String> room_choiceBox){


        LocalDate currentDateLocalDate = currentDate_datePicker.getValue();
        String name = name_fld.getText();
        String paxString = pax_fld.getText();
        boolean vehicle = vehicleYes_radio.isSelected();
        boolean pets = petsYes_radio.isSelected();
        boolean videoke = videokeYes_radio.isSelected();
        String partial_paymentString = payment_fld.getText();
        LocalDate checkInLocalDate = checkIn_datePicker.getValue();
        LocalDate checkOutLocalDate = checkOut_datePicker.getValue();
        String roomUnformatted = room_choiceBox.getValue();


        if(currentDateLocalDate == null)
            System.out.println("current date empty");
        else if (name.isEmpty())
            System.out.println("name empty");
        else if (paxString.isEmpty())
            System.out.println("pax empty");
        else if(vehicleYes_radio.getToggleGroup().getSelectedToggle() == null)
            System.out.println("vehicle empty");
        else if(petsYes_radio.getToggleGroup().getSelectedToggle() == null)
            System.out.println("pets empty");
        else if(videokeYes_radio.getToggleGroup().getSelectedToggle() == null)
            System.out.println("videoke empty");
        else if(partial_paymentString.isEmpty())
            System.out.println("payment empty");
        else if(checkInLocalDate == null)
            System.out.println("checkin empty");
        else if(checkOutLocalDate == null)
            System.out.println("checkout empty");
        else if(roomUnformatted == null)
            System.out.println("room empty");
        else if(checkInLocalDate.isAfter(checkOutLocalDate))
            System.out.println("checkIn is after checkOut");
        else{
            String currentDate = currentDateLocalDate.toString();
            int paxInt = Integer.parseInt(paxString);
            double partial_paymentDouble = Double.parseDouble(partial_paymentString);
            String checkInString = checkIn_datePicker.getValue().toString();
            String checkOutString = checkOut_datePicker.getValue().toString();
            String roomFormatted = roomUnformatted.replace(" ", "_");
            roomFormatted = Rooms.valueOf(roomFormatted).getAbbreviatedName();



            String sql = String.format("INSERT INTO main (dateInserted, name, pax, vehicle, pets, videoke, partial_payment, checkIn, checkOut, room) " +
                            "VALUES ('%s','%s', %d, %b, %b, %b, %.2f, '%s', '%s', '%s');",
                    currentDate, name, paxInt, vehicle, pets, videoke, partial_paymentDouble, checkInString, checkOutString, roomFormatted);

            System.out.println("sql = " + sql);
            try {
                //open db
                PreparedStatement pStmt = openDB().prepareStatement(sql);
                pStmt.executeUpdate();

                closeDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;




    }
    public void selectAll(){

        String sql = "SELECT * FROM main";
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int pax = resultSet.getInt("pax");
                boolean vehicle = resultSet.getBoolean("vehicle");
                boolean pets = resultSet.getBoolean("pets");
                boolean videoke = resultSet.getBoolean("videoke");
                double partial_payment = resultSet.getDouble("partial_payment");
                java.util.Date checkInDate;
                java.util.Date checkOutDate;
                String checkInString;
                String checkOutString;
                String room = resultSet.getString("room");
                try {
                    checkInDate = dateFormat.parse(resultSet.getString("checkIn"));
                    checkOutDate = dateFormat.parse(resultSet.getString("checkOut"));
                    checkInString = dateFormat.format(checkInDate);
                    checkOutString = dateFormat.format(checkOutDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                System.out.println();
                System.out.println(id + ", " + name + ", " + pax + ", " + vehicle + ", " +
                        pets + ", " + videoke + ", " + partial_payment + ", "
                        + checkInString + ", " + checkOutString + ", " + room);

            }
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}