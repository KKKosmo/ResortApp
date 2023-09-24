package com.resort.resortapp.Models;

import com.resort.resortapp.Rooms;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class sqliteModel {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

        int monthMaxDate = Model.getInstance().getMonthMaxDate();

        for (int i = 0; i < monthMaxDate; i++) {
            result.add(32);
        }
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

        ZonedDateTime dateFocus = Model.getInstance().getDateFocus();

        String twoDigitMonth = dateFocus.format(monthFormatter);

        String monthStart = dateFocus.getYear() + "-" + twoDigitMonth + "-01";
        String monthEnd = dateFocus.getYear() + "-" + twoDigitMonth + "-" + monthMaxDate;
        String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + monthEnd + "' AND checkOut >= '" + monthStart + "';";
//        System.out.println("sql = " + sql);
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");

                String roomValue = resultSet.getString("room");
                Rooms rooms = Rooms.fromString(roomValue);

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
        int monthMaxDate = Model.getInstance().getMonthMaxDate();

        for (int i = 0; i < monthMaxDate; i++) {
            result.add("AVAILABLE");
        }

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");


        ZonedDateTime dateFocus = Model.getInstance().getDateFocus();

        String twoDigitMonth = dateFocus.format(monthFormatter);

        String monthStart = dateFocus.getYear() + "-" + twoDigitMonth + "-01";
        String monthEnd = dateFocus.getYear() + "-" + twoDigitMonth + "-" + monthMaxDate;
        String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + monthEnd + "' AND checkOut >= '" + monthStart + "' AND room LIKE '%" + rooms.getAbbreviatedName() + "%';";

        System.out.println("sql = " + sql);
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");

                int startDate = Integer.parseInt(checkInString.substring(8));
                int daysCount = Integer.parseInt(checkOutString.substring(8)) - startDate + 1;

                for(int i = startDate - 1; i < daysCount + startDate - 1; i++){
                    if(!result.get(i).equals("NOT AVAILABLE"))
                        result.set(i, "NOT AVAILABLE");
                }
            }
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result;
    }
    public static List<Set<String>> getAvailableRoomsPerDayList(){

        List<Set<String>> result = new ArrayList<>();

        int monthMaxDate = Model.getInstance().getMonthMaxDate();

        for (int i = 0; i < monthMaxDate; i++) {
            result.add(Rooms.getRoomAbbreviateNamesSet());
        }

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

        ZonedDateTime dateFocus = Model.getInstance().getDateFocus();

        String twoDigitMonth = dateFocus.format(monthFormatter);

        String monthStart = dateFocus.getYear() + "-" + twoDigitMonth + "-01";
        String monthEnd = dateFocus.getYear() + "-" + twoDigitMonth + "-" + monthMaxDate;
        String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + monthEnd + "' AND checkOut >= '" + monthStart + "';";
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");

                int startDate = Integer.parseInt(checkInString.substring(8));
                int daysCount = Integer.parseInt(checkOutString.substring(8)) - startDate + 1;
                //TODO REPLACE WITH ROOMS FUNCTION
                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();

                Collections.addAll(roomSet, roomValue.split(", "));

                for(String room : roomSet){
                    for(int i = startDate - 1; i < daysCount + startDate - 1; i++){
                        result.get(i).remove(room);
                    }
                }

            }
            resultSet.close();
            closeDB();

