package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable{

    public FlowPane flowPane;
    public Text year;
    public Text month;
    public Button prev_month_btn;
    public Button next_month_btn;

    private void fillFlowPaneMonths(){
        Model.getInstance().fillFlowPaneMonths();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setCalendarVariables(flowPane, year, month);
        fillFlowPaneMonths();
        next_month_btn.setOnAction(event -> Model.getInstance().nextMonth());
        prev_month_btn.setOnAction(event -> Model.getInstance().prevMonth());
    }

}