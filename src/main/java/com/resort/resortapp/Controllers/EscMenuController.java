package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EscMenuController implements Initializable {
    public Button add;
    public Button list;
    public Button signOut;
    public Button exit;
    public Button burger;
    public AnchorPane pane;
    public Text user_txt;
    public Button editHistory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        burger.setOnAction(actionEvent -> pane.setVisible(false));
        add.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneCreate();
            pane.setVisible(false);
        });
        list.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneTable(false);
            pane.setVisible(false);
        });
        signOut.setOnAction(actionEvent -> {
            pane.setVisible(false);
            Model.getInstance().getViewFactory().setSceneLogin();
        });
        exit.setOnAction(actionEvent -> Platform.exit());
        editHistory.setOnAction(actionEvent -> {
            pane.setVisible(false);
            Model.getInstance().getViewFactory().setSceneEditHistory();
        });
        user_txt.setText(Model.getInstance().getUser());
    }

    public void setUser(String user){
        user_txt.setText(user);
    }
}
