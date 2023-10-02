package com.resort.resortapp.Models;

import com.resort.resortapp.Rooms;
import javafx.scene.control.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class sqliteModel {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Connection con = null;
    private static Connection openDB() {
        try {
//            List<String> lines = Files.readAllLines(Paths.get("src/main/java/com/resort/resortapp/key.txt"));
            String url = "jdbc:sqlite:src/main/java/com/resort/resortapp/sqlite.db";
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
    public static List<Set<String>> getAvailableRoomsPerDayList(){
        System.out.println("getAvailableRoomsPerdaylist------------");
        List<Set<String>> result = new ArrayList<>();

        LocalDate resultStartDate = Model.getInstance().getCalendarLeftDate();
        LocalDate resultEndDate = Model.getInstance().getCalendarRightDate();

        for (int i = 0; i < resultEndDate.lengthOfMonth(); i++) {
            result.add(Rooms.getRoomAbbreviateNamesSet());
        }

        try {
            String sql = "SELECT id, checkIn, checkOut, room FROM main where checkIn <= '" + resultEndDate + "' AND checkOut >= '" + resultStartDate + "';";
            System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            int rowCount = 0;
            while(resultSet.next()){
                rowCount++;
                LocalDate checkIn = LocalDate.parse(resultSet.getString("checkIn"));
                LocalDate checkOut = LocalDate.parse(resultSet.getString("checkOut"));
                int id = resultSet.getInt("id");

                long daysCount = ChronoUnit.DAYS.between(checkIn, checkOut) + 1;

                //TODO REPLACE WITH ROOMS FUNCTION
                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();

                Collections.addAll(roomSet, roomValue.split(", "));
                System.out.println("["+id+"] " + checkIn + " - " + checkOut + ": " + roomSet);

                int startDate;
                if(checkIn.isBefore(Model.getInstance().getCalendarLeftDate())){
                    startDate = 0;
                    if(Model.getInstance().getCalendarRightDate().isBefore(checkOut)){
                        daysCount = Model.getInstance().getCalendarLeftDate().lengthOfMonth();
                    }
                    else{
                        daysCount = ChronoUnit.DAYS.between(Model.getInstance().getCalendarLeftDate(), checkOut) + 1;
                    }
                }else{
                    startDate = checkIn.getDayOfMonth()-1;
                    daysCount = Math.min(daysCount, checkIn.lengthOfMonth()-checkIn.getDayOfMonth() + 1);
                    daysCount += startDate;
                }
                System.out.println("days " + (startDate+1) + " - " + (daysCount));

                    for(String room : roomSet){
                        for(int i = startDate; i < daysCount ; i++){
                            result.get(i).remove(room);
                        }
                    }
            }

            System.out.println("Row count: " + rowCount);
            pStmt.close();
            resultSet.close();
            closeDB();

//            for(int i = 0; i < result.size(); i++){
//                System.out.println((i + 1) + " " + result.get(i));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static List<Set<String>> getAvailableRoomsPerDayList(int id){
        System.out.println("getAvailableRoomsPerdaylist(int id)");
        List<Set<String>> result = new ArrayList<>();

        LocalDate resultStartDate = Model.getInstance().getCalendarLeftDate();
        LocalDate resultEndDate = Model.getInstance().getCalendarRightDate();

        for (int i = 0; i < resultEndDate.lengthOfMonth(); i++) {
            result.add(Rooms.getRoomAbbreviateNamesSet());
        }

        try {
        String sql = "SELECT id, checkIn, checkOut, room FROM main where checkIn <= '" + resultEndDate + "' AND checkOut >= '" + resultStartDate + "' AND not id = "+id+";";
            System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            int rowCount = 0;
            while(resultSet.next()){
                rowCount++;
                LocalDate checkIn = LocalDate.parse(resultSet.getString("checkIn"));
                LocalDate checkOut = LocalDate.parse(resultSet.getString("checkOut"));
                int rowId = resultSet.getInt("id");

                long daysCount = ChronoUnit.DAYS.between(checkIn, checkOut) + 1;

                //TODO REPLACE WITH ROOMS FUNCTION
                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();

                Collections.addAll(roomSet, roomValue.split(", "));
                System.out.println("["+rowId+"] " + checkIn + " - " + checkOut + ": " + roomSet);

                int startDate;
                if(checkIn.isBefore(Model.getInstance().getCalendarLeftDate())){
                    startDate = 0;
                    if(Model.getInstance().getCalendarRightDate().isBefore(checkOut)){
                        daysCount = Model.getInstance().getCalendarLeftDate().lengthOfMonth();
                    }
                    else{
                        daysCount = ChronoUnit.DAYS.between(Model.getInstance().getCalendarLeftDate(), checkOut) + 1;
                    }
                }else{
                    startDate = checkIn.getDayOfMonth()-1;
                    daysCount = Math.min(daysCount, checkIn.lengthOfMonth()-checkIn.getDayOfMonth() + 1);
                    daysCount += startDate;
                }
                System.out.println("days " + (startDate+1) + " - " + (daysCount));

                for(String room : roomSet){
                    for(int i = startDate; i < daysCount ; i++){
                        result.get(i).remove(room);
                    }
                }
            }

            System.out.println("Row count: " + rowCount);
            pStmt.close();
            resultSet.close();
            closeDB();

//            for(int i = 0; i < result.size(); i++){
//                System.out.println((i + 1) + " " + result.get(i));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    public static boolean insertRecord(RecordModel recordModel, Set<String> available){
        System.out.println("insertrecord");


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

        if (paxString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Number of persons is empty.");
            return false;
        }

        if(vehicle.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Vehicle count is empty.");
            return false;
        }

        if(recordModel.getPetsYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Pets choice is empty.");
            return false;
        }

        if(recordModel.getVideokeYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Videoke choice is empty.");
            return false;
        }

        if(partial_paymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Partial payment is empty.");
            return false;
        }

        if(fullPaymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Full payment is empty.");
            return false;
        }

        if(checkIn.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date is empty.");
            return false;
        }

        if(checkOut.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-out date is empty.");
            return false;
        }

        if(roomUnformatted.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: A room must be selected.");
            return false;
        }

        if(name.length() > 30){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Name should be less than 30 characters.");
            return false;
        }

        if(LocalDate.parse(checkIn).isAfter(LocalDate.parse(checkOut))){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date must come before Check-out date.");
            return false;
        }

//        if(ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut))+1 > 28){
//            Model.getInstance().getViewFactory().showErrorPopup("Error: Booking days cannot be more than 28. This is a bug, will be fixed soon");
//            return false;
//        }

        if(roomCheckboxes.get(0).isSelected() && !available.contains(Rooms.ROOM_J.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_J.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(1).isSelected() && !available.contains(Rooms.ROOM_G.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_G.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(2).isSelected() && !available.contains(Rooms.ATTIC.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ATTIC.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(3).isSelected() && !available.contains(Rooms.KUBO_1.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_1.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(4).isSelected() && !available.contains(Rooms.KUBO_2.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_2.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
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
//            } if (partial_paymentDouble == fullPaymentDouble && !paid) {
//                paid = Model.getInstance().getViewFactory().showConfirmPopup("Confirm: Partial payment is the same as full payment, do you want to consider it as paid?");
//            }
        }
        if (partial_paymentDouble == fullPaymentDouble) {
                paid = true;
            }

        if(paxInt > 99){
                Model.getInstance().getViewFactory().showErrorPopup("Error: No. of person should be less than 100.");
                return false;
            }

        if(Integer.parseInt(vehicle) > 99){
                Model.getInstance().getViewFactory().showErrorPopup("Error: Vehicle count should be less than 100.");
                return false;
        }
        if(fullPaymentDouble >= 1000000){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Full Payment should be less than 1,000,000.");
            return false;
        }


        String sql = String.format("INSERT INTO main (dateInserted, name, pax, vehicle, pets, videoke, partial_payment, full_payment, paid, checkIn, checkOut, room, user) " +
                        "VALUES ('%s','%s', %d, %d, %b, %b, %.2f, %.2f, %b, '%s', '%s', '%s', '%s');",
                currentDate, name, paxInt, Integer.parseInt(vehicle), pets, videoke, partial_paymentDouble, fullPaymentDouble, paid, checkIn, checkOut, roomUnformatted, Model.getInstance().getUser());




        System.out.println("sql = " + sql);
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();



            String changes = "CREATED BOOKING: ";
            changes += "Time Created = " + currentDate;
            changes += ", Name = " + recordModel.getName();
            changes += ", Pax = " + recordModel.getPax();
            changes += ", Vehicle Count = " + recordModel.getVehicle();
            changes += ", Pets = " + recordModel.getPets();
            changes += ", Videoke = " + recordModel.getVideoke();
            changes += ", Partial Payment = " + recordModel.getPartialPayment();
            changes += ", Full Payment = " + recordModel.getFullPayment();
            changes += ", Balance = " + (fullPaymentDouble - partial_paymentDouble);
            changes += ", Pay Status = " + paid;
            changes += ", Check In = " + recordModel.getCheckIn();
            changes += ", Check Out = " + recordModel.getCheckOut();
            changes += ", Rooms = " + recordModel.getRooms();


            sql = String.format("INSERT INTO edit (record_id, edit_timestamp, summary, user) SELECT (MAX(id)), '%s', '%s', '%s' FROM main;",
                    LocalDateTime.now().format(formatter),
                    changes,
                    Model.getInstance().getUser()
            );


//        INSERT INTO edit (record_id, edit_timestamp, summary, user)
//        SELECT (MAX(id) + 1), '2023-10-01 23:06:27', 'CREATED BOOKING: id = 0, dateInserted = 2023-10-01 23:06:27, name = First Name Last Name, pax = 5, vehicle = 2, pets = NO, videoke = YES, partialPayment = 5000, fullPayment = 10000, balance = 5000.0, payStatus = false, checkIn = 2023-10-13, checkOut = 2023-10-13, rooms = g, attic', 'Marvin'
//        FROM main;

            System.out.println("sql = " + sql);

            pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();





            Model.getInstance().getViewFactory().showSuccessPopup("Successfully inserted a record.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            Model.getInstance().getViewFactory().showErrorPopup("Error: " + e);
            return false;
        }
    }
    public static boolean updateRecord(RecordModel recordModel, Set<String> available, String changes){
        System.out.println("updaterecord");
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

        if (paxString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Number of persons is empty.");
            return false;
        }

        if(vehicle.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Vehicle count is empty.");
            return false;
        }

        if(recordModel.getPetsYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Pets choice is empty.");
            return false;
        }

        if(recordModel.getVideokeYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Videoke choice is empty.");
            return false;
        }

        if(recordModel.getPaidYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Paid status choice is empty.");
            return false;
        }

        if(partial_paymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Partial payment is empty.");
            return false;
        }

        if(fullPaymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Full payment is empty.");
            return false;
        }

        if(checkIn.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date is empty.");
            return false;
        }

        if(checkOut.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-out date is empty.");
            return false;
        }

        if(roomUnformatted.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: A room must be selected.");
            return false;
        }

        if(name.length() > 30){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Name should be less than 30 characters.");
            return false;
        }

        if(LocalDate.parse(checkIn).isAfter(LocalDate.parse(checkOut))){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date must come before Check-out date.");
            return false;
        }
//        else if(ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut))+1 > 28){
//            Model.getInstance().getViewFactory().showErrorPopup("Error: Booking days cannot be more than 28. This is a bug, will be fixed soon");
//            return false;
//        }

        if(roomCheckboxes.get(0).isSelected() && !available.contains(Rooms.ROOM_J.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_J.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(1).isSelected() && !available.contains(Rooms.ROOM_G.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_G.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(2).isSelected() && !available.contains(Rooms.ATTIC.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ATTIC.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(3).isSelected() && !available.contains(Rooms.KUBO_1.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_1.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(4).isSelected() && !available.contains(Rooms.KUBO_2.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_2.getDisplayName() + " is unavailable for " + checkIn + " - " + checkOut + ".");
            return false;
        }


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
        }
        if (partial_paymentDouble == fullPaymentDouble) {
            paid = true;
        }
        if(paxInt > 99){
            Model.getInstance().getViewFactory().showErrorPopup("Error: No. of person should be less than 100.");
            return false;
        }
        if(Integer.parseInt(vehicle) > 99){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Vehicle count should be less than 100.");
            return false;
        }
        if(fullPaymentDouble >= 1000000){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Full Payment should be less than 1,000,000.");
            return false;
        }

        //head
        //vehicle
        //payment
        //balance

        try {
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

            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();


            //TODO EDIT HISTORY

            sql = String.format("INSERT INTO edit (record_id, edit_timestamp, summary, user) VALUES ('%d', '%s', '%s', '%s');",
                    recordModel.getIdInt(),
                    LocalDateTime.now().format(formatter),
                    "UPDATED BOOKING: " + changes,
                    Model.getInstance().getUser()
            );

            System.out.println("sql = " + sql);

            pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();

            Model.getInstance().getViewFactory().showSuccessPopup("Successfully updated this record.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + e);
            return false;
        }
    }
    public static void queryTableRecords(){
        System.out.println("querytablerecords");
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

        if(Model.getInstance().checkTableEdges()){
            sql = String.format("SELECT *, (full_payment - partial_payment) as balance FROM main WHERE checkIn <= '%s' AND checkOut >='%s' %s%sORDER BY %s %s;",
                    Model.getInstance().getTableEndDate(), Model.getInstance().getTableStartDate(), roomFilter, nameFilter,
                    Model.getInstance().getOrderCategory().getString(), direction);
        }
//        else if (Model.getInstance().getTableYearMonth() != null) {
//            sql = String.format("SELECT *, (full_payment - partial_payment) as balance FROM main WHERE checkIn <= '%s' AND checkOut >='%s' %s%sORDER BY %s %s;",
//                    Model.getInstance().getTableYearMonth().atEndOfMonth(), Model.getInstance().getTableYearMonth().atDay(1), roomFilter, nameFilter,
//                    Model.getInstance().getOrderCategory().getString(), direction);
//        }
        else{
            sql = String.format("SELECT *, (full_payment - partial_payment) as balance FROM main WHERE 1=1 %s%sORDER BY %s %s;",
                    roomFilter, nameFilter, Model.getInstance().getOrderCategory().getString(), direction);
        }


        System.out.println(sql);
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
            pStmt.close();
            resultSet.close();
            closeDB();

            if(Model.getInstance().checkTableEdges()){
                sql = String.format("SELECT COUNT(*) as totalCount, " +
                                "SUM(CASE WHEN paid = 1 THEN full_payment ELSE (full_payment - partial_payment) END) as totalPayment, " +
                                "SUM(CASE WHEN paid = 0 THEN 1 ELSE 0 END) as totalUnpaid " +
                                "FROM main WHERE checkIn <= '%s' AND checkOut >= '%s' %s%s " +
                                "ORDER BY %s %s;",
                        Model.getInstance().getTableEndDate(), Model.getInstance().getTableStartDate(), roomFilter, nameFilter,
                        Model.getInstance().getOrderCategory().getString(), direction);
            }
            else{
                sql = String.format("SELECT COUNT(*) as totalCount, " +
                                "SUM(CASE WHEN paid = 1 THEN full_payment ELSE (full_payment - partial_payment) END) as totalPayment, " +
                                "SUM(CASE WHEN paid = 0 THEN 1 ELSE 0 END) as totalUnpaid " +
                                "FROM main WHERE 1=1 %s%s" +
                                "ORDER BY %s %s;",
                        roomFilter, nameFilter, Model.getInstance().getOrderCategory().getString(), direction);
            }

            System.out.println(sql);



            pStmt = openDB().prepareStatement(sql);
            resultSet = pStmt.executeQuery();

            Model.getInstance().setRecordCount(resultSet.getInt("totalCount"));
            Model.getInstance().setTotalPayment(resultSet.getDouble("totalPayment"));
            Model.getInstance().setTotalUnpaid(resultSet.getInt("totalUnpaid"));

            pStmt.close();
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }



        Model.getInstance().setListRecordModels(result);
    }
    public static boolean deleteEntry(RecordModel recordModel){
        System.out.println("deleteentry");
        int id = recordModel.getIdInt();
        try {
            String sql = String.format("DELETE FROM main WHERE id = %d", id);
            System.out.println("sql = " + sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();
            pStmt.close();
            closeDB();

            String changes = "DELETED BOOKING: ";
            changes += "Time Created = " + recordModel.getDateInserted();
            changes += ", Name = " + recordModel.getName();
            changes += ", Pax = " + recordModel.getPax();
            changes += ", Vehicle = " + recordModel.getVehicle();
            changes += ", Pets = " + recordModel.getPets();
            changes += ", Videoke = " + recordModel.getVideoke();
            changes += ", Partial Payment = " + recordModel.getPartialPayment();
            changes += ", Full Payment = " + recordModel.getFullPayment();
            changes += ", Balance = " + recordModel.getBalance();
            changes += ", Pay Status = " + recordModel.getPayStatus();
            changes += ", Check In = " + recordModel.getCheckIn();
            changes += ", Check Out = " + recordModel.getCheckOut();
            changes += ", Rooms = " + recordModel.getRooms();


            sql = String.format("INSERT INTO edit (record_id, edit_timestamp, summary, user) VALUES ('%d', '%s', '%s', '%s');",
                    id,
                    LocalDateTime.now().format(formatter),
                    changes,
                    Model.getInstance().getUser()
            );

            System.out.println("sql = " + sql);

            pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();

            Model.getInstance().getViewFactory().showSuccessPopup("Row successfully deleted.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Model.getInstance().getViewFactory().showErrorPopup("Failed to delete row: " + e);
            return false;
        }
    }
    public static List<EditHistoryModel> getEditHistory() {
        System.out.println("getedithistory");
        List<EditHistoryModel> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM edit";
            System.out.println("sql = " + sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                int editID = resultSet.getInt("id");
                int recordId = resultSet.getInt("record_id");
                String editTimestamp = resultSet.getString("edit_timestamp");
                String summary = resultSet.getString("summary");
                String user = resultSet.getString("user");


                String formattedTime = LocalDateTime.parse(editTimestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a"));

                EditHistoryModel editHistoryModel = new EditHistoryModel(editID, recordId, formattedTime, summary, user);

                result.add(editHistoryModel);
            }
            pStmt.close();
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    public static Set<String> getAvailablesForFunction(LocalDate leftEdge, LocalDate rightEdge){
        System.out.println("getAvailablesForFunction");
        Set<String> result = Rooms.getRoomAbbreviateNamesSet();
        try {
            String sql = "SELECT room FROM main where checkIn <= '" + rightEdge + "' AND checkOut >= '" + leftEdge + "';";
            System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();
                Collections.addAll(roomSet, roomValue.split(", "));
                result.removeAll(roomSet);
            }
            System.out.println("AVAILABLE FOR " + leftEdge + "-" + rightEdge + " IS " + result);
            pStmt.close();
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static Set<String> getAvailablesForFunction(LocalDate leftEdge, LocalDate rightEdge, int id){
        System.out.println("getAvailablesForFunction(id)");
        Set<String> result = Rooms.getRoomAbbreviateNamesSet();
        try {
            String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + rightEdge + "' AND checkOut >= '" + leftEdge + "' AND not id = "+id+";";
            System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();
                Collections.addAll(roomSet, roomValue.split(", "));
                result.removeAll(roomSet);
            }
            System.out.println("AVAILABLE FOR " + leftEdge + "-" + rightEdge + " IS " + result);
            pStmt.close();
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



    public static boolean auth(String username, String password) {
        boolean result = false;
        try {
            String sql = "SELECT password from users WHERE username = '"+username+"';";
            System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String dbPassword = resultSet.getString("password");
                if (dbPassword.equals(encrypt(password))){
                    result = true;
                }
            }
            pStmt.close();
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey secretKey = generateSecretKey();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static SecretKey generateSecretKey() {
        try {
            List<Byte> keyBytesList = new ArrayList<>();
            List<String> lines = Files.readAllLines(Paths.get("src/main/java/com/resort/resortapp/key.txt"));
            for (String line : lines) {
                byte byteValue = Byte.parseByte(line.trim());
                keyBytesList.add(byteValue);
            }
            byte[] keyBytes = new byte[keyBytesList.size()];
            for (int i = 0; i < keyBytesList.size(); i++) {
                keyBytes[i] = keyBytesList.get(i);
            }
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Failed to read secret key from file", e);
        }
    }

    public static boolean changePw(String username, String newPW) {
        boolean result = false;
        try {
            String updateSql = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement updateStmt = openDB().prepareStatement(updateSql);
            updateStmt.setString(1, encrypt(newPW));
            updateStmt.setString(2, username);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                result = true;
            }

            updateStmt.close();
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
            Model.getInstance().getViewFactory().showErrorPopup("ERROR: " + e);
        }

        return result;
    }

    public static void forgotPw() {
        try {
            String sql = "SELECT * FROM users";
            System.out.println(sql);

            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            String fileName = "credentials.txt";
            FileWriter writer = null;
            try {
                writer = new FileWriter(fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            while (resultSet.next()) {
                try {
                    String encryptedUsername = encrypt(resultSet.getString("username"));
                    String encryptedPassword = resultSet.getString("password");
                    writer.write(encryptedUsername + "\n");
                    writer.write(encryptedPassword + "\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            writer.close();
            pStmt.close();
            resultSet.close();
            closeDB();

            File file = new File(fileName);
            Model.getInstance().getViewFactory().showForgotPwPopup(fileName, file.getAbsolutePath());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static int getReportID(){
        int result = 0;
        try {
            String sql = "SELECT max(id) as id FROM report";
            System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("id");
            }
            pStmt.close();
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void incrementReportID(){
        try {
            String sql = "INSERT INTO report (dateInserted) VALUES ('"+LocalDateTime.now().format(formatter)+"');";
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}