package com.resort.resortapp.Models;

import com.resort.resortapp.Rooms;
import com.resort.resortapp.Views.ViewFactory;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;


public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    Rooms rooms = Rooms.ALL_ROOMS;
    private int dateOffset;
    private int monthMaxDate;
    private int year;
    private int monthValue;
    private Month month;
    private LocalDate dateFocus;

    private LocalDate calendarLeftDate;
    private LocalDate calendarRightDate;

    private LocalDate selectedLeftDate;
    private LocalDate selectedRightDate;

    List<LocalDate> selectedLocalDates;

    private List<Set<String>> availableRoomsPerDayWithinTheMonthsList;


    private List<RecordModel> tableRecordModels;

    private LocalDate tableStartDate;
    private LocalDate tableEndDate;
    private int currentPage = 1;
    private int maxPage;
    private int startIndex;
    private int endIndex;
    private Set<String> tableRooms = new HashSet<>();
    private String nameFilter = "";

    public enum OrderCategory{
        ID("id"),
        DATEINSERTED("dateInserted"),
        NAME("lower(name)"),
        PAX("pax"),
        VEHICLE("vehicle"),
        PETS("pets"),
        VIDEOKE("videoke"),
        PARTIALPAYMENT("partial_payment"),
        FULLPAYMENT("full_payment"),
        BALANCE("(full_payment - partial_payment)"),
        STATUS("paid"),
        CHECKIN("checkIn"),
        CHECKOUT("checkOut"),
        ROOM("room"),
        USER("lower(user)");

        final private String string;

        OrderCategory(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    private OrderCategory orderCategory = OrderCategory.ID;
    private boolean ASC = false;
    private Double totalPayment = 0.0;
    private int totalUnpaid = 0;
    private int recordCount = 0;
    private int tableRowCount = 15;
    private boolean tableJFilter = false;
    private boolean tableGFilter = false;
    private boolean tableAFilter = false;
    private boolean tableK1Filter = false;
    private boolean tableK2Filter = false;
//    private enum user{
//        noUser,
//        user1,
//        user2,
//        user3,
//        user4,
//        user5;
//    }
//    private user currentUser = user.noUser;
    private Model(){
        this.viewFactory = new ViewFactory();
    }
    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }
    public ViewFactory getViewFactory(){
        return  viewFactory;
    }
    public void setCalendarVariables(FlowPane flowPane, Text year, Text month, Text roomText) {
        viewFactory.setCalendarVariables(flowPane, year, month, roomText);
        setDateFocus();
    }
    public void fillFlowPaneMonths(){
        DayModelSetters();
        viewFactory.fillFlowPaneMonths(rooms);
    }
    public void nextMonth() {
//        System.out.println("NEXXING");
        dateFocus = dateFocus.plusMonths(1);
        setCalendarLeftDate(dateFocus);
        setCalendarRightDate(dateFocus);
        fillFlowPaneMonths();
        getViewFactory().colorize();
        if(selectedLocalDates != null){
            Model.getInstance().getViewFactory().highlight();
        }
    }
    public void prevMonth() {
//        System.out.println("PREVVING");
        dateFocus = dateFocus.minusMonths(1);
        setCalendarLeftDate(dateFocus);
        setCalendarRightDate(dateFocus);
        fillFlowPaneMonths();
        getViewFactory().colorize();
        if(selectedLocalDates != null){
            Model.getInstance().getViewFactory().highlight();
        }
    }

    public int getDateOffset() {
        return dateOffset;
    }

    public void DayModelSetters(){
        year = dateFocus.getYear();
        monthValue = dateFocus.getMonthValue();
        month =  dateFocus.getMonth();
        monthMaxDate = month.maxLength();
        if(year % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }

        dateOffset = dateFocus.withDayOfMonth(1).getDayOfWeek().getValue();

//        dateOffset = ZonedDateTime.of(year, monthValue, 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

        if(dateOffset >= 7){
            dateOffset = 0;
        }
    }

    public int getMonthMaxDate() {
        return monthMaxDate;
    }

    public int getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public LocalDate getDateFocus() {
        return dateFocus;
    }

    public void setDateFocus() {
        dateFocus = LocalDate.now();
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }

    public void setSelected(){
        if (selectedLeftDate != null && selectedRightDate != null) {
            List<LocalDate> result = new ArrayList<>();

            if(selectedLeftDate.isBefore(selectedRightDate)) {
                LocalDate temp = selectedLeftDate;
                while (temp.isBefore(selectedRightDate) || temp.isEqual(selectedRightDate)) {
                    result.add(temp);
                    temp = temp.plusDays(1);
                }
            }else{
                LocalDate temp = selectedRightDate;
                while (temp.isBefore(selectedLeftDate) || temp.isEqual(selectedLeftDate)) {
                    result.add(temp);
                    temp = temp.plusDays(1);
                }
            }
            selectedLocalDates = result;
            Model.getInstance().getViewFactory().highlight();
        }
    }

    //while focusdate > calendarfirstdate

    public LocalDate getSelectedLeftDate() {
        return selectedLeftDate;
    }

    public void setSelectedLeftDate(String selectedLeftDate) {
        this.selectedLeftDate = LocalDate.parse(selectedLeftDate);
    }

    public LocalDate getSelectedRightDate() {
        return selectedRightDate;
    }

    public void setSelectedRightDate(String selectedRightDate) {
        this.selectedRightDate = LocalDate.parse(selectedRightDate);
    }

    public List<LocalDate> getSelectedLocalDates() {
        return selectedLocalDates;
    }

    public List<Set<String>> getAvailableRoomsPerDayWithinTheMonthsList() {
        return availableRoomsPerDayWithinTheMonthsList;
    }

    public void setAvailablesForVisual(List<Set<String>> availableRoomsPerDayWithinTheMonthsList) {
        this.availableRoomsPerDayWithinTheMonthsList = availableRoomsPerDayWithinTheMonthsList;
    }



    public List<RecordModel> getListRecordModels() {
        return tableRecordModels;
    }


    public void initTableDates(){
        LocalDate temp = LocalDate.now();
//        System.out.println("RESETTING EHRE");
        tableStartDate = temp.withDayOfMonth(1);
        tableEndDate = temp.withDayOfMonth(temp.lengthOfMonth());
//        setCurrentPage(1);
//        maxPage = 1;
    }


    public LocalDate getTableStartDate() {
        return tableStartDate;
    }

    public void setTableStartDate(LocalDate tableStartDate) {
        this.tableStartDate = tableStartDate;
    }

    public LocalDate getTableEndDate() {
        return tableEndDate;
    }

    public void setTableEndDate(LocalDate tableEndDate) {
        this.tableEndDate = tableEndDate;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setListRecordModels(List<RecordModel> listRecordModels) {
        tableRecordModels = listRecordModels;
        maxPage = (int) Math.ceil((float) listRecordModels.size() / getTableRowCount());
        endIndex = Math.min(startIndex + getTableRowCount(), tableRecordModels.size());
//        System.out.println("PLSW ORD");
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        this.startIndex = getTableRowCount() * (currentPage - 1);
        this.endIndex = Math.min(startIndex + getTableRowCount(), tableRecordModels.size());
//        System.out.println("SETTING CURRENT OPAEG");
//
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getInitEndIndex(){
        if(endIndex == 0){
            return Math.min(getListRecordModels().size(), getTableRowCount());
        }
        else{
            return endIndex;
        }
    }

    public Set<String> getTableRooms() {
        return tableRooms;
    }

    public void setTableRooms(Set<String> tableRooms) {
        this.tableRooms = tableRooms;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
    }

    public OrderCategory getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(OrderCategory orderCategory) {
        this.orderCategory = orderCategory;
    }

    public boolean isASC() {
        return ASC;
    }

    public void setASC(boolean ASC) {
        this.ASC = ASC;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public int getTotalUnpaid() {
        return totalUnpaid;
    }

    public void setTotalUnpaid(int totalUnpaid) {
        this.totalUnpaid = totalUnpaid;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getTableRowCount() {
        return tableRowCount;
    }

    public void setTableRowCount(int tableRowCount) {
        this.tableRowCount = tableRowCount;
    }

    public boolean isTableJFilter() {
        return tableJFilter;
    }

    public void setTableJFilter(boolean tableJFilter) {
        this.tableJFilter = tableJFilter;
    }

    public boolean isTableGFilter() {
        return tableGFilter;
    }

    public void setTableGFilter(boolean tableGFilter) {
        this.tableGFilter = tableGFilter;
    }

    public boolean isTableAFilter() {
        return tableAFilter;
    }

    public void setTableAFilter(boolean tableAFilter) {
        this.tableAFilter = tableAFilter;
    }

    public boolean isTableK1Filter() {
        return tableK1Filter;
    }

    public void setTableK1Filter(boolean tableK1Filter) {
        this.tableK1Filter = tableK1Filter;
    }

    public boolean isTableK2Filter() {
        return tableK2Filter;
    }

    public void setTableK2Filter(boolean tableK2Filter) {
        this.tableK2Filter = tableK2Filter;
    }

    public LocalDate getCalendarLeftDate() {
        return calendarLeftDate;
    }

    public void setCalendarLeftDate(LocalDate calendarLeftDate) {
        this.calendarLeftDate = calendarLeftDate.withDayOfMonth(1);

    }

    public LocalDate getCalendarRightDate() {
        return calendarRightDate;
    }

    public void setCalendarRightDate(LocalDate calendarRightDate) {
        this.calendarRightDate = calendarRightDate.withDayOfMonth(calendarRightDate.lengthOfMonth());

    }

    public void initCalendarDates(){
        setCalendarLeftDate(LocalDate.now());
        setCalendarRightDate(LocalDate.now());
    }

    public void autoTurnMonth(LocalDate myDate){
        if(myDate.isBefore(getCalendarRightDate())){
//            while (myDate.getMonth() != getDateFocus().getMonth()){
//            }
            int adjust = calendarRightDate.getMonthValue() - myDate.getMonthValue() - 1;
            dateFocus = dateFocus.minusMonths(adjust);
            prevMonth();
        }
        else{
//            while (myDate.getMonth() != getDateFocus().getMonth()){
//                nextMonth();
//            }
            int adjust = myDate.getMonthValue() - calendarRightDate.getMonthValue() + 1;
            dateFocus = dateFocus.plusMonths(adjust);
            prevMonth();
        }
    }
    public void setDateFocus(LocalDate dateFocus) {
        this.dateFocus = dateFocus;
    }

}