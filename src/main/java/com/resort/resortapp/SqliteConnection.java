package com.resort.resortapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {

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





//    public static Connection Connector(){
//        try{
//            Class.forName("org.sqlite.JDBC");
//            Connection conn = DriverManager.getConnection("jdbc:sqlite:EmployeeDb.sqlite");
//            return conn;
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
}
