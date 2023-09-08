package com.resort.resortapp;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Views.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage){
        stage.setTitle("J&G Resort App");
        stage.setScene(Model.getInstance().getViewFactory().getSceneLogin());
        stage.setResizable(false);
        stage.show();


        if(SqliteConnection.openDB() != null){
            System.out.println("Connected!");
            SqliteConnection.closeDB();
        }
        else{
            System.out.println("CNAT CONNETC");
        }
    }

    public static void main(String[] args) {

        launch();
    }
}