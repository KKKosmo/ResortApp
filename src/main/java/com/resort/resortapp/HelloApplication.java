package com.resort.resortapp;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        Image icon16 = new Image(getClass().getResourceAsStream("/Images/logo.png"));
        Image icon32 = new Image(getClass().getResourceAsStream("/Images/logo.png"));
        Image icon64 = new Image(getClass().getResourceAsStream("/Images/logo.png"));

        stage.getIcons().addAll(icon16, icon32, icon64);
        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}