//            for(int i = 0; i < result.size(); i++){
//                System.out.println();
//                System.out.println(i + 1);
//                System.out.println(result.get(i));
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result;
    }





    public static boolean insertRecord(RecordModel recordModel, Set<String> available){


        String name = recordModel.getName();
        String paxString = recordModel.getPax();
        String vehicle = recordModel.getVehicle();
        boolean pets = recordModel.isPetsBool();
        boolean videoke = recordModel.isVideokeBool();
        String partial_paymentString = recordModel.getPartialPayment();
        String fullPaymentString = recordModel.getFullPayment();
        String checkIn = recordModel.getCheckIn();
        String checkOut = recordModel.getCheckOut();
        String roomUnformatted = recordModel.getRooms();
        List<CheckBox> roomCheckboxes = recordModel.getRoomCheckBoxes();
        



        if (name.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Name is empty.");
            return false;
        }
        else if (paxString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Number of persons is empty.");
            return false;
        }
        else if(vehicle.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Vehicle count is empty.");
            return false;
        }
        else if(recordModel.getPetsYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Pets choice is empty.");
            return false;
        }
        else if(recordModel.getVideokeYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Videoke choice is empty.");
            return false;
        }
        else if(partial_paymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Partial payment is empty.");
            return false;
        }
        else if(fullPaymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Full payment is empty.");
            return false;
        }
        else if(checkIn.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date is empty.");
            return false;
        }
        else if(checkOut.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-out date is empty.");
            return false;
        }
        else if(roomUnformatted.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: A room must be selected.");
            return false;
        }
        else if(LocalDate.parse(checkIn).isAfter(LocalDate.parse(checkOut))){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date must come before Check-out date.");
            return false;
        }else if(roomCheckboxes.get(0).isSelected() && !available.contains(Rooms.ROOM_J.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_J.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }else if(roomCheckboxes.get(1).isSelected() && !available.contains(Rooms.ROOM_G.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_G.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }else if(roomCheckboxes.get(2).isSelected() && !available.contains(Rooms.ATTIC.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ATTIC.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }else if(roomCheckboxes.get(3).isSelected() && !available.contains(Rooms.KUBO_1.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_1.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }else if(roomCheckboxes.get(4).isSelected() && !available.contains(Rooms.KUBO_2.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_2.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        else{
            String currentDate = LocalDateTime.now().format(formatter);
            int paxInt = Integer.parseInt(paxString);
            boolean paid = false;

            if(paxInt <= 0){
                Model.getInstance().getViewFactory().showErrorPopup("Error: Number of people must be more than 0.");
                return false;
            }

            double partial_paymentDouble = Double.parseDouble(partial_paymentString);
            double fullPaymentDouble = Double.parseDouble(fullPaymentString);

            if(partial_paymentDouble > fullPaymentDouble){
                Model.getInstance().getViewFactory().showErrorPopup("Error: Partial payment must be less than full payment.");
                return false;
//            } else if (partial_paymentDouble == fullPaymentDouble && !paid) {
//                paid = Model.getInstance().getViewFactory().showConfirmPopup("Confirm: Partial payment is the same as full payment, do you want to consider it as paid?");
//            }
            } else if (partial_paymentDouble == fullPaymentDouble) {
                paid = true;
            }


            String sql = String.format("INSERT INTO main (dateInserted, name, pax, vehicle, pets, videoke, partial_payment, full_payment, paid, checkIn, checkOut, room) " +
                            "VALUES ('%s','%s', %d, %d, %b, %b, %.2f, %.2f, %b, '%s', '%s', '%s');",
                    currentDate, name, paxInt, Integer.parseInt(vehicle), pets, videoke, partial_paymentDouble, fullPaymentDouble, paid, checkIn, checkOut, roomUnformatted);

            System.out.println("sql = " + sql);
            try {
                PreparedStatement pStmt = openDB().prepareStatement(sql);
                pStmt.executeUpdate();

                closeDB();
                Model.getInstance().getViewFactory().showSuccessPopup("Successfully inserted a record.");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();

                Model.getInstance().getViewFactory().showErrorPopup("Error: " + e);
                return false;
            }
        }
    }




    public static boolean updateRecord(RecordModel recordModel, Set<String> available){


        String name = recordModel.getName();
        String paxString = recordModel.getPax();
        String vehicle = recordModel.getVehicle();
        boolean pets = recordModel.isPetsBool();
        boolean videoke = recordModel.isVideokeBool();
        boolean paid = recordModel.isPayStatusBool();
        String partial_paymentString = recordModel.getPartialPayment();
        String fullPaymentString = recordModel.getFullPayment();
        String checkIn = recordModel.getCheckIn();
        String checkOut = recordModel.getCheckOut();
        String roomUnformatted = recordModel.getRooms();
        List<CheckBox> roomCheckboxes = recordModel.getRoomCheckBoxes();


        if (name.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Name is empty.");
            return false;
        }
        else if (paxString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Number of persons is empty.");
            return false;
        }
        else if(vehicle.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Vehicle count is empty.");
            return false;
        }
        else if(recordModel.getPetsYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Pets choice is empty.");
            return false;
        }
        else if(recordModel.getVideokeYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Videoke choice is empty.");
            return false;
        }
        else if(recordModel.getPaidYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Paid status choice is empty.");
            return false;
        }
        else if(partial_paymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Partial payment is empty.");
            return false;
        }
        else if(fullPaymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Full payment is empty.");
            return false;
        }
        else if(checkIn.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date is empty.");
            return false;
        }
        else if(checkOut.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-out date is empty.");
            return false;
        }
        else if(roomUnformatted.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: A room must be selected.");
            return false;
        }
        else if(LocalDate.parse(checkIn).isAfter(LocalDate.parse(checkOut))){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date must come before Check-out date.");
            return false;

        }else if(roomCheckboxes.get(0).isSelected() && !available.contains(Rooms.ROOM_J.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_J.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }else if(roomCheckboxes.get(1).isSelected() && !available.contains(Rooms.ROOM_G.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_G.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }else if(roomCheckboxes.get(2).isSelected() && !available.contains(Rooms.ATTIC.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ATTIC.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }else if(roomCheckboxes.get(3).isSelected() && !available.contains(Rooms.KUBO_1.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_1.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }else if(roomCheckboxes.get(4).isSelected() && !available.contains(Rooms.KUBO_2.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_2.getAbbreviatedName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        else{
            //TODO EDIT HISTORY
            String currentDate = LocalDateTime.now().format(formatter);
            int paxInt = Integer.parseInt(paxString);

            if(paxInt <= 0){
                Model.getInstance().getViewFactory().showErrorPopup("Error: Number of people must be more than 0.");
                return false;
            }

            double partial_paymentDouble = Double.parseDouble(partial_paymentString);
            double fullPaymentDouble = Double.parseDouble(fullPaymentString);

            if(partial_paymentDouble > fullPaymentDouble){
                Model.getInstance().getViewFactory().showErrorPopup("Error: Partial payment must be less than full payment.");
                return false;
//            } else if (partial_paymentDouble == fullPaymentDouble && !paid) {
//                paid = Model.getInstance().getViewFactory().showConfirmPopup("Confirm: Partial payment is the same as full payment, do you want to consider it as paid?");
//            }
            } else if (partial_paymentDouble == fullPaymentDouble) {
                paid = true;
            }

            String sql = String.format("UPDATE main SET " +
                            "name = '%s', " +
                            "pax = %d, " +
                            "vehicle = %d, " +
                            "pets = %b, " +
                            "videoke = %b, " +
                            "partial_payment = %.2f, " +
                            "full_payment = %.2f, " +
                            "paid = %b, " +
                            "checkIn = '%s', " +
                            "checkOut = '%s', " +
                            "room = '%s' " +
                            "WHERE id = '%d';",
                    name, paxInt, Integer.parseInt(vehicle), pets, videoke, partial_paymentDouble, fullPaymentDouble, paid, checkIn, checkOut, roomUnformatted, recordModel.getIdInt());

            System.out.println("sql = " + sql);
            try {
                PreparedStatement pStmt = openDB().prepareStatement(sql);
                pStmt.executeUpdate();

                closeDB();
                Model.getInstance().getViewFactory().showSuccessPopup("Successfully updated this record.");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                Model.getInstance().getViewFactory().showErrorPopup("Error: " + e);
                return false;
            }
        }
    }


    public static void queryTableRecords(){
        List<RecordModel> result = new ArrayList<>();
//        AND room LIKE '%%'
        StringBuilder roomFilter = new StringBuilder();
        for(String string : Model.getInstance().getTableRooms()){
            roomFilter.append("AND room LIKE '%").append(string).append("%' ");
        }

        String nameFilter;

        if (!Model.getInstance().getNameFilter().isEmpty()){
            nameFilter = "AND name LIKE '%"+Model.getInstance().getNameFilter()+"%'";
        }
        else nameFilter = "";
        String direction;
        if(Model.getInstance().isASC()){
            direction = "ASC";
        }
        else{
            direction = "DESC";
        }
        if(Model.getInstance().getOrderCategory() != Model.OrderCategory.ID){
            direction += ", id DESC";
        }

        String sql;

        if(Model.getInstance().getOrderCategory() == Model.OrderCategory.BALANCE){
            sql = String.format("SELECT *, (full_payment - partial_payment) as balance FROM main WHERE checkIn <= '%s' AND checkOut >='%s' %s%sORDER BY %s %s;",
                    Model.getInstance().getTableEndDate(), Model.getInstance().getTableStartDate(), roomFilter, nameFilter,
                    Model.getInstance().getOrderCategory().getString(), direction);        System.out.println(sql);
            try {
                PreparedStatement pStmt = openDB().prepareStatement(sql);
                ResultSet resultSet = pStmt.executeQuery();
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String dateInserted = resultSet.getString("dateInserted");
                    String name = resultSet.getString("name");
                    int pax = resultSet.getInt("pax");
                    int vehicle = resultSet.getInt("vehicle");
                    boolean pets = resultSet.getBoolean("pets");
                    boolean videoke = resultSet.getBoolean("videoke");
                    double partial_payment = resultSet.getDouble("partial_payment");
                    double full_payment = resultSet.getDouble("full_payment");
                    double balance = resultSet.getDouble("balance");
                    boolean payStatus = resultSet.getBoolean("paid");
                    LocalDate checkInString = LocalDate.parse(resultSet.getString("checkIn"));
                    LocalDate checkOutString = LocalDate.parse(resultSet.getString("checkOut"));
                    String room = resultSet.getString("room");
                    String user = resultSet.getString("user");

                    RecordModel recordModel = new RecordModel(id, dateInserted, name, pax, vehicle, pets, videoke, partial_payment, full_payment, balance, payStatus, checkInString, checkOutString, room, user);
                    result.add(recordModel);
                }
                resultSet.close();
                closeDB();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            sql = String.format("SELECT * FROM main WHERE checkIn <= '%s' AND checkOut >='%s' %s%sORDER BY %s %s;",
                    Model.getInstance().getTableEndDate(), Model.getInstance().getTableStartDate(), roomFilter, nameFilter,
                    Model.getInstance().getOrderCategory().getString(), direction);        System.out.println(sql);
            try {
                PreparedStatement pStmt = openDB().prepareStatement(sql);
                ResultSet resultSet = pStmt.executeQuery();
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String dateInserted = resultSet.getString("dateInserted");
                    String name = resultSet.getString("name");
                    int pax = resultSet.getInt("pax");
                    int vehicle = resultSet.getInt("vehicle");
                    boolean pets = resultSet.getBoolean("pets");
                    boolean videoke = resultSet.getBoolean("videoke");
                    double partial_payment = resultSet.getDouble("partial_payment");
                    double full_payment = resultSet.getDouble("full_payment");
                    double balance = full_payment - partial_payment;
                    boolean payStatus = resultSet.getBoolean("paid");
                    LocalDate checkInString = LocalDate.parse(resultSet.getString("checkIn"));
                    LocalDate checkOutString = LocalDate.parse(resultSet.getString("checkOut"));
                    String room = resultSet.getString("room");
                    String user = resultSet.getString("user");

                    RecordModel recordModel = new RecordModel(id, dateInserted, name, pax, vehicle, pets, videoke, partial_payment, full_payment, balance, payStatus, checkInString, checkOutString, room, user);
                    result.add(recordModel);
                }
                resultSet.close();
                closeDB();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        Model.getInstance().setListRecordModels(result);
    }
    public static boolean deleteEntry(int id){
        String sql = String.format("DELETE FROM main WHERE id = %d", id);
        System.out.println("sql = " + sql);

        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            closeDB();
            Model.getInstance().getViewFactory().showSuccessPopup("Row successfully deleted.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Model.getInstance().getViewFactory().showErrorPopup("Failed to delete row: " + e);
            return false;
        }
    }
}