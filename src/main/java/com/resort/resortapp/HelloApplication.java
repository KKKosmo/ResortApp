package com.resort.resortapp;
import com.resort.resortapp.Controllers.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage){
        LoginController loginController = new LoginController();
        loginController.setSceneLogin(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}