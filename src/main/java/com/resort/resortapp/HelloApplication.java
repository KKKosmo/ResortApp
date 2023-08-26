package com.resort.resortapp;

import com.resort.resortapp.Controllers.LoginController;
import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Views.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage){
        Model model = new Model();
        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController();
        loginController.initializeDependencies(model, loginView);



        stage.setTitle("J&G Resort App");
        stage.setMaximized(true);
        stage.setResizable(false);
//        stage.initStyle(StageStyle.UNDECORATED);
        loginController.updateView(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}