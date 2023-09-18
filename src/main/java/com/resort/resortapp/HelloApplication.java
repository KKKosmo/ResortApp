package com.resort.resortapp;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import com.resort.resortapp.Views.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage){
        Model.getInstance().getViewFactory().setStage(stage);
        stage.setTitle("J&G Resort App");
        Model.getInstance().getViewFactory().setSceneLogin();
        stage.setResizable(false);
        stage.show();
//        Model.getInstance().generateReportPDF();
    }

    public static void main(String[] args) {

        launch();
    }
}