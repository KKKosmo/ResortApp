package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import com.resort.resortapp.Rooms;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
    public Button add_btn;
    public ComboBox<String> yearMonth_box;
    public CheckBox e_chkBox;
    public Button dateClear_btn;
    DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.US);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        System.out.println("INITIALIZE");
        Model.getInstance().getViewFactory().setListTable(gridPane);
//        myInit();


        j_chkBox.setSelected(Model.getInstance().isTableJFilter());
        g_chkBox.setSelected(Model.getInstance().isTableGFilter());
        a_chkBox.setSelected(Model.getInstance().isTableAFilter());
        k1_chkBox.setSelected(Model.getInstance().isTableK1Filter());
        k2_chkBox.setSelected(Model.getInstance().isTableK2Filter());
        e_chkBox.setSelected(Model.getInstance().isTableEFilter());

        searchBar_fld.setText(Model.getInstance().getNameFilter());

        startDate_datePicker.setValue(Model.getInstance().getTableStartDate());
        endDate_datePicker.setValue(Model.getInstance().getTableEndDate());

        currentPage_txt.setText(String.valueOf(Model.getInstance().getCurrentPage()));
        page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()));


        LocalDate currentDate = LocalDate.now();

        LocalDate startDate = currentDate.plusMonths(3);

        yearMonth_box.getItems().add("ALL");
        yearMonth_box.setValue("ALL");

        while (startDate.isAfter(YearMonth.of(2023, 9).atEndOfMonth())) {
            String monthYearString = startDate.format(monthYearFormatter);
            yearMonth_box.getItems().add(monthYearString);
            startDate = startDate.minusMonths(1);
        }

        yearMonth_box.getItems().add("Sep 2023");

        yearMonth_box.valueProperty().addListener(yearMonthListener);


        if(!Model.getInstance().isASC() && Model.getInstance().getOrderCategory() != Model.OrderCategory.ID){
            if (Model.getInstance().isASC()) {
                sort_icon.setGlyphName("SORT_UP");
            } else {
                sort_icon.setGlyphName("SORT_DOWN");
            }
//            System.out.println(Model.getInstance().getOrderCategory() + "===================================");
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

        burger_btn.setOnAction(actionEvent -> escMenu.setVisible(!escMenu.isVisible()));

        parentPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                burger_btn.fire();
            }
        });
        searchBar_fld.getParent().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                parentPane.requestFocus();
                event.consume();
            }
        });
        page_fld.getParent().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                parentPane.requestFocus();
                event.consume();
            }
        });

        add_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().setSceneCreate());

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

        startDate_datePicker.valueProperty().addListener(startDateListener);

        endDate_datePicker.valueProperty().addListener(endDateListener);


        prevPage_btn.setOnAction(actionEvent -> page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()-1)));
        nextPage_btn.setOnAction(actionEvent -> page_fld.setText(String.valueOf(Model.getInstance().getCurrentPage()+1)));
        page_fld.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                if(!newValue.equals(oldValue)){
                    if (!newValue.matches("\\d*")) {
                        page_fld.setText(newValue.replaceAll("\\D", ""));
                    }
                    else{
                        int temp = Integer.parseInt(newValue);
                        if(Model.getInstance().getMaxPage() == 0){
                            page_fld.setText("0");
                            currentPage_txt.setText("0");
                            lastPage_txt.setText("0");
                        }
                        else{
                            if(temp < 1){
                                temp = 1;
                                page_fld.setText(String.valueOf(temp));
                            } else if (temp > Model.getInstance().getMaxPage()) {
                                temp = Model.getInstance().getMaxPage();
                                page_fld.setText(String.valueOf(temp));
                            }
                            else{
                                currentPage_txt.setText(String.valueOf(temp));
                                Model.getInstance().setCurrentPage(temp);
                                Model.getInstance().getViewFactory().insertListRows();
                            }
                        }
                    }
                }
            }
        });


        j_chkBox.selectedProperty().addListener(jCheckBoxListener);
        g_chkBox.selectedProperty().addListener(gCheckBoxListener);
        a_chkBox.selectedProperty().addListener(aCheckBoxListener);
        k1_chkBox.selectedProperty().addListener(k1CheckBoxListener);
        k2_chkBox.selectedProperty().addListener(k2CheckBoxListener);

        e_chkBox.selectedProperty().addListener(eCheckBoxListener);

        searchBar_fld.textProperty().addListener(searchBarListener);



        default_btn.setOnAction(actionEvent -> {
            if(Model.getInstance().getViewFactory().showConfirmPopup("Do you really want to reset the settings?")){
                clear();
            }
        });

        dateClear_btn.setOnAction(actionEvent -> {
            if(startDate_datePicker.getValue() != null){
                startDate_datePicker.setValue(null);
            }
            else if(endDate_datePicker.getValue() != null){
                endDate_datePicker.setValue(null);
            }
            yearMonth_box.setValue("ALL");
        });

        export_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().generateReportPDF());

        history_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().setSceneEditHistory());
    }

    ChangeListener<Boolean> jCheckBoxListener = (observable, oldValue, newValue) -> {
        if (newValue) {
            Model.getInstance().getTableRooms().add(Rooms.ROOM_J.getAbbreviatedName());
            untoggleExclusive();
        } else {
            Model.getInstance().getTableRooms().remove(Rooms.ROOM_J.getAbbreviatedName());
        }
        Model.getInstance().setTableJFilter(newValue);
        refreshPage();
    };
    ChangeListener<Boolean> gCheckBoxListener = (observable, oldValue, newValue) -> {
        if (newValue) {
            Model.getInstance().getTableRooms().add(Rooms.ROOM_G.getAbbreviatedName());
            untoggleExclusive();
        } else {
            Model.getInstance().getTableRooms().remove(Rooms.ROOM_G.getAbbreviatedName());
        }
        Model.getInstance().setTableGFilter(newValue);
        refreshPage();
    };
    ChangeListener<Boolean> aCheckBoxListener = (observable, oldValue, newValue) -> {
        if (newValue) {
            Model.getInstance().getTableRooms().add(Rooms.ATTIC.getAbbreviatedName());
            untoggleExclusive();
        } else {
            Model.getInstance().getTableRooms().remove(Rooms.ATTIC.getAbbreviatedName());
        }
        Model.getInstance().setTableAFilter(newValue);
        refreshPage();
    };
    ChangeListener<Boolean> k1CheckBoxListener = (observable, oldValue, newValue) -> {
        if (newValue) {
            Model.getInstance().getTableRooms().add(Rooms.KUBO_1.getAbbreviatedName());
            untoggleExclusive();
        } else {
            Model.getInstance().getTableRooms().remove(Rooms.KUBO_1.getAbbreviatedName());
        }
        Model.getInstance().setTableK1Filter(newValue);
        refreshPage();
    };
    ChangeListener<Boolean> k2CheckBoxListener = (observable, oldValue, newValue) -> {
        if (newValue) {
            Model.getInstance().getTableRooms().add(Rooms.KUBO_2.getAbbreviatedName());
            untoggleExclusive();
        } else {
            Model.getInstance().getTableRooms().remove(Rooms.KUBO_2.getAbbreviatedName());
        }
        Model.getInstance().setTableK2Filter(newValue);
        refreshPage();
    };
    ChangeListener<Boolean> eCheckBoxListener = (observable, oldValue, newValue) -> {
        if(newValue){
            Model.getInstance().getTableRooms().clear();
            Model.getInstance().getTableRooms().add(Rooms.EXCLUSIVE.getAbbreviatedName());
            toggleExclusive(j_chkBox, jCheckBoxListener);
            toggleExclusive(g_chkBox, gCheckBoxListener);
            toggleExclusive(a_chkBox, aCheckBoxListener);
            toggleExclusive(k1_chkBox, k1CheckBoxListener);
            toggleExclusive(k2_chkBox, k2CheckBoxListener);
        }
        else{
            Model.getInstance().getTableRooms().remove(Rooms.EXCLUSIVE.getAbbreviatedName());
        }
        Model.getInstance().setTableEFilter(newValue);
        refreshPage();
    };

    public void toggleExclusive(CheckBox checkBox, ChangeListener<Boolean> listener){
        checkBox.selectedProperty().removeListener(listener);
        checkBox.setSelected(false);
        checkBox.selectedProperty().addListener(listener);
    }

    public void untoggleExclusive(){
        e_chkBox.selectedProperty().removeListener(eCheckBoxListener);
        Model.getInstance().getTableRooms().remove(Rooms.EXCLUSIVE.getAbbreviatedName());
        e_chkBox.setSelected(false);
        e_chkBox.selectedProperty().addListener(eCheckBoxListener);
    }

    ChangeListener<LocalDate> startDateListener = (observable, oldValue, newValue) -> {
        Model.getInstance().setTableStartDate(newValue);
        LocalDate endDateValue = endDate_datePicker.getValue();
        if(newValue != null && endDateValue != null){
            if(newValue.isAfter(endDateValue)){
                Model.getInstance().getViewFactory().showErrorPopup("Start date should not be after end date.");
                startDate_datePicker.setValue(oldValue);
            }
            else{
                if(newValue.withDayOfMonth(1).equals(newValue)
                        &&
                        newValue.withDayOfMonth(newValue.lengthOfMonth()).equals(endDateValue)){
                    Model.getInstance().setTableYearMonth(YearMonth.of(newValue.getYear(), newValue.getMonthValue()));
                }
                else{
                    yearMonth_box.setValue(null);
                    Model.getInstance().setTableYearMonth(null);
                }
                refreshPage();
            }
        }
    };

    ChangeListener<LocalDate> endDateListener = (observable, oldValue, newValue) -> {
        Model.getInstance().setTableEndDate(newValue);
        LocalDate startDateValue = startDate_datePicker.getValue();
        if(startDateValue != null && newValue != null){
            if(startDateValue.isAfter(newValue)){
                Model.getInstance().getViewFactory().showErrorPopup("Start date should not be after end date.");
                endDate_datePicker.setValue(oldValue);
            }
            else{
                if(newValue.withDayOfMonth(newValue.lengthOfMonth()).equals(newValue)
                        &&
                        newValue.withDayOfMonth(1).equals(startDateValue)
                ){
                    Model.getInstance().setTableYearMonth(YearMonth.of(newValue.getYear(), newValue.getMonthValue()));
                }
                else{
                    yearMonth_box.setValue(null);
                    Model.getInstance().setTableYearMonth(null);
                }
                refreshPage();
            }
        }
    };

    ChangeListener<String> yearMonthListener = (observable, oldValue, newValue) -> {
        if(newValue != null){
            startDate_datePicker.setValue(null);
            endDate_datePicker.setValue(null);
            if(newValue.equals("ALL")){
                Model.getInstance().setTableYearMonth(null);
                refreshPage();
            }
            else{
                YearMonth yearMonth = YearMonth.parse(newValue, monthYearFormatter);
                startDate_datePicker.setValue(yearMonth.atDay(1));
                endDate_datePicker.setValue(yearMonth.atEndOfMonth());
            }
        }
    };

    ChangeListener<String> searchBarListener = (observable, oldValue, newValue) -> {
        Model.getInstance().setNameFilter(newValue);
        refreshPage();
    };



    public void clear(){
        //sort
        if(Model.getInstance().getOrderCategory() != Model.OrderCategory.ID){
            if(!id_pane.getChildren().contains(sort_icon)){
                id_pane.getChildren().add(sort_icon);
            }
            Model.getInstance().setOrderCategory(Model.OrderCategory.ID);
        }
        Model.getInstance().setASC(false);
        sort_icon.setGlyphName("SORT_DOWN");


        //date
        startDate_datePicker.valueProperty().removeListener(startDateListener);
        Model.getInstance().setTableStartDate(null);
        startDate_datePicker.setValue(null);
        startDate_datePicker.valueProperty().addListener(startDateListener);

        endDate_datePicker.valueProperty().removeListener(endDateListener);
        Model.getInstance().setTableEndDate(null);
        endDate_datePicker.setValue(null);
        endDate_datePicker.valueProperty().addListener(endDateListener);

        yearMonth_box.valueProperty().removeListener(yearMonthListener);
        Model.getInstance().setTableYearMonth(null);
        yearMonth_box.setValue("ALL");
        yearMonth_box.valueProperty().addListener(yearMonthListener);


        //rooms
        j_chkBox.selectedProperty().removeListener(jCheckBoxListener);
        Model.getInstance().getTableRooms().remove(Rooms.ROOM_J.getAbbreviatedName());
        j_chkBox.setSelected(false);
        j_chkBox.selectedProperty().addListener(jCheckBoxListener);

        g_chkBox.selectedProperty().removeListener(gCheckBoxListener);
        Model.getInstance().getTableRooms().remove(Rooms.ROOM_G.getAbbreviatedName());
        g_chkBox.setSelected(false);
        g_chkBox.selectedProperty().addListener(gCheckBoxListener);

        a_chkBox.selectedProperty().removeListener(aCheckBoxListener);
        Model.getInstance().getTableRooms().remove(Rooms.ATTIC.getAbbreviatedName());
        a_chkBox.setSelected(false);
        a_chkBox.selectedProperty().addListener(aCheckBoxListener);

        k1_chkBox.selectedProperty().removeListener(k1CheckBoxListener);
        Model.getInstance().getTableRooms().remove(Rooms.KUBO_1.getAbbreviatedName());
        k1_chkBox.setSelected(false);
        k1_chkBox.selectedProperty().addListener(k1CheckBoxListener);

        k2_chkBox.selectedProperty().removeListener(k2CheckBoxListener);
        Model.getInstance().getTableRooms().remove(Rooms.KUBO_2.getAbbreviatedName());
        k2_chkBox.setSelected(false);
        k2_chkBox.selectedProperty().addListener(k2CheckBoxListener);

        e_chkBox.selectedProperty().removeListener(eCheckBoxListener);
        Model.getInstance().getTableRooms().remove(Rooms.EXCLUSIVE.getAbbreviatedName());
        e_chkBox.setSelected(false);
        e_chkBox.selectedProperty().addListener(eCheckBoxListener);


        //searchbar
        searchBar_fld.textProperty().removeListener(searchBarListener);
        Model.getInstance().setNameFilter("");
        searchBar_fld.setText("");
        searchBar_fld.textProperty().addListener(searchBarListener);

//        Model.getInstance().setTableYearMonth(null);
//        sqliteModel.queryTableRecords();
//        Model.getInstance().getViewFactory().insertListRows();
//        lastPage_txt.setText(String.valueOf(Model.getInstance().getMaxPage()));
//        page_fld.setText("1");
//        totalBookings_txt.setText(String.valueOf(Model.getInstance().getRecordCount()));
//        totalPayment_txt.setText(String.valueOf(Model.getInstance().getTotalPayment()));
//        unpaid_txt.setText(String.valueOf(Model.getInstance().getTotalUnpaid()));
        refreshPage();
    }

    public void myInit(){
//        System.out.println("MYINIT");
//        Model.getInstance().getViewFactory().setListTable(gridPane);
        sqliteModel.queryTableRecords();
        Model.getInstance().getViewFactory().insertListRows();
        totalBookings_txt.setText(String.valueOf(Model.getInstance().getRecordCount()));
        totalPayment_txt.setText(String.valueOf(Model.getInstance().getTotalPayment()));
        unpaid_txt.setText(String.valueOf(Model.getInstance().getTotalUnpaid()));

        int maxPage = Model.getInstance().getMaxPage();
        if(maxPage > 0){
            page_fld.setText("1");
        }
        else{
            page_fld.setText("0");
        }
        lastPage_txt.setText(String.valueOf(maxPage));

        if(Model.getInstance().getTableYearMonth() != null){
            yearMonth_box.setValue(Model.getInstance().getTableYearMonth().format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.US)));
        }

        //System.out.println(currentPage_txt.getFont().getFamily());
    }

    public void setEscMenu(){
        escMenu =  Model.getInstance().getViewFactory().getEscMenu(parentPane);
    }
    
    private void refreshPage(){
        sqliteModel.queryTableRecords();
        Model.getInstance().getViewFactory().insertListRows();

//        if(Model.getInstance().getTableRecordModels().size() > 0){
//
//        }
        int maxPage = Model.getInstance().getMaxPage();
        if(maxPage > 0){
            page_fld.setText("1");
        }
        else{
            page_fld.setText("0");
        }
        lastPage_txt.setText(String.valueOf(maxPage));

        totalBookings_txt.setText(String.valueOf(Model.getInstance().getRecordCount()));
        totalPayment_txt.setText(String.valueOf(Model.getInstance().getTotalPayment()));
        unpaid_txt.setText(String.valueOf(Model.getInstance().getTotalUnpaid()));
    }
}