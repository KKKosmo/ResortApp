package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VisualsMonthController implements Initializable {
    public FlowPane flowPane;
    public Text year;
    public Text month;
    public Button prev_month_btn;
    public Button next_month_btn;
    public Button next_room_btn;
    public Button prev_room_btn;
    public Text room;
    public Button add_btn;

    private void fillFlowPaneMonths(){
        Model.getInstance().fillFlowPaneMonths();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setCalendarVariables(flowPane, year, month, room);
        fillFlowPaneMonths();
        next_month_btn.setOnAction(event ->
                {
                    nextMonth();
                }
        );
        prev_month_btn.setOnAction(event ->
                {
                    prevMonth();
                }
        );
        next_room_btn.setOnAction(event ->{
            nextRoom();
        });
        prev_room_btn.setOnAction(event ->{
            prevRoom();
        });
        add_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneCreate();
        });
    }

    public void nextMonth() {
        Model.getInstance().nextMonth();
    }
    public void prevMonth() {
        Model.getInstance().prevMonth();
    }
    public void nextRoom() {
        Model.getInstance().nextRoom();
    }
    public void prevRoom() {
        Model.getInstance().prevRoom();
    }
}