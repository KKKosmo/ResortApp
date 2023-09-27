package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import com.resort.resortapp.Rooms;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
    public CheckBox j_chkBox;
    public CheckBox g_chkBox;
    public CheckBox a_chkBox;
    public CheckBox k1_chkBox;
    public CheckBox k2_chkBox;
    public Button default_btn;
    public Button export_btn;
    public Text totalBookings_txt;
    public Text totalPayment_txt;
    public Text unpaid_txt;
    public HBox totalPayment_hBox;
    public Button history_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("INITIALIZE");

        Model.getInstance().getViewFactory().setListTable(gridPane);
//        myInit();



        //rooms

        j_chkBox.setSelected(Model.getInstance().isTableJFilter());
        g_chkBox.setSelected(Model.getInstance().isTableGFilter());
        a_chkBox.setSelected(Model.getInstance().isTableAFilter());
        k1_chkBox.setSelected(Model.getInstance().isTableK1Filter());
        k2_chkBox.setSelected(Model.getInstance().isTableK2Filter());

        searchBar_fld.setText(Model.getInstance().getNameFilter());

        startDate_datePicker.setValue(Model.getInstance().getTableStartDate());
        endDate_datePicker.setValue(Model.getInstance().getTableEndDate());
        currentPage_txt.setText(String.valueOf(Model.getInstance().getCurrentPage()));
        page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()));


        if(!Model.getInstance().isASC() && Model.getInstance().getOrderCategory() != Model.OrderCategory.ID){
            if (Model.getInstance().isASC()) {
                sort_icon.setGlyphName("SORT_UP");
            } else {
                sort_icon.setGlyphName("SORT_DOWN");
            }
            System.out.println(Model.getInstance().getOrderCategory() + "===================================");
            switch (Model.getInstance().getOrderCategory()) {
                case DATEINSERTED -> timeCreated_pane.getChildren().add(sort_icon);
                case NAME -> name_pane.getChildren().add(sort_icon);
                case PAX -> pax_pane.getChildren().add(sort_icon);
                case VEHICLE -> vehicle_pane.getChildren().add(sort_icon);
                case PETS -> pets_pane.getChildren().add(sort_icon);
                case VIDEOKE -> videoke_pane.getChildren().add(sort_icon);
                case PARTIALPAYMENT -> partialPay_pane.getChildren().add(sort_icon);
                case FULLPAYMENT -> fullPay_pane.getChildren().add(sort_icon);
                case BALANCE -> balance_pane.getChildren().add(sort_icon);
                case STATUS -> status_pane.getChildren().add(sort_icon);
                case CHECKIN -> checkIn_pane.getChildren().add(sort_icon);
                case CHECKOUT -> checkOut_pane.getChildren().add(sort_icon);
                case ROOM -> room_pane.getChildren().add(sort_icon);
                case USER -> user_pane.getChildren().add(sort_icon);
                default -> {
                }
            }
        }



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
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.ID){
                Model.getInstance().setOrderCategory(Model.OrderCategory.ID);
                Model.getInstance().setASC(false);
                id_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        timeCreated_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.DATEINSERTED){
                Model.getInstance().setOrderCategory(Model.OrderCategory.DATEINSERTED);
                Model.getInstance().setASC(false);
                timeCreated_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        name_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.NAME){
                Model.getInstance().setOrderCategory(Model.OrderCategory.NAME);
                Model.getInstance().setASC(true);
                name_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        pax_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.PAX){
                Model.getInstance().setOrderCategory(Model.OrderCategory.PAX);
                Model.getInstance().setASC(false);
                pax_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        vehicle_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.VEHICLE){
                Model.getInstance().setOrderCategory(Model.OrderCategory.VEHICLE);
                Model.getInstance().setASC(false);
                vehicle_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        pets_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.PETS){
                Model.getInstance().setOrderCategory(Model.OrderCategory.PETS);
                Model.getInstance().setASC(false);
                pets_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        videoke_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.VIDEOKE){
                Model.getInstance().setOrderCategory(Model.OrderCategory.VIDEOKE);
                Model.getInstance().setASC(false);
                videoke_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        partialPay_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.PARTIALPAYMENT){
                Model.getInstance().setOrderCategory(Model.OrderCategory.PARTIALPAYMENT);
                Model.getInstance().setASC(false);
                partialPay_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        fullPay_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.FULLPAYMENT){
                Model.getInstance().setOrderCategory(Model.OrderCategory.FULLPAYMENT);
                Model.getInstance().setASC(false);
                fullPay_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        balance_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.BALANCE){
                Model.getInstance().setOrderCategory(Model.OrderCategory.BALANCE);
                Model.getInstance().setASC(false);
                balance_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        status_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.STATUS){
                Model.getInstance().setOrderCategory(Model.OrderCategory.STATUS);
                Model.getInstance().setASC(true);
                status_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        checkIn_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.CHECKIN){
                Model.getInstance().setOrderCategory(Model.OrderCategory.CHECKIN);
                Model.getInstance().setASC(false);
                checkIn_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        checkOut_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.CHECKOUT){
                Model.getInstance().setOrderCategory(Model.OrderCategory.CHECKOUT);
                Model.getInstance().setASC(false);
                checkOut_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        room_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.ROOM){
                Model.getInstance().setOrderCategory(Model.OrderCategory.ROOM);
                Model.getInstance().setASC(true);
                room_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        user_pane.setOnMouseClicked(event -> {
            if(Model.getInstance().getOrderCategory() != Model.OrderCategory.USER){
                Model.getInstance().setOrderCategory(Model.OrderCategory.USER);
                Model.getInstance().setASC(true);
                user_pane.getChildren().add(sort_icon);
            }
            else{
                Model.getInstance().setASC(!Model.getInstance().isASC());
            }
            if(Model.getInstance().isASC()){
                sort_icon.setGlyphName("SORT_UP");
            }
            else{
                sort_icon.setGlyphName("SORT_DOWN");
            }
            refreshPage();
        });

        for(HBox hbox : hBoxList){
            onHover(hbox);
        }

        startDate_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().setTableStartDate(newValue);
            refreshPage();
        });
        endDate_datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().setTableEndDate(newValue);
            refreshPage();
        });
        prevPage_btn.setOnAction(actionEvent -> {
            page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()-1));
        });
        nextPage_btn.setOnAction(actionEvent -> {
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

        j_chkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Model.getInstance().getTableRooms().add(Rooms.ROOM_J.getAbbreviatedName());
            }
            else{
                Model.getInstance().getTableRooms().remove(Rooms.ROOM_J.getAbbreviatedName());
            }
            Model.getInstance().setTableJFilter(newValue);
            refreshPage();

        });
        g_chkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Model.getInstance().getTableRooms().add(Rooms.ROOM_G.getAbbreviatedName());
            }
            else{
                Model.getInstance().getTableRooms().remove(Rooms.ROOM_G.getAbbreviatedName());
            }
            Model.getInstance().setTableGFilter(newValue);
            refreshPage();
        });
        a_chkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Model.getInstance().getTableRooms().add(Rooms.ATTIC.getAbbreviatedName());
            }
            else{
                Model.getInstance().getTableRooms().remove(Rooms.ATTIC.getAbbreviatedName());
            }
            Model.getInstance().setTableAFilter(newValue);
            refreshPage();
        });
        k1_chkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Model.getInstance().getTableRooms().add(Rooms.KUBO_1.getAbbreviatedName());
            }
            else{
                Model.getInstance().getTableRooms().remove(Rooms.KUBO_1.getAbbreviatedName());
            }
            Model.getInstance().setTableK1Filter(newValue);
            refreshPage();
        });
        k2_chkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Model.getInstance().getTableRooms().add(Rooms.KUBO_2.getAbbreviatedName());
            }
            else{
                Model.getInstance().getTableRooms().remove(Rooms.KUBO_2.getAbbreviatedName());
            }
            Model.getInstance().setTableK2Filter(newValue);
            refreshPage();
        });

        searchBar_fld.textProperty().addListener((observable, oldValue, newValue) -> {
            Model.getInstance().setNameFilter(newValue);
            refreshPage();
        });

        default_btn.setOnAction(actionEvent -> {
            if(Model.getInstance().getViewFactory().showConfirmPopup("Do you really want to reset the settings?")){
                //sort
                if(Model.getInstance().getOrderCategory() != Model.OrderCategory.ID){
                    id_pane.getChildren().add(sort_icon);
                    Model.getInstance().setOrderCategory(Model.OrderCategory.ID);
                }
                Model.getInstance().setASC(false);
                sort_icon.setGlyphName("SORT_DOWN");


                //date
                Model.getInstance().initTableDates();
                startDate_datePicker.setValue(Model.getInstance().getTableStartDate());
                endDate_datePicker.setValue(Model.getInstance().getTableEndDate());

                //rooms
                j_chkBox.setSelected(false);
                g_chkBox.setSelected(false);
                a_chkBox.setSelected(false);
                k1_chkBox.setSelected(false);
                k2_chkBox.setSelected(false);

                //searchbar
                searchBar_fld.setText("");


                sqliteModel.queryTableRecords();
                Model.getInstance().getViewFactory().insertListRows();
            }
        });

        export_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().generateReportPDF();
        });

        history_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().setSceneEditHistory();
        });
    }

    public void myInit(){
//        System.out.println("MYINIT");
//        Model.getInstance().getViewFactory().setListTable(gridPane);
        sqliteModel.queryTableRecords();
        Model.getInstance().getViewFactory().insertListRows();
        totalBookings_txt.setText(String.valueOf(Model.getInstance().getRecordCount()));
        totalPayment_txt.setText(String.valueOf(Model.getInstance().getTotalPayment()));
        unpaid_txt.setText(String.valueOf(Model.getInstance().getTotalUnpaid()));
        lastPage_txt.setText(String.valueOf(Model.getInstance().getMaxPage()));
    }

    public void setEscMenu(){
        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
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
    
    private void refreshPage(){
        sqliteModel.queryTableRecords();
        Model.getInstance().getViewFactory().insertListRows();
        lastPage_txt.setText(String.valueOf(Model.getInstance().getMaxPage()));
        page_fld.setText("1");
        totalBookings_txt.setText(String.valueOf(Model.getInstance().getRecordCount()));
        totalPayment_txt.setText(String.valueOf(Model.getInstance().getTotalPayment()));
        unpaid_txt.setText(String.valueOf(Model.getInstance().getTotalUnpaid()));
    }
}