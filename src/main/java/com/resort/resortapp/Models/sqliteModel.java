package com.resort.resortapp.Models;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class sqliteModel {
    private static Connection con = null;
    public static Connection openDB() {
        try {
            String url = "jdbc:sqlite:src/sqlite.db";
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void closeDB(){
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


        for (int i = 0; i < DayModel.getMonthMaxDate(); i++) {
            result.add(32);
        }
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        String twoDigitMonth = DayModel.getDateFocus().format(monthFormatter);

        String monthStart = DayModel.getDateFocus().getYear() + "-" + twoDigitMonth + "-01";
        String monthEnd = DayModel.getDateFocus().getYear() + "-" + twoDigitMonth + "-" + DayModel.getMonthMaxDate();
        String sql = "SELECT checkIn, checkOut, room FROM main where checkIn <= '" + monthEnd + "' AND checkOut >= '" + monthStart + "';";
//        System.out.println("sql = " + sql);
        try {
            PreparedStatement pStmt = openDB().prepareStatement(sql);
            ResultSet resultSet = pStmt.executeQuery();
            while(resultSet.next()){
                String checkInString = resultSet.getString("checkIn");
                String checkOutString = resultSet.getString("checkOut");
                String room = resultSet.getString("room");

                int startDate = Integer.parseInt(checkInString.substring(8));
                int daysCount = Integer.parseInt(checkOutString.substring(8)) - startDate + 1;
                int paxDerived = switch (room) {
                    case "j" -> 6;
                    case "g" -> 9;
                    case "attic" -> 7;
                    case "k1", "k2" -> 5;
                    default -> 0;
                };

                for(int i = startDate - 1; i < daysCount + startDate - 1; i++){
                    result.set(i, result.get(i) - paxDerived);
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