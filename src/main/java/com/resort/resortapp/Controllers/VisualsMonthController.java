package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Views.Calendar;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().setCalendar(new Calendar(flowPane, year, month));
        Model.getInstance().getViewFactory().getCalendar().fillFlowPane();
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
    }

    public void prevMonth() {
    }

}