package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.EditHistoryModel;
import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditHistoryController implements Initializable {
    public Button back_btn;
    List<EditHistoryModel> list = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = sqliteModel.getEditHistory();

        back_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneTable();
        });

        //foreach in list, add to table
    }


    public List<EditHistoryModel> getList() {
        return list;
    }

    public void setList(List<EditHistoryModel> list) {
        this.list = list;
    }
}
