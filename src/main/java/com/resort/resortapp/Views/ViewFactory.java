package com.resort.resortapp.Views;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.resort.resortapp.Controllers.EditController;
import com.resort.resortapp.Controllers.TableController;
import com.resort.resortapp.Models.*;
import com.resort.resortapp.Rooms;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

public class ViewFactory {
    private AnchorPane escMenu;
    private ZonedDateTime today;
    private Text yearText;
    private Text monthText;
    private Text roomText;
    private FlowPane flowPane;
    private Stage stage;
    List<DayModel> dayModelList = new ArrayList<>();
    List<Node> listTableChildren;

    Scene table;
    GridPane listTable;

    //    TODO listview settings
    Border borderUnselected = new Border(new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.SOLID, null, new BorderWidths(3)));
    Border normalBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(5)));
    public void setCalendarVariables(FlowPane flowPane, Text yearText, Text monthText, Text roomText) {
        this.flowPane = flowPane;
        this.yearText = yearText;
        this.monthText = monthText;
        this.roomText = roomText;
    }
    public AnchorPane getEscMenu(Pane parentPane){
        if (escMenu == null) {
            try {
                escMenu = FXMLLoader.load(getClass().getResource("/Fxml/EscMenu.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        parentPane.getChildren().add(escMenu);
        return escMenu;
    }
    public void setSceneTable() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Table.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if(table == null){
//            try {
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        table = new Scene(root);
        TableController tableController = loader.getController();
        tableController.myInit();
        tableController.setEscMenu();

        Tooltip tooltip = new Tooltip("IF NOT PAID, THEN THE PARTIAL PAYMENT INSTEAD OF THE FULL PAYMENT GETS ADDED TO THE TOTAL PAYMENT");
        tooltip.setShowDelay(Duration.millis(0));
        Tooltip.install(tableController.totalPayment_hBox, tooltip);

        stage.setScene(table);




//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Table.fxml"));
//                stage.setScene(new Scene(loader.load()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


    }
    public void setSceneLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setSceneCreate(){
//        if (create == null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Create.fxml"));
//                create = new Scene(loader.load());
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }
    public void setSceneMainMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/MainMenu.fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertCalendar(Pane pane){
        try {
            AnchorPane childPane = FXMLLoader.load(getClass().getResource("/Fxml/Calendar.fxml"));
            pane.getChildren().add(childPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void fillFlowPaneMonths(Rooms rooms){
        flowPane.getChildren().clear();
        //TODO clear daymodel list here?
        setCalendarGrid(rooms);

        int monthMaxDate = Model.getInstance().getMonthMaxDate();
        int dateOffset = Model.getInstance().getDateOffset();
        if(rooms == Rooms.ALL_ROOMS){
            Model.getInstance().setAvailableRoomsPerDayList(sqliteModel.getAvailableRoomsPerDayList());

            for(int i = 0; i < monthMaxDate; i++){
                Text temp = dayModelList.get(i + dateOffset).getRoomsText();
                StringBuilder desc = new StringBuilder();
                Set<String> set = Model.getInstance().getAvailableRoomsPerDayList().get(i);
                for(String string : set){
                    desc.append(Rooms.abbvToDisplay(string)).append("\n");
                }

                temp.setText(desc.toString());
                temp.setTextAlignment(TextAlignment.CENTER);
            }
        }
        else{
            List<String> slotsList = sqliteModel.getMonthSlots(rooms);

            for(int i = 0; i < monthMaxDate; i++){
                Text temp = dayModelList.get(i + dateOffset).getRoomsText();
                temp.setText(slotsList.get(i));
            }
        }
    }

    public void setClickable(){
        for(int i = 0; i < Model.getInstance().getMonthMaxDate(); i++){
            StackPane temp = dayModelList.get(i + Model.getInstance().getDateOffset()).getStackPane();
            temp.setOnMouseClicked(event -> {
                Text temp2 = (Text)(temp.getChildren().get(2));
                System.out.println(temp2.getText());
                temp.setStyle("-fx-background-color: red;");
            });
        }
    }
    public void colorize(Set<String> roomsCheckBoxes){
//        System.out.println(Model.getInstance().getAvailableRoomsPerDayList());

        if(roomsCheckBoxes.isEmpty()){
            for(int i = 0; i < Model.getInstance().getMonthMaxDate(); i++){
                StackPane temp = dayModelList.get(i + Model.getInstance().getDateOffset()).getStackPane();
                temp.setStyle("-fx-background-color: transparent;");
            }
        }
        else{
            for(int i = 0; i < Model.getInstance().getMonthMaxDate(); i++){
                StackPane temp = dayModelList.get(i + Model.getInstance().getDateOffset()).getStackPane();
                boolean available = true;
                for (String room : roomsCheckBoxes) {
                    if(!Model.getInstance().getAvailableRoomsPerDayList().get(i).contains(room)){
                        available = false;
                        break;
                    }
                }
                if(available){
                    temp.setStyle("-fx-background-color: green;");
                }
                else{
                    temp.setStyle("-fx-background-color: red;");
                }
            }
        }
    }
    public void highlight(){
        for(int i = 0; i < 42; i++){
            StackPane temp = dayModelList.get(i).getStackPane();
            if(Model.getInstance().getSelected().contains(i - Model.getInstance().getDateOffset() + 1)){
                temp.setBorder(normalBorder);
            }
            else{
                temp.setBorder(borderUnselected);
            }
        }
    }
    public void clear(){
        for(int i = 0; i < 42; i++){
            StackPane temp = dayModelList.get(i).getStackPane();
            temp.setBorder(normalBorder);
            temp.setStyle("-fx-background-color: white;");
        }
    }
    private void setCalendarGrid(Rooms rooms){
        yearText.setText(String.valueOf(Model.getInstance().getYear()));
        monthText.setText(String.valueOf(Model.getInstance().getMonth()));
        roomText.setText(rooms.getDisplayName());

        double calendarWidth = flowPane.getPrefWidth();
        double calendarHeight = flowPane.getPrefHeight();
        double strokeWidth = 5;
        double spacingH = flowPane.getHgap();
        double spacingV = flowPane.getVgap();

        double boxWidth = (calendarWidth/7) - strokeWidth - 10 - spacingH;
        double boxHeight = (calendarHeight/6) - strokeWidth - 10 - spacingV;

        dayModelList.clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                DayModel dayModel = new DayModel(i, j);

                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.TRANSPARENT);
//                rectangle.setStrokeWidth(strokeWidth);
                rectangle.setWidth(boxWidth);
                rectangle.setHeight(boxHeight);
                stackPane.getChildren().add(rectangle);

                if(dayModel.isWithinMonth()){
                    Text dateText = new Text(String.valueOf(dayModel.getGridDate()));
                    dateText.setTranslateY(-(boxHeight / 2) * 0.75);
                    dateText.setTranslateX((boxWidth / 2) * 0.75);
                    dayModel.setDayText(dateText);

                    stackPane.getChildren().add(dateText);
                    Text totalText = new Text("");
                    totalText.setTranslateY(boxHeight * 0.1);
                    stackPane.getChildren().add(totalText);
                    dayModel.setRoomsText(totalText);
                }
                stackPane.setBorder(normalBorder);
                flowPane.getChildren().add(stackPane);
                dayModel.setStackPane(stackPane);
                dayModelList.add(dayModel);
            }
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void flowPaneSmall(){
        flowPane.setPrefHeight(330);
    }
    public void setSceneEdit(RecordModel recordModel){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Edit.fxml"));
            Parent root = loader.load();

            EditController editController = loader.getController();
            editController.setValues(recordModel);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertListRows(){
        System.out.println("INSERTING TABLE");
        listTableChildren.removeIf(node -> listTableChildren.indexOf(node) > 8);
        List<RecordModel> list = Model.getInstance().getListRecordModels();
//        System.out.println(list);
        int startIndex = Model.getInstance().getStartIndex();
        int endIndex = Model.getInstance().getInitEndIndex();

        System.out.println(startIndex);
        System.out.println(endIndex);

        for(int i = startIndex; i < endIndex; i++){
            for(int j = 0; j < 14; j++){
                List<String> recordList = list.get(i).getList();
                Label label = new Label();
                label.setAlignment(Pos.CENTER);
                label.setText(recordList.get(j));
                label.setTextAlignment(TextAlignment.CENTER);


                GridPane.setRowIndex(label, i - startIndex);
                GridPane.setColumnIndex(label, j);
                listTable.getChildren().add(label);
                if (j == 1) {
                    label.setMaxWidth(65.1796875);
                    label.setWrapText(true);
                } else if (j == 12) {
                    label.setMaxWidth(49.669921875);
                    label.setWrapText(true);
                } else if (j == 7) {
                    VBox vbox = new VBox();
                    vbox.setAlignment(Pos.CENTER);

                    Line line = new Line();
                    line.setStartX(47.82231140136719);
                    line.setEndX(100.61520385742188);

                    Label temp = new Label();
                    temp.setAlignment(Pos.CENTER);
                    temp.setText(recordList.get(14));
                    temp.setTextAlignment(TextAlignment.CENTER);

                    vbox.getChildren().addAll(label, line, temp);
                    GridPane.setRowIndex(vbox, i - startIndex);
                    GridPane.setColumnIndex(vbox, j);
                    listTable.getChildren().add(vbox);
                }
//                else if(j == 8){
//                    VBox vbox = new VBox();
//                    vbox.setAlignment(Pos.CENTER);
//
//                    Label temp = new Label();
//                    temp.setAlignment(Pos.CENTER);
//                    temp.setText(recordList.get(14));
//                    temp.setTextAlignment(TextAlignment.CENTER);
//
//                    vbox.getChildren().addAll(label, temp);
//                    GridPane.setRowIndex(vbox, i);
//                    GridPane.setColumnIndex(vbox, j);
//                    gridPane.getChildren().add(vbox);
//                }
            }


            Button editButton = new Button("Edit");
            Button deleteButton = new Button("X");

            RecordModel recordModel = list.get(i);
            editButton.setOnAction(actionEvent -> {
                setSceneEdit(recordModel);
            });

            deleteButton.setOnAction(actionEvent -> {
                if(showConfirmPopup("Are you sure you want to delete this row?")){
                    if(sqliteModel.deleteEntry(recordModel.getIdInt())){
                        listTableChildren.removeIf(node -> listTableChildren.indexOf(node) > 8);
                        setSceneTable();
                    }
                }
            });


            GridPane.setColumnIndex(editButton, 14);
            GridPane.setColumnIndex(deleteButton, 15);

            GridPane.setRowIndex(editButton, i - startIndex);
            GridPane.setRowIndex(deleteButton, i - startIndex);

            listTable.getChildren().addAll(editButton, deleteButton);
        }
    }
    public boolean showDialog(Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Alert!");
        alert.setContentText("This is an alert");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isEmpty()){
            return false;
        } else if(result.get() == ButtonType.OK){
            return true;
        } else if(result.get() == ButtonType.CANCEL){
            return false;
        }
        return false;
    }
    public void showSuccessPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public boolean showConfirmPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isEmpty()){
            return false;
        } else return result.get() == ButtonType.OK;
    }

    public List<DayModel> getDayModelList() {
        return dayModelList;
    }

    public void setListTableChildren(List<Node> listTableChildren) {
        this.listTableChildren = listTableChildren;
    }

    public void setListTable(GridPane listTable) {
        this.listTable = listTable;
        setListTableChildren(listTable.getChildren());
    }

    public void generateReportPDF(){
        try {
            String path = "Report.pdf";
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.LETTER.rotate());




            Document document = new Document(pdfDocument);
            float margin = 20;
            document.setMargins(margin, margin, margin, margin);
//            document.setTopMargin(20);
//            document.setBottomMargin(20);

            int currentPage = 1;
            int rowCount = 15;
            float fontSize = 9.4f;

            float[] headerWidth = new float[5];
            Table table = new Table(headerWidth);
            table.addCell(new com.itextpdf.layout.element.Cell().add(
                    Model.getInstance().getTableStartDate() + " - " + Model.getInstance().getTableEndDate()).
                    setBold().setFontSize(fontSize));

            StringBuilder rooms;

            if(Model.getInstance().getTableRooms().isEmpty()){
                rooms = new StringBuilder("All rooms");
            }
            else{
                rooms = new StringBuilder("Rooms : ");
                for (String string : Model.getInstance().getTableRooms()){
                    rooms.append(string).append(" ");
                }
            }
            table.addCell(rooms.toString()).setBold().setFontSize(fontSize);
            table.addCell("Total Bookings: " + Model.getInstance().getRecordCount()).setBold().setFontSize(fontSize);
            table.addCell("Page " + currentPage + "/" + String.valueOf((int)Math.ceil(((double) 33 / rowCount)))).setBold().setFontSize(fontSize);
            table.addCell("Total Payment Received: " + Model.getInstance().getTotalPayment() + " (" + Model.getInstance().getTotalUnpaid() + " UNPAID)").setBold().setFontSize(fontSize);

//            Table nestedTable = new Table(new float[]{col1/2, col1/2});
//            nestedTable.addCell(new com.itextpdf.layout.element.Cell().add("Total Reservations").setBorder(com.itextpdf.layout.border.Border.NO_BORDER).setBold());
//            nestedTable.addCell(new com.itextpdf.layout.element.Cell().add("0").setBorder(com.itextpdf.layout.border.Border.NO_BORDER));
//            nestedTable.addCell(new com.itextpdf.layout.element.Cell().add("Total Guests").setBorder(com.itextpdf.layout.border.Border.NO_BORDER).setBold());
//            nestedTable.addCell(new com.itextpdf.layout.element.Cell().add("0").setBorder(com.itextpdf.layout.border.Border.NO_BORDER));
//            nestedTable.addCell(new com.itextpdf.layout.element.Cell().add("Total Revenue").setBorder(com.itextpdf.layout.border.Border.NO_BORDER).setBold());
//            nestedTable.addCell(new com.itextpdf.layout.element.Cell().add("0 PHP").setBorder(com.itextpdf.layout.border.Border.NO_BORDER));

//            table.addCell(nestedTable);

            document.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));

//            document.add(new Paragraph("").setBold().setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
            document.add(new Paragraph(""));
            float[] columnWidthsInPoints = {
                    45.8f, //id
                    90.4f, //date
                    250.2f, //name
                    40.2f, //pax
                    46.7f, //vehicle
                    40.9f, //pets
                    47.7f, //videoke
                    55.0f, //payment
                    55.8f, //balance
                    44.4f, //status
                    100.5f, //checkin
                    100.5f, //chekcout
                    64.6f, //room
                    135.6f //user
            };
            Table body = new Table(columnWidthsInPoints);
            String[] columnHeaders = {
                    "ID", "Date", "Name", "No. of Heads", "Vehicle", "Pets", "Videoke",
                    "Payment", "Balance", "Status", "Check-In", "Check-Out", "Room", "User"
            };

            for (String columnHeader : columnHeaders) {
                body.addCell(new com.itextpdf.layout.element.Cell().add(columnHeader)
                        .setBold()
                        .setFontSize(fontSize)
                        .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE));
            }


            List<RecordModel> list = Model.getInstance().getListRecordModels();
            for(int i = 0; i < 15; i++){
                for(int j = 0; j < 14; j++){

                    List<String> recordList = list.get(i).getList();
                    if (j == 1) {
                        //date inserted
//                        body.addCell(new com.itextpdf.layout.element.Cell().add(recordList.get(j)));
                    } else if (j == 7) {
                        //partial payment
                        //line
                        //full payment
//                        temp.setText(recordList.get(14));
                    } else if (j == 12) {
                        //rooms
                    }
                    else{
//                        body.addCell(new com.itextpdf.layout.element.Cell().add(recordList.get(j)));
                    }
                    body.addCell(new com.itextpdf.layout.element.Cell().add(recordList.get(j))
                            .setFontSize(fontSize)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE));
                }
            }

            document.add(body);
            document.close();

            try {
                Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + "D:\\work\\Java\\Projects\\ResortApp\\Report.pdf");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}