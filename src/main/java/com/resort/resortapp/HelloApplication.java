package com.resort.resortapp;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage){
        Model.getInstance().getViewFactory().setStage(stage);
        stage.setTitle("J&G Resort App");
        Model.getInstance().initTableDates();
        Model.getInstance().initCalendarDates();
        Model.getInstance().getViewFactory().setSceneLogin();
//        Model.getInstance().setCurrentPage(1);

        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {

        launch();
    }
}