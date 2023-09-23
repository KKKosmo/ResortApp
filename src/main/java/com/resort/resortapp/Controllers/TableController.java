package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TableController implements Initializable {
    public TextField page_fld;
    public Button nextPage_btn;
    public Button prevPage_btn;
    public GridPane gridPane;
    public DatePicker endDate_datePicker;
    public DatePicker startDate_datePicker;
    public TextField searchBar_fld;
    public AnchorPane parentPane;
    public Button burger_btn;
    public AnchorPane escMenu;
    public GridPane header;
    public FontAwesomeIconView sort_icon;
    public HBox id_pane;
    public HBox timeCreated_pane;
    public HBox name_pane;
    public HBox pax_pane;
    public HBox vehicle_pane;
    public HBox pets_pane;
    public HBox videoke_pane;
    public HBox partialPay_pane;
    public HBox fullPay_pane;
    public HBox balance_pane;
    public HBox checkIn_pane;
    public HBox checkOut_pane;
    public HBox room_pane;
    public HBox user_pane;
    public HBox status_pane;
    public List<HBox> hBoxList;
    public Text currentPage_txt;
    public Text lastPage_txt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DONT INIT LIST EVERYIME");
        Model.getInstance().getViewFactory().setListTable(gridPane);
        Model.getInstance().initTableValues();
        sqliteModel.queryTableRecords();
        myInit();
        Model.getInstance().setCurrentPage(1);

        startDate_datePicker.setValue(Model.getInstance().getTableStartDate());
        endDate_datePicker.setValue(Model.getInstance().getTableEndDate());
        lastPage_txt.setText(String.valueOf(Model.getInstance().getMaxPage()));


        burger_btn.setOnAction(actionEvent -> {
            escMenu.setVisible(true);
        });

        hBoxList = new ArrayList<>();
        hBoxList.add(id_pane);
        hBoxList.add(timeCreated_pane);
        hBoxList.add(name_pane);
        hBoxList.add(pax_pane);
        hBoxList.add(vehicle_pane);
        hBoxList.add(pets_pane);
        hBoxList.add(videoke_pane);
        hBoxList.add(partialPay_pane);
        hBoxList.add(fullPay_pane);
        hBoxList.add(balance_pane);
        hBoxList.add(checkIn_pane);
        hBoxList.add(checkOut_pane);
        hBoxList.add(room_pane);
        hBoxList.add(user_pane);
        hBoxList.add(status_pane);


        id_pane.setOnMouseClicked(event -> {

        });
        for(HBox hbox : hBoxList){
            onHover(hbox);
        }

        startDate_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().setTableStartDate(newValue);
            sqliteModel.queryTableRecords();
            Model.getInstance().getViewFactory().insertListRows();
        });
        endDate_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().setTableEndDate(newValue);
            sqliteModel.queryTableRecords();
            Model.getInstance().getViewFactory().insertListRows();
        });
        prevPage_btn.setOnAction(actionEvent -> {
//            Model.getInstance().setCurrentPage(Math.max(0, Model.getInstance().getCurrentPage())-1);
//            page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()));
            page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()-1));
        });
        nextPage_btn.setOnAction(actionEvent -> {
//            Model.getInstance().setCurrentPage(Math.min(Model.getInstance().getMaxPage(), Model.getInstance().getCurrentPage())+1);
//            page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()));
            page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()+1));
        });
        page_fld.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                int temp = Integer.parseInt(newValue);
                if(temp < 1){
                    temp = 1;
                    page_fld.setText(String.valueOf(temp));
                } else if (temp > Model.getInstance().getMaxPage()) {
                    temp = Model.getInstance().getMaxPage();
                    page_fld.setText(String.valueOf(temp));
                }
                else{
                    System.out.println("HERE");
                    currentPage_txt.setText(String.valueOf(temp));
                    Model.getInstance().setCurrentPage(temp);
                    Model.getInstance().getViewFactory().insertListRows();
                }
            }
        });

    }

    public void myInit(){
        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
        Model.getInstance().getViewFactory().insertListRows();
    }
    private void onHover(HBox hBox){
        hBox.setOnMouseEntered(event -> {
//            mediaPlayer.play();
            hBox.getStyleClass().add("pane");
        });

        hBox.setOnMouseExited(event -> {
            hBox.getStyleClass().remove("pane");
        });
    }
}