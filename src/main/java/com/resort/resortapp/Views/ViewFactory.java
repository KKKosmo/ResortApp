package com.resort.resortapp.Views;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.resort.resortapp.Controllers.EditController;
import com.resort.resortapp.Controllers.TableController;
import com.resort.resortapp.Models.*;
import com.resort.resortapp.Rooms;
import com.itextpdf.layout.element.Cell;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
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
    List<DayModel> onScreenCalendarDayModels = new ArrayList<>();
    List<LocalDate> onScreenLocalDates = new ArrayList<>();
    List<Node> listTableChildren;

    Scene table;
    GridPane listTable;
    List<CheckBox> roomCheckBoxes = new ArrayList<>();

    boolean editing = false;
    int editId = -1;
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

        Model.getInstance().getViewFactory().notEditing();
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
            Model.getInstance().getViewFactory().notEditing();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setSceneEditHistory(){
        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/EditHistory.fxml"));
//            Parent root = loader.load();
//
//            EditHistoryController editHistoryController = loader.getController();
//            editHistoryController.setList(sqliteModel.getEditHistory());
//            stage.setScene(new Scene(root));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/EditHistory.fxml"));
            Model.getInstance().getViewFactory().notEditing();
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
            Model.getInstance().getViewFactory().notEditing();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }
    public void setSceneMainMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/MainMenu.fxml"));
            Model.getInstance().getViewFactory().notEditing();
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
        setCalendarGrid();

        int dateOffset = Model.getInstance().getDateOffset();

        int startDate = 0;

        LocalDate tempDate;



        if(Model.getInstance().getEdgeLeftDate() == null)
            tempDate = Model.getInstance().getCalendarLeftDate();
        else
            tempDate = Model.getInstance().getEdgeLeftDate();


        if(tempDate.isBefore(Model.getInstance().getDateFocus())){
            while (tempDate.getMonth() != Model.getInstance().getDateFocus().getMonth()){
                startDate += tempDate.lengthOfMonth();
                tempDate = tempDate.plusMonths(1);
            }
        }
        else{
            tempDate = Model.getInstance().getDateFocus();
            while (tempDate.getMonth() != Model.getInstance().getDateFocus().getMonth()){
                startDate += tempDate.lengthOfMonth();
                tempDate = tempDate.plusMonths(1);
            }
        }





//        System.out.println("========tempdate = " + tempDate);
//        System.out.println("========datefocus  = " + Model.getInstance().getDateFocus());
//        System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");

        if(editing && editId != -1){
//            System.out.println("EDITING--------------------");
            Model.getInstance().setAvailableRoomsPerDayWithinTheMonthsList(sqliteModel.getAvailableRoomsPerDayList(editId));
        }
        else{
//            System.out.println("NOT EDITING---------------------");
            Model.getInstance().setAvailableRoomsPerDayWithinTheMonthsList(sqliteModel.getAvailableRoomsPerDayList());
        }

        int monthMaxDate = Model.getInstance().getCalendarRightDate().getDayOfMonth();

