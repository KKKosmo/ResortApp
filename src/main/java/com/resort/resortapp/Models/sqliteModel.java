package com.resort.resortapp.Models;

import com.resort.resortapp.Rooms;
import javafx.scene.control.CheckBox;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class sqliteModel {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Connection con = null;


    private static Connection openDB() {
        try {
            String url = "jdbc:sqlite:data/sqlite.db";

            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
        }
        return con;
    }


    private static void closeDB(){
        if(con != null){
            try{
                con.close();
            }catch (SQLException e){
                Model.getInstance().printLog(e);
            }
        }
    }

    public static void sqlInit(){
        //System.out.println("sqlInit");
        String folder = "data";
        File srcFolder = new File(folder);
        if (!srcFolder.exists()) {
            //System.out.println("making sql folder");
            srcFolder.mkdir();
        }

        String databasePath = folder + "/sqlite.db";
        File databaseFile = new File(databasePath);

        if (!databaseFile.exists()) {
            //System.out.println("making sql file");
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
                Statement statement = connection.createStatement();

                String sql = "CREATE TABLE IF NOT EXISTS edit (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "record_id INTEGER NOT NULL," +
                        "edit_timestamp SMALLDATETIME NOT NULL," +
                        "summary VARCHAR(100)," +
                        "user VARCHAR(20) NOT NULL," +
                        "FOREIGN KEY (record_id) REFERENCES main(id))";

                statement.execute(sql);

                sql = "CREATE TABLE IF NOT EXISTS main (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "dateInserted SMALLDATETIME," +
                        "name VARCHAR(50)," +
                        "pax INTEGER," +
                        "vehicle INTEGER," +
                        "pets BOOLEAN," +
                        "videoke BOOLEAN," +
                        "partial_payment NUMERIC(6, 2)," +
                        "full_payment NUMERIC(6, 2)," +
                        "paid BOOLEAN," +
                        "checkIn DATE," +
                        "checkOut DATE," +
                        "room VARCHAR(10)," +
                        "user VARCHAR(20))";

                statement.execute(sql);

                sql = "CREATE TABLE IF NOT EXISTS report (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "dateInserted SMALLDATETIME)";

                statement.execute(sql);

                sql = "INSERT INTO report (dateInserted) VALUES ('"+LocalDateTime.now().format(formatter)+"');";
                statement.execute(sql);


                sql = "CREATE TABLE IF NOT EXISTS users (" +
                        "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "username VARCHAR(10) UNIQUE NOT NULL," +
                        "password VARCHAR(255) NOT NULL)";

                statement.execute(sql);


                sql = "INSERT INTO users (user_id, username, password) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);


                preparedStatement.setInt(1, 1);
                preparedStatement.setString(2, "Glorifina");
                preparedStatement.setString(3, "LonsF/hS+zQ6iXI4pjLdnA==");
                preparedStatement.executeUpdate();

                preparedStatement.setInt(1, 2);
                preparedStatement.setString(2, "Gianne");
                preparedStatement.setString(3, "mSQcyxZpkQL2ZU2b9MGnJg==");
                preparedStatement.executeUpdate();

                preparedStatement.setInt(1, 3);
                preparedStatement.setString(2, "Regina");
                preparedStatement.setString(3, "omignY2OreJc2MR+/6g9Jg==");
                preparedStatement.executeUpdate();

                preparedStatement.setInt(1, 4);
                preparedStatement.setString(2, "Roy");
                preparedStatement.setString(3, "WVKymHNS4zdWeVQjQMFvLQ==");
                preparedStatement.executeUpdate();

                preparedStatement.setInt(1, 5);
                preparedStatement.setString(2, "Marvin");
                preparedStatement.setString(3, "E6fgHzvEVgM8EoaptMs4pQ==");
                preparedStatement.executeUpdate();

                preparedStatement.setInt(1, 6);
                preparedStatement.setString(2, "Marie");
                preparedStatement.setString(3, "Gk/kp8C+C3EdZBOTryudKQ==");
                preparedStatement.executeUpdate();

                preparedStatement.close();


                sql = "CREATE INDEX IF NOT EXISTS idIndex ON main (id)";
                statement.execute(sql);

                sql = "CREATE INDEX IF NOT EXISTS mainIndex ON main (checkIn, checkOut, room, name)";
                statement.execute(sql);

                connection.close();
            } catch (SQLException e) {
                Model.getInstance().printLog(e);
            }
        }
    }


    public static List<List<String>> getAvailableRoomsPerDayList(){
        //System.out.println("getAvailableRoomsPerDayList------------");
        List<List<String>> result = new ArrayList<>();

        LocalDate resultStartDate = Model.getInstance().getCalendarLeftDate();
        LocalDate resultEndDate = Model.getInstance().getCalendarRightDate();

        for (int i = 0; i < resultEndDate.lengthOfMonth(); i++) {
            result.add(Rooms.getRoomAbbreviateNamesList());
        }

        try {
            String sql = "SELECT id, checkIn, checkOut, room FROM main where checkIn <= '" + resultEndDate + "' AND checkOut >= '" + resultStartDate + "';";
            //System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                LocalDate checkIn = LocalDate.parse(resultSet.getString("checkIn"));
                LocalDate checkOut = LocalDate.parse(resultSet.getString("checkOut"));

                long daysCount = ChronoUnit.DAYS.between(checkIn, checkOut) + 1;

                //TODO REPLACE WITH ROOMS FUNCTION
                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();

                Collections.addAll(roomSet, roomValue.split(", "));
//                int id = resultSet.getInt("id");
//                System.out.println("["+id+"] " + checkIn + " - " + checkOut + ": " + roomSet);

                int startDate;
                if(checkIn.isBefore(resultStartDate)){
                    startDate = 0;
                    if(resultEndDate.isBefore(checkOut)){
                        daysCount = resultStartDate.lengthOfMonth();
                    }
                    else{
                        daysCount = ChronoUnit.DAYS.between(resultStartDate, checkOut) + 1;
                    }
                }else{
                    startDate = checkIn.getDayOfMonth()-1;
                    daysCount = Math.min(daysCount, checkIn.lengthOfMonth()-checkIn.getDayOfMonth() + 1);
                    daysCount += startDate;
                }
//                System.out.println("days " + (startDate+1) + " - " + (daysCount));

                    for(String room : roomSet){
                        for(int i = startDate; i < daysCount ; i++){
                            result.get(i).remove(room);
                        }
                    }
            }

            pStmt.close();
            resultSet.close();
            closeDB();

//            for(int i = 0; i < result.size(); i++){
//                System.out.println((i + 1) + " " + result.get(i));
//            }
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
        }
        return result;
    }
    public static List<List<String>> getAvailableRoomsPerDayList(int id){
        //System.out.println("getAvailableRoomsPerDayList(int id)");
        List<List<String>> result = new ArrayList<>();

        LocalDate resultStartDate = Model.getInstance().getCalendarLeftDate();
        LocalDate resultEndDate = Model.getInstance().getCalendarRightDate();

        for (int i = 0; i < resultEndDate.lengthOfMonth(); i++) {
            result.add(Rooms.getRoomAbbreviateNamesList());
        }

        try {
        String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + resultEndDate + "' AND checkOut >= '" + resultStartDate + "' AND not id = "+id+";";
            //System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                LocalDate checkIn = LocalDate.parse(resultSet.getString("checkIn"));
                LocalDate checkOut = LocalDate.parse(resultSet.getString("checkOut"));

                long daysCount = ChronoUnit.DAYS.between(checkIn, checkOut) + 1;

                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();

                Collections.addAll(roomSet, roomValue.split(", "));
//                int rowId = resultSet.getInt("id");
//                System.out.println("["+rowId+"] " + checkIn + " - " + checkOut + ": " + roomSet);

                int startDate;
                if(checkIn.isBefore(resultStartDate)){
                    startDate = 0;
                    if(resultEndDate.isBefore(checkOut)){
                        daysCount = resultStartDate.lengthOfMonth();
                    }
                    else{
                        daysCount = ChronoUnit.DAYS.between(resultStartDate, checkOut) + 1;
                    }
                }else{
                    startDate = checkIn.getDayOfMonth()-1;
                    daysCount = Math.min(daysCount, checkIn.lengthOfMonth()-checkIn.getDayOfMonth() + 1);
                    daysCount += startDate;
                }
//                System.out.println("days " + (startDate+1) + " - " + (daysCount));

                for(String room : roomSet){
                    for(int i = startDate; i < daysCount ; i++){
                        result.get(i).remove(room);
                    }
                }
            }

            pStmt.close();
            resultSet.close();
            closeDB();

//            for(int i = 0; i < result.size(); i++){
//                System.out.println((i + 1) + " " + result.get(i));
//            }
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
        }
        return result;
    }



    public static boolean insertRecord(RecordModel recordModel, Set<String> available){
        //System.out.println("insertRecord");

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
            Model.getInstance().getViewFactory().showErrorPopup("Name is empty.");
            return false;
        }
        if (paxString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Head count is empty.");
            return false;
        }
        if(vehicle.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Vehicle count is empty.");
            return false;
        }
        if(recordModel.getPetsYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Pets choice is empty.");
            return false;
        }
        if(recordModel.getVideokeYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Videoke choice is empty.");
            return false;
        }
        if(partial_paymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Partial payment is empty.");
            return false;
        }
        if(fullPaymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Full payment is empty.");
            return false;
        }
        if(checkIn.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Check-in date is empty.");
            return false;
        }
        if(checkOut.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Check-out date is empty.");
            return false;
        }
        if(roomUnformatted.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("A room must be selected.");
            return false;
        }
        if(name.length() > 25){
            Model.getInstance().getViewFactory().showErrorPopup("Name should not be more than 25 characters.");
            return false;
        }
        if(LocalDate.parse(checkIn).isAfter(LocalDate.parse(checkOut))){
            Model.getInstance().getViewFactory().showErrorPopup("Check-in date must come before Check-out date.");
            return false;
        }
