package com.resort.resortapp.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class LoginView {
    private Scene login;

    public Scene getSceneLogin() {
        if (login == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
                login = new Scene(loader.load());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return login;
    }
}