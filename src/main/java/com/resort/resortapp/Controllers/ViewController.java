package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    public Text dateInserted_txt;
    public Text name_txt;
    public Text pax_txt;
    public Text vehicle_txt;
    public Text pets_txt;
    public Text videoke_txt;
    public Text payment_txt;
    public Text checkIn_txt;
    public Text checkOut_txt;
    public Text room_txt;
    public FlowPane month_pane;
    public Button done_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        done_btn.setOnAction(event ->
        {
            Model.getInstance().getViewFactory().setSceneMainMenu();
        });

    }

}