//        if(ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut))+1 > 28){
//            Model.getInstance().getViewFactory().showErrorPopup("Booking days cannot be more than 28. This is a bug, will be fixed soon");
//            return false;
//        }
        if(roomCheckboxes.get(0).isSelected() && !available.contains(Rooms.ROOM_J.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.ROOM_J.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(1).isSelected() && !available.contains(Rooms.ROOM_G.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.ROOM_G.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(2).isSelected() && !available.contains(Rooms.ATTIC.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.ATTIC.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(3).isSelected() && !available.contains(Rooms.KUBO_1.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.KUBO_1.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(4).isSelected() && !available.contains(Rooms.KUBO_2.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.KUBO_2.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(5).isSelected() && !available.contains(Rooms.EXCLUSIVE.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.EXCLUSIVE.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        String currentDate = LocalDateTime.now().format(formatter);
        int paxInt = Integer.parseInt(paxString);
        boolean paid = false;
//        if(paxInt <= 0){
//            Model.getInstance().getViewFactory().showErrorPopup("Number of people must be more than 0.");
//            return false;
//        }
        double partial_paymentDouble = Double.parseDouble(partial_paymentString);
        double fullPaymentDouble = Double.parseDouble(fullPaymentString);
        if(partial_paymentDouble > fullPaymentDouble){
            Model.getInstance().getViewFactory().showErrorPopup("Partial payment must be less or equal to full payment.");
            return false;
//            } if (partial_paymentDouble == fullPaymentDouble && !paid) {
//                paid = Model.getInstance().getViewFactory().showConfirmPopup("Confirm: Partial payment is the same as full payment, do you want to consider it as paid?");
//            }
        }
        if (partial_paymentDouble == fullPaymentDouble) {
                paid = true;
            }
        if(paxInt > 99){
                Model.getInstance().getViewFactory().showErrorPopup("Head count should be less than 100.");
                return false;
            }
        if(Integer.parseInt(vehicle) > 99){
                Model.getInstance().getViewFactory().showErrorPopup("Vehicle count should be less than 100.");
                return false;
        }
        if(fullPaymentDouble >= 1000000){
            Model.getInstance().getViewFactory().showErrorPopup("Full Payment should be less than 1,000,000.");
            return false;
        }
        String user = Model.getInstance().getUser();

        String sql = String.format("INSERT INTO main (dateInserted, name, pax, vehicle, pets, videoke, partial_payment, full_payment, paid, checkIn, checkOut, room, user) " +
                        "VALUES ('%s','%s', %d, %d, %b, %b, %.2f, %.2f, %b, '%s', '%s', '%s', '%s');",
                currentDate, name, paxInt, Integer.parseInt(vehicle), pets, videoke, partial_paymentDouble, fullPaymentDouble, paid, checkIn, checkOut, roomUnformatted, user);

        //System.out.println("sql = " + sql);
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();


            pStmt.close();
            closeDB();

            String changes = "CREATED BOOKING: ";
            changes += "TIME CREATED: " + LocalDateTime.parse(currentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a"));
            changes += ", NAME: " + name;
            changes += ", HEAD COUNT: " + paxString;
            changes += ", VEHICLE COUNT: " + vehicle;
            changes += ", PETS: " + pets;
            changes += ", VIDEOKE: " + videoke;
            changes += ", PARTIAL PAYMENT: " + partial_paymentDouble;
            changes += ", FULL PAYMENT: " + fullPaymentDouble;
            changes += ", BALANCE: " + (fullPaymentDouble - partial_paymentDouble);
            changes += ", PAY STATUS: " + paid;
            changes += ", ROOM/s: " + roomUnformatted;
            changes += ", CHECK IN: " + checkIn;
            changes += ", CHECK OUT: " + checkOut;

            sql = String.format("INSERT INTO edit (record_id, edit_timestamp, summary, user) SELECT (MAX(id)), '%s', '%s', '%s' FROM main;",
                    currentDate,
                    changes,
                    user
            );

            //System.out.println("sql = " + sql);

            pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();

            Model.getInstance().getViewFactory().showSuccessPopup("Successfully created a booking.\n" +
                    "\nTIME CREATED: " + LocalDateTime.parse(currentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a")) +
                    "\nNAME: " + name +
                    "\nHEAD COUNT: " + paxString +
                    "\nVEHICLE COUNT: " + vehicle +
                    "\nPETS: " + (pets ? "Yes" : "No") +
                    "\nVIDEOKE: " + (videoke ? "Yes" : "No") +
                    "\nPARTIAL PAYMENT: " + partial_paymentDouble +
                    "\nFULL PAYMENT: " + fullPaymentDouble +
                    "\nBALANCE: " + (fullPaymentDouble - partial_paymentDouble) +
                    "\nPAY STATUS: " + (paid ? "paid" : "unpaid") +
                    "\nROOM/s: " + roomUnformatted +
                    "\nCHECK IN: " + checkIn +
                    "\nCHECK OUT: " + checkOut);
            return true;
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
            return false;
        }
    }
    public static boolean updateRecord(RecordModel recordModel, Set<String> available, String changes){
        //System.out.println("updateRecord");
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
            Model.getInstance().getViewFactory().showErrorPopup("Name is empty.");
            return false;
        }
        if (paxString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Head count is empty.");
            return false;
        }
        if(vehicle.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Vehicle count is empty.");
            return false;
        }
        if(recordModel.getPetsYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Pets choice is empty.");
            return false;
        }
        if(recordModel.getVideokeYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Videoke choice is empty.");
            return false;
        }
        if(recordModel.getPaidYes_radio().getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Paid status choice is empty.");
            return false;
        }
        if(partial_paymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Partial payment is empty.");
            return false;
        }
        if(fullPaymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Full payment is empty.");
            return false;
        }
        if(checkIn.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Check-in date is empty.");
            return false;
        }
        if(checkOut.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Check-out date is empty.");
            return false;
        }
        if(roomUnformatted.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("A room must be selected.");
            return false;
        }
        if(name.length() > 25){
            Model.getInstance().getViewFactory().showErrorPopup("Name should not be more than 25 characters.");
            return false;
        }
        if(LocalDate.parse(checkIn).isAfter(LocalDate.parse(checkOut))){
            Model.getInstance().getViewFactory().showErrorPopup("Check-in date must come before Check-out date.");
            return false;
        }
//        else if(ChronoUnit.DAYS.between(LocalDate.parse(checkIn), LocalDate.parse(checkOut))+1 > 28){
//            Model.getInstance().getViewFactory().showErrorPopup("Booking days cannot be more than 28. This is a bug, will be fixed soon");
//            return false;
//        }
        if(roomCheckboxes.get(0).isSelected() && !available.contains(Rooms.ROOM_J.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.ROOM_J.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(1).isSelected() && !available.contains(Rooms.ROOM_G.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.ROOM_G.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(2).isSelected() && !available.contains(Rooms.ATTIC.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.ATTIC.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(3).isSelected() && !available.contains(Rooms.KUBO_1.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.KUBO_1.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(4).isSelected() && !available.contains(Rooms.KUBO_2.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.KUBO_2.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }
        if(roomCheckboxes.get(5).isSelected() && !available.contains(Rooms.EXCLUSIVE.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup(Rooms.EXCLUSIVE.getDisplayName() + " is unavailable for \n" + checkIn + " - " + checkOut + ".");
            return false;
        }

        int paxInt = Integer.parseInt(paxString);

//        if(paxInt <= 0){
//            Model.getInstance().getViewFactory().showErrorPopup("Number of people must be more than 0.");
//            return false;
//        }

        double partial_paymentDouble = Double.parseDouble(partial_paymentString);
        double fullPaymentDouble = Double.parseDouble(fullPaymentString);

        if(partial_paymentDouble > fullPaymentDouble){
            Model.getInstance().getViewFactory().showErrorPopup("Partial payment must be less or equal to full payment.");
            return false;
//            } else if (partial_paymentDouble == fullPaymentDouble && !paid) {
//                paid = Model.getInstance().getViewFactory().showConfirmPopup("Confirm: Partial payment is the same as full payment, do you want to consider it as paid?");
//            }
        }
        if (partial_paymentDouble == fullPaymentDouble) {
            paid = true;
        }
        if(paxInt > 99){
            Model.getInstance().getViewFactory().showErrorPopup("Head count should be less than 100.");
            return false;
        }
        if(Integer.parseInt(vehicle) > 99){
            Model.getInstance().getViewFactory().showErrorPopup("Vehicle count should be less than 100.");
            return false;
        }
        if(fullPaymentDouble >= 1000000){
            Model.getInstance().getViewFactory().showErrorPopup("Full Payment should be less than 1,000,000.");
            return false;
        }

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

            //System.out.println("sql = " + sql);

            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();

            sql = String.format("INSERT INTO edit (record_id, edit_timestamp, summary, user) VALUES ('%d', '%s', '%s', '%s');",
                    recordModel.getIdInt(),
                    LocalDateTime.now().format(formatter),
                    "UPDATED BOOKING: " + changes,
                    Model.getInstance().getUser()
            );

            //System.out.println("sql = " + sql);

            pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();

            Model.getInstance().getViewFactory().showSuccessPopup("Successfully updated this booking.");
            return true;
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
            return false;
        }
    }
    public static void queryTableRecords(){
        //System.out.println("queryTableRecords");
        List<RecordModel> result = new ArrayList<>();
        StringBuilder roomFilter = new StringBuilder();

        if(!Model.getInstance().getTableRooms().isEmpty()){
            for(String string : Model.getInstance().getTableRooms()){
                roomFilter.append("OR room LIKE '%").append(string).append("%' ");
            }
            roomFilter.replace(0, 3, "AND (");
            roomFilter.append(") ");
        }

        String nameFilter;

        String modelDateFilter = Model.getInstance().getNameFilter();
        if (!modelDateFilter.isEmpty()){
            nameFilter = "AND name LIKE '%"+modelDateFilter+"%'";
        }
        else nameFilter = "";
        String direction;
        if(Model.getInstance().isASC()){
            direction = "ASC";
        }
        else{
            direction = "DESC";
        }

        String orderCategoryString = Model.getInstance().getOrderCategory().getString();
        if(Model.getInstance().getOrderCategory() != Model.OrderCategory.ID){
            direction += ", id DESC";
        }


        LocalDate tableStartDate = Model.getInstance().getTableStartDate();
        LocalDate tableEndDate = Model.getInstance().getTableEndDate();
        String sql;

        if(Model.getInstance().checkTableEdges()){
//            sql = String.format("SELECT *, (full_payment - partial_payment) as balance FROM main WHERE checkIn <= '%s' AND checkOut >='%s' %s%sORDER BY %s %s;",
//                    tableEndDate, tableStartDate, roomFilter, nameFilter,
//                    orderCategoryString, direction);
            sql = String.format("SELECT *, (full_payment - partial_payment) as balance FROM main WHERE checkIn <= '%s' AND checkIn >='%s' %s%sORDER BY %s %s;",
                    tableEndDate, tableStartDate, roomFilter, nameFilter,
                    orderCategoryString, direction);
        }
//        else if (Model.getInstance().getTableYearMonth() != null) {
//            sql = String.format("SELECT *, (full_payment - partial_payment) as balance FROM main WHERE checkIn <= '%s' AND checkOut >='%s' %s%sORDER BY %s %s;",
//                    Model.getInstance().getTableYearMonth().atEndOfMonth(), Model.getInstance().getTableYearMonth().atDay(1), roomFilter, nameFilter,
//                    orderCategoryString, direction);
//        }
        else{
            sql = String.format("SELECT *, (full_payment - partial_payment) as balance FROM main WHERE 1=1 %s%sORDER BY %s %s;",
                    roomFilter, nameFilter, orderCategoryString, direction);
        }

//        System.out.println(sql);
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
//                sql = String.format("SELECT COUNT(*) as totalCount, " +
//                                "SUM(CASE WHEN paid = 1 THEN full_payment ELSE (full_payment - partial_payment) END) as totalPayment, " +
//                                "SUM(CASE WHEN paid = 0 THEN 1 ELSE 0 END) as totalUnpaid " +
//                                "FROM main WHERE checkIn <= '%s' AND checkOut >= '%s' %s%s " +
//                                "ORDER BY %s %s;",
//                        tableEndDate, tableStartDate, roomFilter, nameFilter,
//                        orderCategoryString, direction);
                sql = String.format("SELECT COUNT(*) as totalCount, " +
                                "SUM(CASE WHEN paid = 1 THEN full_payment ELSE (full_payment - partial_payment) END) as totalPayment, " +
                                "SUM(CASE WHEN paid = 0 THEN 1 ELSE 0 END) as totalUnpaid " +
                                "FROM main WHERE checkIn <= '%s' AND checkIn >='%s' %s%s " +
                                "ORDER BY %s %s;",
                        tableEndDate, tableStartDate, roomFilter, nameFilter,
                        orderCategoryString, direction);
            }
            else{
                sql = String.format("SELECT COUNT(*) as totalCount, " +
                                "SUM(CASE WHEN paid = 1 THEN full_payment ELSE (full_payment - partial_payment) END) as totalPayment, " +
                                "SUM(CASE WHEN paid = 0 THEN 1 ELSE 0 END) as totalUnpaid " +
                                "FROM main WHERE 1=1 %s%s" +
                                "ORDER BY %s %s;",
                        roomFilter, nameFilter, orderCategoryString, direction);
            }

            //System.out.println(sql);

            pStmt = openDB().prepareStatement(sql);
            resultSet = pStmt.executeQuery();

            Model.getInstance().setRecordCount(resultSet.getInt("totalCount"));
            Model.getInstance().setTotalPayment(resultSet.getDouble("totalPayment"));
            Model.getInstance().setTotalUnpaid(resultSet.getInt("totalUnpaid"));

            pStmt.close();
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            Model.getInstance().printLog(e);
        }

        Model.getInstance().setListRecordModels(result);
    }
    public static boolean deleteEntry(RecordModel recordModel){
        //System.out.println("deleteEntry");
        int id = recordModel.getIdInt();
        try {
            String sql = String.format("DELETE FROM main WHERE id = %d", id);
            //System.out.println("sql = " + sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();
            pStmt.close();
            closeDB();

            String changes = "DELETED BOOKING: ";
            changes += "TIME CREATED: " + recordModel.getDateInserted();
            changes += ", NAME: " + recordModel.getName();
            changes += ", HEAD COUNT: " + recordModel.getPax();
            changes += ", VEHICLE COUNT: " + recordModel.getVehicle();
            changes += ", PETS: " + recordModel.getPets();
            changes += ", VIDEOKE: " + recordModel.getVideoke();
            changes += ", PARTIAL PAYMENT: " + recordModel.getPartialPayment();
            changes += ", FULL PAYMENT: " + recordModel.getFullPayment();
            changes += ", BALANCE: " + recordModel.getBalance();
            changes += ", PAY STATUS: " + recordModel.getPayStatus();
            changes += ", ROOM/s: " + recordModel.getRooms();
            changes += ", CHECK IN: " + recordModel.getCheckIn();
            changes += ", CHECK OUT: " + recordModel.getCheckOut();


            sql = String.format("INSERT INTO edit (record_id, edit_timestamp, summary, user) VALUES ('%d', '%s', '%s', '%s');",
                    id,
                    LocalDateTime.now().format(formatter),
                    changes,
                    Model.getInstance().getUser()
            );

            //System.out.println("sql = " + sql);

            pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            pStmt.close();
            closeDB();

            Model.getInstance().getViewFactory().showSuccessPopup("Successfully deleted this booking.");
            return true;
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
//            Model.getInstance().getViewFactory().showErrorPopup("Failed to delete row: " + e);
            return false;
        }
    }
    public static List<EditHistoryModel> getEditHistory() {
        //System.out.println("getEditHistory");
        List<EditHistoryModel> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM edit";
            //System.out.println("sql = " + sql);
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
            Model.getInstance().printLog(e);
        }

        return result;
    }
    public static Set<String> getAvailablesForFunction(LocalDate leftEdge, LocalDate rightEdge){
        //System.out.println("getAvailablesForFunction");
        Set<String> result = Rooms.getRoomAbbreviateNamesSet();
        try {
            String sql = "SELECT room FROM main where checkIn <= '" + rightEdge + "' AND checkOut >= '" + leftEdge + "';";
            //System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();
                Collections.addAll(roomSet, roomValue.split(", "));
                result.removeAll(roomSet);
            }
//            System.out.println("AVAILABLE FOR " + leftEdge + "-" + rightEdge + " IS " + result);
            pStmt.close();
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
        }
        return result;
    }
    public static Set<String> getAvailablesForFunction(LocalDate leftEdge, LocalDate rightEdge, int id){
        //System.out.println("getAvailablesForFunction(id)");
        Set<String> result = Rooms.getRoomAbbreviateNamesSet();
        try {
            String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + rightEdge + "' AND checkOut >= '" + leftEdge + "' AND not id = "+id+";";
            //System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();
                Collections.addAll(roomSet, roomValue.split(", "));
                result.removeAll(roomSet);
            }
//            System.out.println("AVAILABLE FOR " + leftEdge + "-" + rightEdge + " IS " + result);
            pStmt.close();
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
        }
        return result;
    }



    public static boolean auth(String username, String password) {
        boolean result = false;
        try {
            String sql = "SELECT password from users WHERE username = '"+username+"';";
            //System.out.println(sql);
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
            Model.getInstance().printLog(e);
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
            Model.getInstance().printLog(new RuntimeException(e));
            throw new RuntimeException(e);
        }
    }

    public static SecretKey generateSecretKey() {
        try {
            List<Byte> keyBytesList = new ArrayList<>();

            InputStream inputStream = sqliteModel.class.getResourceAsStream("/key.txt");

            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    List<String> lines = reader.lines().toList();
                    for (String line : lines) {
                        byte byteValue = Byte.parseByte(line.trim());
                        keyBytesList.add(byteValue);
                    }
                }
            } else {
                Model.getInstance().printLog(new IOException("Resource 'key.txt' not found."));
                throw new IOException("Resource 'key.txt' not found.");
            }

            byte[] keyBytes = new byte[keyBytesList.size()];
            for (int i = 0; i < keyBytesList.size(); i++) {
                keyBytes[i] = keyBytesList.get(i);
            }
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            Model.getInstance().printLog(new RuntimeException("Failed to read secret key from file", e));
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
            Model.getInstance().printLog(e);
            result = false;
//            Model.getInstance().getViewFactory().showErrorPopup(e);
        }

        return result;
    }

    public static void forgotPw() {
        try {
            String sql = "SELECT * FROM users";
            //System.out.println(sql);

            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            String fileName = "credentials.txt";
            FileWriter writer;
            try {
                writer = new FileWriter(fileName);
            } catch (IOException e) {
                Model.getInstance().printLog(new RuntimeException(e));
                throw new RuntimeException(e);
            }

            while (resultSet.next()) {
                try {
                    String encryptedUsername = encrypt(resultSet.getString("username"));
                    String encryptedPassword = resultSet.getString("password");
                    writer.write(encryptedUsername + "\n");
                    writer.write(encryptedPassword + "\n");

                } catch (Exception e) {
                    Model.getInstance().printLog(e);
                }
            }
            writer.close();
            pStmt.close();
            resultSet.close();
            closeDB();

            File file = new File(fileName);
            Model.getInstance().getViewFactory().showForgotPwPopup(fileName, file.getAbsolutePath());
        } catch (SQLException | IOException e) {
            Model.getInstance().printLog(e);
        }
    }

    public static int getReportID(){
        int result = 0;
        try {
            String sql = "SELECT max(id) as id FROM report";
            //System.out.println(sql);
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt("id");
            }
            pStmt.close();
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            Model.getInstance().printLog(e);
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
            Model.getInstance().printLog(e);
        }
    }
}