//        System.out.println("===========STARTDATE = " + startDate);
//        System.out.println("==============MONTHMAXDATE = " + monthMaxDate);

        if(rooms == Rooms.ALL_ROOMS){

//            System.out.println("STARTDATE = " + startDate);
//            System.out.println("monthmaxdate = " + monthMaxDate);
            for(int i = 0; i < monthMaxDate; i++){
                if(onScreenCalendarDayModels.get(i + dateOffset).getRoomsText() == null){
                    System.out.println("NULL HERE" + (i + dateOffset));
                }
                Text temp = onScreenCalendarDayModels.get(i + dateOffset).getRoomsText();
                StringBuilder desc = new StringBuilder();

                Set<String> set = Model.getInstance().getAvailableRoomsPerDayWithinTheMonthsList().get(startDate + i);
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
                Text temp = onScreenCalendarDayModels.get(i + dateOffset).getRoomsText();
                temp.setText(slotsList.get(i));
            }
        }
    }
    public void colorize(){
//        System.out.println("IN COLORIZE");
//        System.out.println("CALENDAR DAY MODELS = " + onScreenCalendarDayModels.size());
//        System.out.println("whole list = " + Model.getInstance().getAvailableRoomsPerDayWithinTheMonthsList().size());
        Set<String> roomsCheckBoxes = Rooms.manageCheckboxesSetAbbreviatedName(roomCheckBoxes);
        if(roomsCheckBoxes.isEmpty()){
            for(int i = 0; i < Model.getInstance().getMonthMaxDate(); i++){
                StackPane temp = onScreenCalendarDayModels.get(i + Model.getInstance().getDateOffset()).getStackPane();
                temp.setStyle("-fx-background-color: transparent;");
            }
        }
        else{
            int positionInList = 0;

            if(Model.getInstance().getEdgeLeftDate() != null){
                LocalDate temp2 = Model.getInstance().getEdgeLeftDate();
                while (Model.getInstance().getCalendarLeftDate().isAfter(temp2)){
                    positionInList += temp2.lengthOfMonth();
                    temp2 = temp2.plusMonths(1);
                }
            }

            for(int i = 0; i < Model.getInstance().getMonthMaxDate(); i++){
                StackPane temp = onScreenCalendarDayModels.get(i + Model.getInstance().getDateOffset()).getStackPane();
                boolean available = true;
                for (String room : roomsCheckBoxes) {
                    if(!Model.getInstance().getAvailableRoomsPerDayWithinTheMonthsList().get(i + positionInList).contains(room)){
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
//        System.out.println("HIGHLIGHTING");
        Set<Integer> indexes = new HashSet<>();

        for(LocalDate localDate : Model.getInstance().getSelectedLocalDates()){
//            System.out.println(localDate);
            if(Model.getInstance().getViewFactory().onScreenLocalDates.contains(localDate)){
                int index = Model.getInstance().getViewFactory().onScreenLocalDates.indexOf(localDate);
                indexes.add(index + Model.getInstance().getDateOffset());
            }
        }

        for(int i = 0; i < 42; i++){
            StackPane temp = onScreenCalendarDayModels.get(i).getStackPane();

            if(indexes.contains(i)){
                temp.setBorder(normalBorder);
            }
            else{
                temp.setBorder(borderUnselected);
            }
        }
    }
    public void clear(){
        for(int i = 0; i < 42; i++){
            StackPane temp = onScreenCalendarDayModels.get(i).getStackPane();
            temp.setBorder(normalBorder);
            temp.setStyle("-fx-background-color: white;");
        }
    }
    public void setCalendarGrid(){
        yearText.setText(String.valueOf(Model.getInstance().getYear()));
        monthText.setText(String.valueOf(Model.getInstance().getMonth()));

        double calendarWidth = flowPane.getPrefWidth();
        double calendarHeight = flowPane.getPrefHeight();
        double strokeWidth = 5;
        double spacingH = flowPane.getHgap();
        double spacingV = flowPane.getVgap();

        double boxWidth = (calendarWidth/7) - strokeWidth - 10 - spacingH;
        double boxHeight = (calendarHeight/6) - strokeWidth - 10 - spacingV;

        onScreenLocalDates.clear();
        onScreenCalendarDayModels.clear();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                DayModel dayModel = new DayModel(i, j);

                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.TRANSPARENT);
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


//                    int offset = Model.getInstance().getDateOffset();
                    LocalDate dateFocus = Model.getInstance().getDateFocus();
                    int gridDate = dayModel.getGridDate();

//                    int maxDayOfMonth = dateFocus.lengthOfMonth();
//                    int adjustedDay = Math.min(maxDayOfMonth, gridDate + offset);

                    LocalDate newDate = dateFocus.withDayOfMonth(gridDate);
                    onScreenLocalDates.add(newDate);
//                    System.out.println("ONSCREEN LOCAL DATE " + newDate);
                }
//                else {
//                    stackPane.setBorder(borderUnselected);
//                }
                stackPane.setBorder(borderUnselected);
                flowPane.getChildren().add(stackPane);
                dayModel.setStackPane(stackPane);
                onScreenCalendarDayModels.add(dayModel);

//                onScreenLocalDates.add(Model.getInstance().getDateFocus().withDayOfMonth(dayModel.getGridDate() + Model.getInstance().getDateOffset()));
            }
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setSceneEdit(RecordModel recordModel){
        try {
            Model.getInstance().getViewFactory().editing(Integer.parseInt(recordModel.getId()));
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
//        System.out.println("INSERTING TABLE");
        listTableChildren.removeIf(node -> listTableChildren.indexOf(node) > 8);
        List<RecordModel> list = Model.getInstance().getListRecordModels();
//        System.out.println(list);
        int startIndex = Model.getInstance().getStartIndex();
        int endIndex = Model.getInstance().getInitEndIndex();

//        System.out.println(startIndex);
//        System.out.println(endIndex);

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
                    if(sqliteModel.deleteEntry(recordModel)){
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
    public List<DayModel> getOnScreenCalendarDayModels() {
        return onScreenCalendarDayModels;
    }
    public void setListTableChildren(List<Node> listTableChildren) {
        this.listTableChildren = listTableChildren;
    }
    public void setListTable(GridPane listTable) {
        this.listTable = listTable;
        setListTableChildren(listTable.getChildren());
    }

    public void setRoomCheckBoxes(List<CheckBox> roomCheckBoxes) {
        this.roomCheckBoxes = roomCheckBoxes;
    }

    float fontSize = 9.4f;
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

            int totalPages = Model.getInstance().getMaxPage();

            for (int currentPage = 1; currentPage <= totalPages; currentPage++){

                float[] headerWidth = new float[5];
                Table table = new Table(headerWidth);
                table.addCell(new Cell().add(
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
                table.addCell("Page " + currentPage + "/" + totalPages).setBold().setFontSize(fontSize);
                table.addCell("Total Payment Received: " + Model.getInstance().getTotalPayment() + " (" + Model.getInstance().getTotalUnpaid() + " UNPAID)").setBold().setFontSize(fontSize);


                document.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));

//            document.add(new Paragraph("").setBold().setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                document.add(new Paragraph(""));
                float[] columnWidthsInPoints = {
                        45.8f, //id
                        90.4f, //date
                        215.2f, //name
                        35.2f, //pax
                        41.7f, //vehicle
                        30.9f, //pets
                        42.7f, //videoke
                        125.0f, //payment
                        55.8f, //balance
                        44.4f, //status
                        100.5f, //checkin
                        100.5f, //chekcout
                        64.6f, //room
                        125.6f //user
                };
                Table body = new Table(columnWidthsInPoints);

                body.addCell(createCellBold("ID"));
                body.addCell(createCellBold("Time Created"));
                body.addCell(createCellBold("Name"));
                body.addCell(createCellBold("No. of Heads"));
                body.addCell(createCellBold("Vehicle"));
                body.addCell(createCellBold("Pets"));
                body.addCell(createCellBold("Videoke"));



                Table headerPayment = new Table(new float[1]).setMargin(0f).setPadding(0f);

                headerPayment.addCell(createCellBold("Partial Payment").setMargin(0f).setPadding(0f)
                        .setBorderBottom(new SolidBorder(com.itextpdf.kernel.color.Color.BLACK, 1f))
                        .setBorderLeft(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                        .setBorderRight(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                        .setBorderTop(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f)));
                headerPayment.addCell(createCellBold("Full Payment").setMargin(0f).setPadding(0f)
                        .setBorderBottom(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                        .setBorderLeft(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                        .setBorderRight(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                        .setBorderTop(new SolidBorder(com.itextpdf.kernel.color.Color.BLACK, 1f)));

                body.addCell(headerPayment);



                body.addCell(createCellBold("Balance"));
                body.addCell(createCellBold("Status"));
                body.addCell(createCellBold("Check-In"));
                body.addCell(createCellBold("Check-Out"));
                body.addCell(createCellBold("Room"));
                body.addCell(createCellBold("User"));


                List<RecordModel> list = Model.getInstance().getListRecordModels();

                System.out.println(currentPage);

                int startIndex = Model.getInstance().getTableRowCount() * (currentPage - 1);
                int end = Math.min(startIndex + Model.getInstance().getTableRowCount(), Model.getInstance().getListRecordModels().size());
                System.out.println(Model.getInstance().getTableRowCount());
                System.out.println(startIndex + " - " + end);
//                int end = Math.min(15 * currentPage, Model.getInstance().getTableRowCount());
                for(int i = startIndex; i < end; i++){
                    for(int j = 0; j < 14; j++){

                        List<String> recordList = list.get(i).getList();
                        if (j == 7) {
                            //partial payment
                            //line
                            //full payment
//                        temp.setText(recordList.get(14));
//                        body.addCell(createCell(recordList.get(j)));


                            Table rowPayment = new Table(new float[1]).setMargin(0f).setPadding(0f);

                            rowPayment.addCell(createCell(recordList.get(j)).setMargin(0f).setPadding(0f)
                                    .setBorderBottom(new SolidBorder(com.itextpdf.kernel.color.Color.BLACK, 1f))
                                    .setBorderLeft(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                                    .setBorderRight(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                                    .setBorderTop(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f)));
                            rowPayment.addCell(createCell(recordList.get(14)).setMargin(0f).setPadding(0f)
                                    .setBorderBottom(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                                    .setBorderLeft(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                                    .setBorderRight(new SolidBorder(com.itextpdf.kernel.color.Color.WHITE, 0f))
                                    .setBorderTop(new SolidBorder(com.itextpdf.kernel.color.Color.BLACK, 1f)));

                            body.addCell(rowPayment.setHorizontalAlignment(HorizontalAlignment.CENTER));


                        } else if (j == 2) {
                            body.addCell(createCell(recordList.get(j)));
                        }
                        else{
                            body.addCell(createCell(recordList.get(j)));
                        }
                    }
                }

                document.add(body);
            }
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
    public Cell createCell(String text) {
        return new Cell().add(text)
                .setFontSize(fontSize)
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }
    public Cell createCellBold(String text) {
        return new Cell().add(text)
                .setFontSize(fontSize)
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBold();
    }
    public void editing(int id){
        editId = id;
        editing = true;
    }
    public void notEditing(){
        editId = -1;
        editing = false;
    }
}