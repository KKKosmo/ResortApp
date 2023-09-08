package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class VisualsMonthController implements Initializable {
    public FlowPane flowPane;
    public Text year;
    public Text month;
    public Button prev_btn;
    public Button next_btn;
    private void fillFlowPane(){
        Model.getInstance().getViewFactory().fillFlowPane();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setCalendarVariables(flowPane, year, month);
        fillFlowPane();
        next_btn.setOnAction(event ->
                {
                    nextMonth();
                }
        );
        prev_btn.setOnAction(event ->
                {
                    prevMonth();
                }
        );
    }



    public void nextMonth() {
        Model.getInstance().getViewFactory().nextMonth();
    }

    public void prevMonth() {
        Model.getInstance().getViewFactory().prevMonth();
    }
}