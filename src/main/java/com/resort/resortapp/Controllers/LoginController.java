package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Views.LoginView;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public PasswordField password_field;
    public Button login_btn;
    public Text error_txt;
    public Button exit_btn;
    Model model;
    LoginView loginView;

    public void initializeDependencies(Model model, LoginView loginView) {
        this.model = model;
        this.loginView = loginView;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(event -> {
            Stage stage = (Stage)login_btn.getScene().getWindow();

        });
    }
    public void updateView(Stage stage){
        stage.setScene(loginView.getSceneLogin());
    }
}