package com.resort.resortapp;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage){
        Model.getInstance().initLogger();
        sqliteModel.sqlInit();
        Model.getInstance().getViewFactory().setStage(stage);
        stage.setTitle("J&G Resort App");
        Model.getInstance().initCalendarDates();
//        Model.getInstance().getViewFactory().setSceneTable(true)
        Model.getInstance().getViewFactory().setSceneLogin();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> Model.getInstance().closeLogger()));
        stage.setOnCloseRequest(event -> {
            event.consume();
                if (Model.getInstance().getViewFactory().showConfirmPopup("Are you sure you want to exit?")) {
                    Platform.exit();
                }
        });

        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}