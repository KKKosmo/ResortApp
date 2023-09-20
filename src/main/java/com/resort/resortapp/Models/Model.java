package com.resort.resortapp.Models;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.resort.resortapp.Rooms;
import com.resort.resortapp.Views.ViewFactory;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;



public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    Rooms rooms = Rooms.ALL_ROOMS;
    private int dateOffset;
    private int monthMaxDate;
    private int year;
    private int monthValue;
    private Month month;
    private ZonedDateTime dateFocus;
    private LocalDate leftDate;
    private LocalDate rightDate;
    private Set<Integer> selected;

    private List<Set<String>> availableRoomsPerDayList;

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
        dateFocus = dateFocus.plusMonths(1);
        fillFlowPaneMonths();
    }
    public void prevMonth() {
        dateFocus = dateFocus.minusMonths(1);
        fillFlowPaneMonths();
    }
    public void nextRoom() {
        rooms = rooms.next();
        fillFlowPaneMonths();
    }
    public void prevRoom() {
        rooms = rooms.prev();
        fillFlowPaneMonths();
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
        dateOffset = ZonedDateTime.of(year, monthValue, 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();
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

    public ZonedDateTime getDateFocus() {
        return dateFocus;
    }

    private void setDateFocus() {
        dateFocus = ZonedDateTime.now();
    }

    public Rooms getRooms() {
        return rooms;
    }

    public void setRooms(Rooms rooms) {
        this.rooms = rooms;
    }


    public void setSelected(){
        Set<Integer> result = new HashSet<>();


        if (leftDate != null && rightDate != null) {
            if(leftDate.isBefore(rightDate)){
                LocalDate tempDate = leftDate;
                while (tempDate.isBefore(rightDate) || tempDate.isEqual(rightDate)) {
                    int day = tempDate.getDayOfMonth();
                    if (day <= Model.getInstance().getMonthMaxDate())
                        result.add(day);
                    tempDate = tempDate.plusDays(1);
                }
            }
            else{
                LocalDate tempDate = rightDate;
                while (tempDate.isBefore(leftDate) || tempDate.isEqual(leftDate)) {
                    int day = tempDate.getDayOfMonth();
                    if (day <= Model.getInstance().getMonthMaxDate())
                        result.add(day);
                    tempDate = tempDate.plusDays(1);
                }
            }
        }


        selected = result;


        Model.getInstance().getViewFactory().highlight();
    }

    public LocalDate getLeftDate() {
        return leftDate;
    }

    public void setLeftDate(LocalDate leftDate) {
        this.leftDate = leftDate;
    }

    public LocalDate getRightDate() {
        return rightDate;
    }

    public void setRightDate(LocalDate rightDate) {
        this.rightDate = rightDate;
    }

    public Set<Integer> getSelected() {
        return selected;
    }

    public void setSelected(Set<Integer> selected) {
        this.selected = selected;
    }

    public void generateReportPDF(){
        try {
            String path = "Report.pdf";
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.LETTER);
            Document document = new Document(pdfDocument);

            float col1 = 285f;
            float col2 = 435f;
            float[] headerWidth = {col1, col2};
            Table table = new Table(headerWidth);
            table.addCell(new Cell().add("J&G Resort\n" +
                    "Monthly Report\n" +
                    "August 2023\n" +
                    "Page 1/1").setBold().setTextAlignment(TextAlignment.CENTER));

            Table nestedTable = new Table(new float[]{col1/2, col1/2});
            nestedTable.addCell(new Cell().add("Total Reservations").setBorder(Border.NO_BORDER).setBold());
            nestedTable.addCell(new Cell().add("0").setBorder(Border.NO_BORDER));
            nestedTable.addCell(new Cell().add("Total Guests").setBorder(Border.NO_BORDER).setBold());
            nestedTable.addCell(new Cell().add("0").setBorder(Border.NO_BORDER));
            nestedTable.addCell(new Cell().add("Total Revenue").setBorder(Border.NO_BORDER).setBold());
            nestedTable.addCell(new Cell().add("0 PHP").setBorder(Border.NO_BORDER));

            table.addCell(nestedTable);

            document.add(table);

            Table body = new Table(new float[]{0,0,0,0,0,0,0,0,0,0,0});
            body.addCell(new Cell().add("ID"));
            body.addCell(new Cell().add("Date"));
            body.addCell(new Cell().add("Name"));
            body.addCell(new Cell().add("No. of Heads"));
            body.addCell(new Cell().add("Vehicle"));
            body.addCell(new Cell().add("Pets"));
            body.addCell(new Cell().add("Videoke"));
            body.addCell(new Cell().add("Partial Payment"));
            body.addCell(new Cell().add("Check-In"));
            body.addCell(new Cell().add("Check-Out"));
            body.addCell(new Cell().add("Room"));
            body.addCell(new Cell().add("User"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            body.addCell(new Cell().add("test"));
            document.add(new Paragraph("THIS IS A PREVIEW FILE").setBold().setTextAlignment(TextAlignment.CENTER));
            document.add(body);
            document.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Set<String>> getAvailableRoomsPerDayList() {
        return availableRoomsPerDayList;
    }

    public void setAvailableRoomsPerDayList(List<Set<String>> availableRoomsPerDayList) {
        this.availableRoomsPerDayList = availableRoomsPerDayList;
    }


    public Set<String> getAvailableInRangeInit(LocalDate checkIn, LocalDate checkOut, List<CheckBox> checkBoxList){
        Set<String> rooms = Rooms.manageCheckboxesSetAbbreviatedName(checkBoxList);
        int left = checkIn.getDayOfMonth() - 1;
        int right = checkOut.getDayOfMonth() - 1;

//        System.out.println("Rooms = " + rooms);

        Set<String> result = new HashSet<>();

        for(int i = left; i <= right; i++){

            availableRoomsPerDayList.get(i).addAll(rooms);
            result = availableRoomsPerDayList.get(i);

            Text temp = viewFactory.getDayModelList().get(i + dateOffset).getRoomsText();
            StringBuilder desc = new StringBuilder();
            for(String string : result){
                desc.append(Rooms.abbvToDisplay(string)).append("\n");
            }
            temp.setText(desc.toString());
            temp.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        }
        return result;
    }
    public Set<String> getAvailableInRange(LocalDate checkIn, LocalDate checkOut){
        Set<String> rooms = Rooms.getRoomAbbreviateNamesSet();
        int left = checkIn.getDayOfMonth() - 1;
        int right = checkOut.getDayOfMonth() - 1;

        for(int i = left; i <= right; i++){
            Set<String> result = availableRoomsPerDayList.get(i);
            rooms.retainAll(result);
        }
        return rooms;
    }
}