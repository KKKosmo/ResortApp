package com.resort.resortapp.Models;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
//        System.out.println(dateOffset);

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

                System.out.println("startDate = " + startDate);
                System.out.println("daysCount = " + daysCount);
                System.out.println(result);
                System.out.println(checkInString + ", " + checkOutString + ", " + rooms.getDisplayName());
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
    public static void insertRecord(String name, int pax, boolean vehicle, boolean pets, boolean videoke, double partial_payment, String checkIn, String checkOut, String room){

        String sql = String.format("INSERT INTO main (name, pax, vehicle, pets, videoke, partial_payment, checkIn, checkOut, room) " +
                        "VALUES ('%s', %d, %b, %b, %b, %.2f, '%s', '%s', '%s');",
            name, pax, vehicle, pets, videoke, partial_payment, checkIn, checkOut, room);

            System.out.println("sql = " + sql);
        try {
            //open db
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            pStmt.executeUpdate();

            closeDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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