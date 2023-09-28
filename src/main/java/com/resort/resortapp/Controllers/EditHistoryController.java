package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.EditHistoryModel;
import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EditHistoryController implements Initializable {
    public Button back_btn;
    public TableView<EditHistoryModel> tableview;
    public TableColumn<EditHistoryModel, Integer> editId_col;
    public TableColumn<EditHistoryModel, Integer> bookId_col;
    public TableColumn<EditHistoryModel, String> dateTime_col;
    public TableColumn<EditHistoryModel, String> changes_col;
    public TableColumn<EditHistoryModel, String> user_col;
    public AnchorPane parent;
    List<EditHistoryModel> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list = sqliteModel.getEditHistory();

        back_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().setSceneTable());

        editId_col.setComparator(Integer::compareTo);
        editId_col.setSortType(TableColumn.SortType.DESCENDING);
        tableview.getSortOrder().add(editId_col);

        editId_col.setCellValueFactory(new PropertyValueFactory<>("editId"));
        bookId_col.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        dateTime_col.setCellValueFactory(new PropertyValueFactory<>("time"));
        changes_col.setCellValueFactory(new PropertyValueFactory<>("changes"));
        user_col.setCellValueFactory(new PropertyValueFactory<>("user"));

        Callback<TableColumn<EditHistoryModel, String>, TableCell<EditHistoryModel, String>> cellFactory = col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setAlignment(Pos.CENTER);
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER);
                }
            }
        };
        Callback<TableColumn<EditHistoryModel, Integer>, TableCell<EditHistoryModel, Integer>> integerCellFactory = col -> new TableCell<>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(Integer.toString(item));
                            setStyle("-fx-alignment: CENTER;");
                        }
                    }
                };

        editId_col.setCellFactory(integerCellFactory);
        bookId_col.setCellFactory(integerCellFactory);
        user_col.setCellFactory(cellFactory);

        tableview.getItems().setAll(list);
        tableview.sort();
    }



    public List<EditHistoryModel> getList() {
        return list;
    }

    public void setList(ObservableList<EditHistoryModel> list) {
        this.list = list;
    }
}