package com.resort.resortapp.Models;

import com.resort.resortapp.Views.ViewFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private int dateOffset;
    private int monthMaxDate;
    private int year;
//    private int monthValue;
    private Month month;
    private LocalDate dateFocus;

    private LocalDate calendarLeftDate;
    private LocalDate calendarRightDate;

    private LocalDate selectedLeftDate;
    private LocalDate selectedRightDate;

    List<LocalDate> selectedLocalDates;

    private List<List<String>> availableRoomsPerDayWithinTheMonthsList;


    private List<RecordModel> tableRecordModels;

    private LocalDate tableStartDate;
    private LocalDate tableEndDate;
    private YearMonth tableYearMonth;
    private int currentPage = 0;
    private int maxPage;
    private int startIndex;
    private int endIndex;
    private final Set<String> tableRooms = new HashSet<>();
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
    private final int tableRowCount = 15;
    private boolean tableJFilter = false;
    private boolean tableGFilter = false;
    private boolean tableAFilter = false;
    private boolean tableK1Filter = false;
    private boolean tableK2Filter = false;
    private boolean tableEFilter = false;
    private String user;
    Logger logger = Logger.getLogger("ResortApp");
    String logFilePath = "error.log";
    FileHandler fileHandler;
    public void initLogger(){
        try {
            fileHandler = new FileHandler(logFilePath, 10 * 1024 * 1024, 1, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.SEVERE);
        } catch (Exception e) {
            Model.getInstance().getViewFactory().showErrorPopup(e.toString());
            e.printStackTrace();
        }
    }

    public void printLog(Exception e){
        viewFactory.showErrorPopup(e.toString());
        logger.log(Level.SEVERE, "An error occurred:", e);
    }

    public void closeLogger(){
        logger.removeHandler(fileHandler);
        fileHandler.close();
    }

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
    public void setCalendarVariables(FlowPane flowPane, Text year, Text month) {
        viewFactory.setCalendarVariables(flowPane, year, month);
        setDateFocus(LocalDate.now());
    }
    public void fillFlowPaneMonths(){
        DayModelSetters();
        viewFactory.fillFlowPaneMonths();
    }
    public void nextMonth() {
        //System.out.println("NEXXING");
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
        //System.out.println("PREVVING");
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
//        monthValue = dateFocus.getMonthValue();
        month =  dateFocus.getMonth();
        monthMaxDate = month.maxLength();
        if(year % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }

        dateOffset = dateFocus.withDayOfMonth(1).getDayOfWeek().getValue();

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

    public void setDateFocus(LocalDate localDate) {
        dateFocus = localDate;
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
//    public LocalDate getSelectedLeftDate() {
//        return selectedLeftDate;
//    }
//    public LocalDate getSelectedRightDate() {
//        return selectedRightDate;
//    }

    public void setSelectedLeftDate(String selectedLeftDate) {
        this.selectedLeftDate = LocalDate.parse(selectedLeftDate);
    }

    public void setSelectedRightDate(String selectedRightDate) {
        this.selectedRightDate = LocalDate.parse(selectedRightDate);
    }

    public List<LocalDate> getSelectedLocalDates() {
        return selectedLocalDates;
    }

    public List<List<String>> getAvailableRoomsPerDayWithinTheMonthsList() {
        return availableRoomsPerDayWithinTheMonthsList;
    }

    public void setAvailablesForVisual(List<List<String>> availableRoomsPerDayWithinTheMonthsList) {
        this.availableRoomsPerDayWithinTheMonthsList = availableRoomsPerDayWithinTheMonthsList;
    }

    public List<RecordModel> getListRecordModels() {
        return tableRecordModels;
    }

    public boolean checkTableEdges(){
        return (tableStartDate != null && tableEndDate != null);
    }
//    public void initTableDates(){
//        LocalDate temp = LocalDate.now();
//        tableStartDate = temp.withDayOfMonth(1);
//        tableEndDate = temp.withDayOfMonth(temp.lengthOfMonth());
//    }

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

//        System.out.println("+_++++++++++++++++MAX PAGE = " + maxPage);
        endIndex = Math.min(startIndex + getTableRowCount(), tableRecordModels.size());
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        this.startIndex = getTableRowCount() * (currentPage - 1);
        this.endIndex = Math.min(startIndex + getTableRowCount(), tableRecordModels.size());
//        System.out.println("SETTING CURRENT OPAEG");
//
    }

    public List<RecordModel> getTableRecordModels() {
        return tableRecordModels;
    }

    public int getMaxPage() {
        return maxPage;
    }

//    public void setMaxPage(int maxPage) {
//        this.maxPage = maxPage;
//    }

    public int getStartIndex() {
        return startIndex;
    }

//    public int getEndIndex() {
//        return endIndex;
//    }

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

//    public void setTableRooms(Set<String> tableRooms) {
//        this.tableRooms = tableRooms;
//    }

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

//    public void setTableRowCount(int tableRowCount) {
//        this.tableRowCount = tableRowCount;
//    }

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

    public boolean isTableEFilter() {
        return tableEFilter;
    }

    public void setTableEFilter(boolean tableEFilter) {
        this.tableEFilter = tableEFilter;
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
        Model.getInstance().setDateFocus(myDate.plusMonths(1));
        prevMonth();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public YearMonth getTableYearMonth() {
        return tableYearMonth;
    }

    public void setTableYearMonth(YearMonth tableYearMonth) {
        this.tableYearMonth = tableYearMonth;
    }
}