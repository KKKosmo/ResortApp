package com.resort.resortapp.Models;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class sqliteModel {

    private static String url = "jdbc:sqlite:src/sqlite.db";
    private static Connection con = null;

    public static Connection openDB() {
        try {
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
