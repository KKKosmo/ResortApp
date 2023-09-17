package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    public AnchorPane parentPane;
    public Button burger_btn;
    public AnchorPane escMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
        burger_btn.setOnAction(actionEvent -> {
            escMenu.setVisible(true);
        });
        Model.getInstance().getViewFactory().insertListRows(gridPane, sqliteModel.queryViewList());
        seeCalendar_btn.setOnAction(event ->
        {
            Model.getInstance().getViewFactory().setSceneMainMenu();
        });
        add_btn.setOnAction(event ->
        {
            Model.getInstance().getViewFactory().setSceneCreate();
        });
    }
}