package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    public Button add_btn;
    public AnchorPane anchorPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneCreate();
        });
        Model.getInstance().getViewFactory().insertCalendar(anchorPane);
        Model.getInstance().getViewFactory().setClickable();
    }
}