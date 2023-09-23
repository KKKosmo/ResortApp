package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EscMenuController implements Initializable {
    public Button add;
    public Button calendar;
    public Button list;
    public Button signOut;
    public Button exit;
    public Button burger;
    public AnchorPane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        burger.setOnAction(actionEvent -> {
            pane.setVisible(false);
        });
        add.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneCreate();
            pane.setVisible(false);
        });
        calendar.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneMainMenu();
            pane.setVisible(false);
        });
        list.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneList();
            pane.setVisible(false);
        });
        signOut.setOnAction(actionEvent -> {
            //TODO USERS
        });
        exit.setOnAction(actionEvent -> {
            //TODO EXIT FUNCTION
        });
    }
}
