package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Rooms;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    public Button burger_btn;
    public AnchorPane parentPane;
    public AnchorPane escMenu;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
        burger_btn.setOnAction(actionEvent -> {
            escMenu.setVisible(true);
        });
        done_btn.setOnAction(event ->
        {
            Model.getInstance().getViewFactory().setSceneList();
        });

    }

    public void setValues(String insertedDate, String name, String pax, String vehicle, String pets, String videoke, String payment, String checkIn, String checkOut, String room) {
        dateInserted_txt.setText(insertedDate);
        name_txt.setText(name);
        pax_txt.setText(pax);
        vehicle_txt.setText(vehicle);
        pets_txt.setText(pets);
        videoke_txt.setText(videoke);
        payment_txt.setText(payment);
        checkIn_txt.setText(checkIn);
        checkOut_txt.setText(checkOut);
        room_txt.setText(Rooms.abbvToDisplay(room));
    }
}
