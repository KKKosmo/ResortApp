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
//        System.out.println(dateOffset);
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
            result.add(Rooms.getRoomDisplayNameSet());
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

                String roomValue = resultSet.getString("room");
                Set<String> roomSet = new HashSet<>();

                Collections.addAll(roomSet, roomValue.split(", "));

                for(String room : roomSet){
                    for(int i = startDate - 1; i < daysCount + startDate - 1; i++){
                        result.get(i).remove(Rooms.abbvToDisplay(room));
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


    public static Set<String> getAvailableRoomsPerDayList(LocalDate checkIn, LocalDate checkOut){

        Set<String> set = Rooms.getRoomAbbreviateNameSet();



//        for (int i = 0; i < Model.getInstance().getMonthMaxDate(); i++) {
//            set.add("j");
//            set.add("g");
//            set.add("attic");
//            set.add("k1");
//            set.add("k2");
//        }

        String sql = "SELECT room FROM main where checkIn <= '" + checkOut.toString() + "' AND checkOut >= '" + checkIn.toString() + "';";
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                set.remove(resultSet.getString("room"));
            }
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(checkIn + " - " + checkOut + " = " + set);
        return set;
    }
    public static Set<String> getAvailableRoomsPerDayList(LocalDate checkIn, LocalDate checkOut, int id){

        Set<String> set = Rooms.getRoomAbbreviateNameSet();


        String sql = String.format("SELECT room FROM main where checkIn <= '%s' AND checkOut >= '%s' AND NOT id = %d;", checkOut.toString(), checkIn.toString(), id);
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                set.remove(resultSet.getString("room"));
            }
            resultSet.close();
            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(checkIn + " - " + checkOut + " = " + set);
        return set;
    }





    public static boolean insertRecord(TextField name_fld,
                                       TextField pax_fld, TextField vehicle_fld, RadioButton petsYes_radio,
                                       RadioButton videokeYes_radio, TextField payment_fld, DatePicker checkIn_datePicker,
                                       DatePicker checkOut_datePicker, List<CheckBox> roomCheckboxes, Set<String> available){


        String name = name_fld.getText();
        String paxString = pax_fld.getText();
        String vehicle = vehicle_fld.getText();
        boolean pets = petsYes_radio.isSelected();
        boolean videoke = videokeYes_radio.isSelected();
        String partial_paymentString = payment_fld.getText();
        LocalDate checkInLocalDate = checkIn_datePicker.getValue();
        LocalDate checkOutLocalDate = checkOut_datePicker.getValue();
        String roomUnformatted = Rooms.manageCheckboxesString(roomCheckboxes);
//        Set<String> roomStringSet = Rooms.manageCheckboxes(roomCheckboxes);
//        List<String> roomStringSet = Rooms.manageCheckboxes(roomCheckboxes);
        



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
        else if(petsYes_radio.getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Pets choice is empty.");
            return false;
        }
        else if(videokeYes_radio.getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Videoke choice is empty.");
            return false;
        }
        else if(partial_paymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Partial payment is empty.");
            return false;
        }
        else if(checkInLocalDate == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date is empty.");
            return false;
        }
        else if(checkOutLocalDate == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-out date is empty.");
            return false;
        }
        else if(roomUnformatted.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: A room must be selected.");
            return false;
        }
        else if(checkInLocalDate.isAfter(checkOutLocalDate)){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date must come before Check-out date.");
            return false;
        } else if(roomCheckboxes.get(0).isSelected() && !available.contains(Rooms.ROOM_G.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_G.getAbbreviatedName() + " is unavailable for " + checkInLocalDate + " - " + checkOutLocalDate + ".");
            return false;
        }else if(roomCheckboxes.get(1).isSelected() && !available.contains(Rooms.ROOM_J.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ROOM_J.getAbbreviatedName() + " is unavailable for " + checkInLocalDate + " - " + checkOutLocalDate + ".");
            return false;
        }else if(roomCheckboxes.get(2).isSelected() && !available.contains(Rooms.ATTIC.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.ATTIC.getAbbreviatedName() + " is unavailable for " + checkInLocalDate + " - " + checkOutLocalDate + ".");
            return false;
        }else if(roomCheckboxes.get(3).isSelected() && !available.contains(Rooms.KUBO_1.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_1.getAbbreviatedName() + " is unavailable for " + checkInLocalDate + " - " + checkOutLocalDate + ".");
            return false;
        }else if(roomCheckboxes.get(4).isSelected() && !available.contains(Rooms.KUBO_2.getAbbreviatedName())){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.KUBO_2.getAbbreviatedName() + " is unavailable for " + checkInLocalDate + " - " + checkOutLocalDate + ".");
            return false;
        }
        else{
            String currentDate = LocalDateTime.now().format(formatter);

            int paxInt = Integer.parseInt(paxString);

            if(paxInt <= 0){
                Model.getInstance().getViewFactory().showErrorPopup("Error: Number of people must be more than 0.");
                return false;
            }

            double partial_paymentDouble = Double.parseDouble(partial_paymentString);
            String checkInString = checkIn_datePicker.getValue().toString();
            String checkOutString = checkOut_datePicker.getValue().toString();



            String sql = String.format("INSERT INTO main (dateInserted, name, pax, vehicle, pets, videoke, partial_payment, checkIn, checkOut, room) " +
                            "VALUES ('%s','%s', %d, %d, %b, %b, %.2f, '%s', '%s', '%s');",
                    currentDate, name, paxInt, Integer.parseInt(vehicle), pets, videoke, partial_paymentDouble, checkInString, checkOutString, roomUnformatted);

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




    public static boolean updateRecord(int id, TextField name_fld,
                                       TextField pax_fld, RadioButton vehicleYes_radio, RadioButton petsYes_radio,
                                       RadioButton videokeYes_radio, TextField payment_fld, DatePicker checkIn_datePicker,
                                       DatePicker checkOut_datePicker, ChoiceBox<String> room_choiceBox, Set<String> available){


        String name = name_fld.getText();
        String paxString = pax_fld.getText();
        boolean vehicle = vehicleYes_radio.isSelected();
        boolean pets = petsYes_radio.isSelected();
        boolean videoke = videokeYes_radio.isSelected();
        String partial_paymentString = payment_fld.getText();
        LocalDate checkInLocalDate = checkIn_datePicker.getValue();
        LocalDate checkOutLocalDate = checkOut_datePicker.getValue();
        String room = room_choiceBox.getValue();

//        available.add(room);
        room = Rooms.displayToAbbv(room);


        System.out.println("=======HERE======");
        System.out.println(room);
        System.out.println(available);

        if (name.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Name is empty.");
            return false;
        }
        else if (paxString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Number of persons is empty.");
            return false;
        }
        else if(vehicleYes_radio.getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Vehicle choice is empty.");
            return false;
        }
        else if(petsYes_radio.getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Pets choice is empty.");
            return false;
        }
        else if(videokeYes_radio.getToggleGroup().getSelectedToggle() == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Videoke choice is empty.");
            return false;
        }
        else if(partial_paymentString.isEmpty()){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Partial payment is empty.");
            return false;
        }
        else if(checkInLocalDate == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date is empty.");
            return false;
        }
        else if(checkOutLocalDate == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-out date is empty.");
            return false;
        }
        else if(room == null){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Room choice is empty.");
            return false;
        }
        else if(checkInLocalDate.isAfter(checkOutLocalDate)){
            Model.getInstance().getViewFactory().showErrorPopup("Error: Check-in date must come before Check-out date.");
            return false;
        }
        else if(!available.contains(room)){
            Model.getInstance().getViewFactory().showErrorPopup("Error: " + Rooms.abbvToDisplay(room) + " is unavailable for " + checkInLocalDate + " - " + checkOutLocalDate + ".");
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
            String checkInString = checkIn_datePicker.getValue().toString();
            String checkOutString = checkOut_datePicker.getValue().toString();



            String sql = String.format("UPDATE main SET " +
                            "name = '%s', " +
                            "pax = %d, " +
                            "vehicle = %b, " +
                            "pets = %b, " +
                            "videoke = %b, " +
                            "partial_payment = %.2f, " +
                            "checkIn = '%s', " +
                            "checkOut = '%s', " +
                            "room = '%s' " +
                            "WHERE id = '%d';",
                    name, paxInt, vehicle, pets, videoke, partial_paymentDouble, checkInString, checkOutString, room, id);

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


    public enum OrderCategory{
        ID,
        ENTRYDATE,
        NAME,
        PAX,
        VEHICLE,
        PETS,
        VIDEOKE,
        PAYMENT,
        CHECKIN,
        CHECKOUT,
        ROOM,
        USER;
    }

    public enum OrderDirection{
        ASC,
        DESC;
    }

    public static List<List<String>> queryViewList(OrderCategory orderCategory, OrderDirection orderDirection, int page){
        List<List<String>> result = new ArrayList<>();
        String sql = "SELECT * FROM main ORDER BY id DESC limit 15";
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String dateInserted = resultSet.getString("dateInserted");
                String name = resultSet.getString("name");
                int pax = resultSet.getInt("pax");
                boolean vehicle = resultSet.getBoolean("vehicle");
                boolean pets = resultSet.getBoolean("pets");
                boolean videoke = resultSet.getBoolean("videoke");
                double partial_payment = resultSet.getDouble("partial_payment");
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");
                String room = resultSet.getString("room");
                String user = resultSet.getString("user");

//                System.out.println();
//                System.out.println(id + ", " + dateInserted+ ", " + name + ", " + pax + ", " + vehicle + ", " +
//                        pets + ", " + videoke + ", " + partial_payment + ", "
//                        + checkInString + ", " + checkOutString + ", " + room + ", " + user);
                List<String> row = new ArrayList<>();
                row.add(Integer.toString(id));
                row.add(dateInserted);
                row.add(name);
                row.add(Integer.toString(pax));
                row.add(vehicle ? "Yes" : "No");
                row.add(pets ? "Yes" : "No");
                row.add(videoke ? "Yes" : "No");
                row.add(Double.toString(partial_payment));
                row.add(checkInString);
                row.add(checkOutString);
                row.add(room);
                row.add(user);
                result.add(row);
            }
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static List<List<String>> queryViewList(){
        List<List<String>> result = new ArrayList<>();
        String sql = "SELECT * FROM main ORDER BY id DESC limit 15";
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
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");
                String room = resultSet.getString("room");
                String user = resultSet.getString("user");

//                System.out.println();
//                System.out.println(id + ", " + dateInserted+ ", " + name + ", " + pax + ", " + vehicle + ", " +
//                        pets + ", " + videoke + ", " + partial_payment + ", "
//                        + checkInString + ", " + checkOutString + ", " + room + ", " + user);
                List<String> row = new ArrayList<>();
                row.add(Integer.toString(id));
                row.add(dateInserted);
                row.add(name);
                row.add(Integer.toString(pax));
                row.add(Integer.toString(vehicle));
                row.add(pets ? "Yes" : "No");
                row.add(videoke ? "Yes" : "No");
                row.add(Double.toString(partial_payment));
                row.add(checkInString);
                row.add(checkOutString);
                row.add(room);
                row.add(user);
                result.add(row);
            }
            resultSet.close();
            closeDB();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }




    public static boolean deleteEntry(int id){
        String sql = String.format("DELETE FROM main WHERE id = %d", id);
        System.out.println("sql = " + sql);

        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            closeDB();
            Model.getInstance().getViewFactory().showConfirmPopup("Row successfully deleted.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Model.getInstance().getViewFactory().showErrorPopup("Failed to delete row: " + e);
            return false;
        }
    }
}