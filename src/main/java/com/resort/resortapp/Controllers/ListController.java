package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    public TextField room_inputFld;
    public Button nextPage_btn;
    public Button prevPage_btn;
    public Button seeCalendar_btn;
    public Button add_btn;
    public GridPane gridPane;
    public Text room_text;
    public Button nextRoom_btn;
    public Button prevRoom_btn;
    public DatePicker endDate_datePicker;
    public DatePicker startDate_datePicker;
    public TextField searchBar_fld;
    public Button back_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().insertListRows(gridPane);
    }


}
