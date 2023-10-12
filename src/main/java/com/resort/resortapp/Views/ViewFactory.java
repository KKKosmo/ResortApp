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
import com.resort.resortapp.Controllers.ChangePwController;
import com.resort.resortapp.Controllers.EditController;
import com.resort.resortapp.Controllers.EscMenuController;
import com.resort.resortapp.Controllers.TableController;
import com.resort.resortapp.Models.DayModel;
import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.RecordModel;
import com.resort.resortapp.Models.sqliteModel;
import com.resort.resortapp.Rooms;
import com.itextpdf.layout.element.Cell;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class ViewFactory {
    private AnchorPane escMenu;
    private Text yearText;
    private Text monthText;
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
    Border borderUnselected = new Border(new BorderStroke(Paint.valueOf("6E6E6E"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(3)));
    Border normalBorder = new Border(new BorderStroke(Paint.valueOf("2E2E2E"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(5)));
    EscMenuController escMenuController;
    public void setCalendarVariables(FlowPane flowPane, Text yearText, Text monthText) {
        this.flowPane = flowPane;
        this.yearText = yearText;
        this.monthText = monthText;
    }
    public AnchorPane getEscMenu(Pane parentPane){
        if (escMenu == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/EscMenu.fxml"));
                escMenu = loader.load();
                escMenuController = loader.getController();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        parentPane.getChildren().add(escMenu);
        return escMenu;
    }
    public void setSceneTable(boolean def) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Table.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            Model.getInstance().printLog(e);
        }
//        if(table == null){
//            try {
//            } catch (Exception e) {
//                Model.getInstance().printLog(e);
//            }
//        }

        table = new Scene(root);
        TableController tableController = loader.getController();
        tableController.myInit();
        tableController.setEscMenu();
        if(def)
            tableController.clear();

        Tooltip tooltip = new Tooltip("IF NOT PAID, THEN THE PARTIAL PAYMENT INSTEAD OF THE FULL PAYMENT GETS ADDED TO THE TOTAL PAYMENT");
        tooltip.setShowDelay(Duration.millis(0));
        tooltip.setStyle("-fx-background-color: #E2E2E2; -fx-text-fill: #000000; -fx-font-size: 12;");
        Tooltip.install(tableController.totalPayment_hBox, tooltip);

        notEditing();
        stage.setScene(table);




//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Table.fxml"));
//                stage.setScene(new Scene(loader.load()));
//            } catch (Exception e) {
//                Model.getInstance().printLog(e);
//            }


    }
    public void setSceneLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
            notEditing();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            Model.getInstance().printLog(e);
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
            notEditing();
            stage.setScene(new Scene(loader.load()));

        } catch (Exception e) {
            Model.getInstance().printLog(e);
        }
    }
    public void setSceneCreate(){
//        if (create == null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Create.fxml"));
//                create = new Scene(loader.load());
            notEditing();
            stage.setScene(new Scene(loader.load()));
        } catch (Exception e) {
            Model.getInstance().printLog(e);
        }
//        }
    }
    public void insertCalendar(Pane pane){
        try {
            AnchorPane childPane = FXMLLoader.load(getClass().getResource("/Fxml/Calendar.fxml"));
            pane.getChildren().add(childPane);
        } catch (IOException e) {
            Model.getInstance().printLog(new RuntimeException(e));
            throw new RuntimeException(e);
        }
    }
    public void fillFlowPaneMonths(){
        if(editing && editId != -1){
//            System.out.println("EDITING--------------------");
            Model.getInstance().setAvailablesForVisual(sqliteModel.getAvailableRoomsPerDayList(editId));
        }
        else{
//            System.out.println("NOT EDITING---------------------");
            Model.getInstance().setAvailablesForVisual(sqliteModel.getAvailableRoomsPerDayList());
        }
        flowPane.getChildren().clear();
        setCalendarGrid();

        int dateOffset = Model.getInstance().getDateOffset();

        int monthMaxDate = Model.getInstance().getCalendarRightDate().getDayOfMonth();

        for(int i = 0; i < monthMaxDate; i++){
            Text temp = onScreenCalendarDayModels.get(i + dateOffset).getRoomsText();
            StringBuilder desc = new StringBuilder();
            List<String> set = Model.getInstance().getAvailableRoomsPerDayWithinTheMonthsList().get(i);
            for(String string : set){
                desc.append(Rooms.abbvToDisplay(string)).append("\n");
            }

            temp.setText(desc.toString());
            temp.setTextAlignment(TextAlignment.CENTER);
        }

    }
    public void colorize(){
        System.out.println("IN COLORIZE++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        System.out.println("CALENDAR DAY MODELS = " + onScreenCalendarDayModels.size());
//        System.out.println("whole list = " + Model.getInstance().getAvailableRoomsPerDayWithinTheMonthsList().size());
        Set<String> roomsCheckBoxes = Rooms.manageCheckboxesSetAbbreviatedName(roomCheckBoxes);
        int monthMaxDate = Model.getInstance().getMonthMaxDate();
        int dateOffset = Model.getInstance().getDateOffset();
        if(roomsCheckBoxes.isEmpty()){
            for(int i = 0; i < monthMaxDate; i++){
                StackPane temp = onScreenCalendarDayModels.get(i + dateOffset).getStackPane();
                temp.setStyle("-fx-background-color: transparent;");
            }
        }
        else{

            for(int i = 0; i < monthMaxDate; i++){
                StackPane temp = onScreenCalendarDayModels.get(i + dateOffset).getStackPane();
                boolean available = true;
                for (String room : roomsCheckBoxes) {
                    if(!Model.getInstance().getAvailableRoomsPerDayWithinTheMonthsList().get(i).contains(room)){
                        available = false;
                        break;
                    }
                }
                if(available){
                    temp.setStyle("-fx-background-radius: 30; -fx-background-color: #73c06a;");
                }
                else{
                    temp.setStyle("-fx-background-radius: 30; -fx-background-color: #c06a6a;");
                }
            }
        }
    }
    public void highlight(){
//        System.out.println("HIGHLIGHTING");
        Set<Integer> indexes = new HashSet<>();


        for(LocalDate localDate : Model.getInstance().getSelectedLocalDates()){
//            System.out.println(localDate);
            if(onScreenLocalDates.contains(localDate)){
                int index = onScreenLocalDates.indexOf(localDate);
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
                    dateText.setTranslateY(-(boxHeight / 2) * 0.70);
                    dateText.setTranslateX((boxWidth / 2) * 0.70);
                    dayModel.setDayText(dateText);

                    stackPane.getChildren().add(dateText);
                    Text totalText = new Text("");
                    totalText.setTranslateY(boxHeight * 0.075);
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
            editing(Integer.parseInt(recordModel.getId()));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Edit.fxml"));
            Parent root = loader.load();

            EditController editController = loader.getController();
            editController.setValues(recordModel);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Model.getInstance().printLog(e);
        }
    }
    public void setSceneChangePw(String user){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ChangePw.fxml"));
            Parent root = loader.load();

            ChangePwController changePwController = loader.getController();
            changePwController.setValues(user);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            Model.getInstance().printLog(e);
        }
    }
    public void insertListRows(){
//        System.out.println("INSERTING TABLE");
        listTableChildren.removeIf(node -> listTableChildren.indexOf(node) > 14);

        List<RecordModel> list = Model.getInstance().getListRecordModels();
//        System.out.println(list);
        int startIndex = Model.getInstance().getStartIndex();
        int endIndex = Model.getInstance().getInitEndIndex();


        for(int i = startIndex; i < endIndex; i++){
            for(int j = 0; j < 14; j++){
                List<String> recordList = list.get(i).getList();
                Label label = new Label();
                label.setAlignment(Pos.CENTER);
                label.setText(recordList.get(j));
                label.setTextAlignment(TextAlignment.CENTER);
//                label.setWrapText(true);
//                label.setPrefWidth(280);

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


            RecordModel recordModel = list.get(i);

            Button editButton = new Button();
            Button deleteButton = new Button();

            FontAwesomeIconView editIcon = new FontAwesomeIconView();
            editIcon.setGlyphName("EDIT");
            FontAwesomeIconView deleteIcon = new FontAwesomeIconView();
            deleteIcon.setGlyphName("TIMES");
            editIcon.setGlyphSize(16);
            deleteIcon.setGlyphSize(16);

            editButton.setGraphic(editIcon);
            deleteButton.setGraphic(deleteIcon);

            editButton.setCursor(Cursor.HAND);
            deleteButton.setCursor(Cursor.HAND);

            rowButtonOnHoverEdit(editButton, i - startIndex);
            rowButtonOnHoverDelete(deleteButton, i - startIndex);

            editButton.getStyleClass().add("rowButton");
            deleteButton.getStyleClass().add("rowButton");

            editButton.setPrefHeight(32);
            editButton.setPrefWidth(32);
            deleteButton.setPrefHeight(32);
            deleteButton.setPrefWidth(32);

            if(recordModel.getUser().equals(Model.getInstance().getUser())){
                editIcon.setFill(Paint.valueOf("#2E2E2E"));
                deleteIcon.setFill(Paint.valueOf("#2E2E2E"));

                editButton.setOnAction(actionEvent -> setSceneEdit(recordModel));

                deleteButton.setOnAction(actionEvent -> {
                    if(showConfirmPopup("Are you sure you want to delete this booking? (ID = " + recordModel.getId() + ")")){
                        if(sqliteModel.deleteEntry(recordModel)){
                            setSceneTable(false);
                        }
                    }
                });


            }
            else{
                editIcon.setFill(Paint.valueOf("#adadad"));
                deleteIcon.setFill(Paint.valueOf("#adadad"));

                editButton.setOnAction(actionEvent -> showErrorPopup("You are not user "+recordModel.getUser()+", cannot modify this booking"));
                deleteButton.setOnAction(actionEvent -> showErrorPopup("You are not user "+recordModel.getUser()+", cannot modify this booking"));
            }
            GridPane.setColumnIndex(editButton, 14);
            GridPane.setColumnIndex(deleteButton, 15);

            GridPane.setRowIndex(editButton, i - startIndex);
            GridPane.setRowIndex(deleteButton, i - startIndex);

            listTable.getChildren().addAll(editButton, deleteButton);


        }
    }

    private void rowButtonOnHoverEdit(Button button, int index){
        button.setOnMouseEntered(mouseEvent -> listTableChildren.get(index).getStyleClass().add("edit"));
        button.setOnMouseExited(mouseEvent -> listTableChildren.get(index).getStyleClass().remove("edit"));
    }
    private void rowButtonOnHoverDelete(Button button, int index){
        button.setOnMouseEntered(mouseEvent -> listTableChildren.get(index).getStyleClass().add("delete"));
        button.setOnMouseExited(mouseEvent -> listTableChildren.get(index).getStyleClass().remove("delete"));
    }

    public void showSuccessPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/Styles/alert.css").toExternalForm());
        alert.showAndWait();
    }
    public void showErrorPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/Styles/alert.css").toExternalForm());
        alert.showAndWait();
    }
    public boolean showConfirmPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setContentText(message);

        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setStyle(
                "-fx-background-color: #FFFFFF; -fx-border-color: #2E2E2E; -fx-cursor: hand; -fx-border-radius: 30;"
        );


        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(
                "-fx-background-color: #e5e5e5; -fx-border-color: #474747; -fx-cursor: hand; -fx-border-radius: 30;"
        ));


        cancelButton.setOnMouseExited(e -> cancelButton.setStyle(
                "-fx-background-color: #FFFFFF; -fx-border-color: #2E2E2E; -fx-cursor: hand; -fx-border-radius: 30;"
        ));

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/Styles/alert.css").toExternalForm());
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isEmpty()){
            return false;
        } else return result.get() == ButtonType.OK;
    }

    public void showForgotPwPopup(String filename, String filePath) {
        VBox root = new VBox();

        Label label = new Label("A file (" + filename + ") has been exported as: " + filePath);
        label.getStyleClass().add("myHeader");
        label.setWrapText(true);
        label.setMaxWidth(520);
        root.getChildren().add(label);

        Hyperlink openExplorerLink = new Hyperlink("Click to open in explorer");
        openExplorerLink.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + filePath);
            } catch (Exception e) {
                Model.getInstance().printLog(e);
            }
        });
        openExplorerLink.getStyleClass().add("hyperlink");
        root.getChildren().add(openExplorerLink);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Retrieval Instructions");
        alert.setHeaderText(null);

        String contentText = "\nTo retrieve the password, please send the file (" + filename + ") via email (manilalouisangel2@gmail.com) to the developer and wait for further instructions.\n\n" +
                "You can also contact me for support at Facebook: Louis Manila\n\n";


        Text text = new Text(contentText);
        text.setWrappingWidth(504);
        text.getStyleClass().add("myContent");
        root.getChildren().add(text);


        Button copyEmailButton = new Button("Copy Email");
        HBox emailBox = new HBox(copyEmailButton);
        root.getChildren().add(emailBox);

        Label copySuccessLabel = new Label("Email copied!");
        copySuccessLabel.setStyle("-fx-text-fill: #2E2E2E;");
        copySuccessLabel.setVisible(false);
        root.getChildren().add(copySuccessLabel);

        copyEmailButton.setOnAction(event -> {
            ClipboardContent content = new ClipboardContent();
            content.putString("manilalouisangel2@gmail.com");

            Clipboard clipboard = Clipboard.getSystemClipboard();
            clipboard.setContent(content);

            copySuccessLabel.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
            visiblePause.setOnFinished(e -> copySuccessLabel.setVisible(false));
            visiblePause.play();
        });

        copyEmailButton.setStyle(
                "-fx-background-insets: 2, 0, 2; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-width: 2px; -fx-border-color: #2E2E2E; -fx-background-color: #FFFFFF; -fx-cursor: hand;"
        );

        copyEmailButton.setOnMouseEntered(e -> copyEmailButton.setStyle(
                "-fx-background-insets: 2, 0, 2; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-width: 2px; -fx-border-color: #474747; -fx-background-color: #e5e5e5; -fx-cursor: hand;"
        ));

        copyEmailButton.setOnMouseExited(e -> copyEmailButton.setStyle(
                "-fx-background-insets: 2, 0, 2; -fx-border-radius: 30; -fx-background-radius: 30; -fx-border-width: 2px; -fx-border-color: #2E2E2E; -fx-background-color: #FFFFFF; -fx-cursor: hand;"
        ));


        alert.getDialogPane().setContent(root);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/Styles/alert.css").toExternalForm());
        alert.showAndWait();
    }

    public void showReportLocation(String filename, String filePath){
        VBox root = new VBox();

        Label label = new Label("A report summary PDF (" + filename + ") has been exported as: " + filePath);
        label.getStyleClass().add("myHeader");
        label.setWrapText(true);
        label.setMaxWidth(560);
        root.getChildren().add(label);

        Hyperlink openExplorerLink = new Hyperlink("Click to open in explorer");
        openExplorerLink.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + filePath);
            } catch (Exception e) {
                Model.getInstance().printLog(e);
            }
        });
        openExplorerLink.getStyleClass().add("hyperlink");
        root.getChildren().add(openExplorerLink);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report PDF exported notice");
        alert.setHeaderText(null);

        alert.getDialogPane().setContent(root);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/Styles/alert.css").toExternalForm());
        alert.showAndWait();
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
        int totalPages = Model.getInstance().getMaxPage();

        if(totalPages == 0){
            showErrorPopup("Cannot export a report with no content.");
        }
        else{
            try {
                int reportID = sqliteModel.getReportID();

                File folder = new File("Reports");
                if (!folder.exists()) {
                    folder.mkdirs();
                }


                String path = "Reports/Report"+reportID+".pdf";
                PdfWriter pdfWriter = new PdfWriter(path);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                pdfDocument.setDefaultPageSize(PageSize.LETTER.rotate());

                Document document = new Document(pdfDocument);
                float margin = 20;
                document.setMargins(margin, margin, margin, margin);
//            document.setTopMargin(20);
//            document.setBottomMargin(20);


                for (int currentPage = 1; currentPage <= totalPages; currentPage++){

                    float[] headerWidth = new float[6];
                    Table table = new Table(headerWidth);




                    if (Model.getInstance().getTableYearMonth() != null) {
//                        table.addCell(new Cell().add("Time Range: " + Model.getInstance().getTableYearMonth().format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.US))).setBold().setFontSize(fontSize));
                        table.addCell(createCellBold("Time Range: " + Model.getInstance().getTableYearMonth().format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.US))));
                    }
                    else if(Model.getInstance().checkTableEdges()){
//                        table.addCell(new Cell().add("Time Range: " + Model.getInstance().getTableStartDate() + " - " + Model.getInstance().getTableEndDate()).setBold().setFontSize(fontSize));
                        table.addCell(createCellBold("Time Range: " + Model.getInstance().getTableStartDate() + " - " + Model.getInstance().getTableEndDate()));
                    }
                    else{
//                        table.addCell(new Cell().add("Time Range: ALL TIME").setBold().setFontSize(fontSize));
                        table.addCell(createCellBold("Time Range: ALL TIME"));
                    }

                    StringBuilder rooms;

                    Set<String> tableRooms = Model.getInstance().getTableRooms();

                    if(tableRooms.isEmpty()){
                        rooms = new StringBuilder("All rooms");
                    }
                    else{
                        if(tableRooms.size()>1){
                            rooms = new StringBuilder("Rooms: ");
                        }else{
                            rooms = new StringBuilder("Room: ");
                        }


                        for (String string : tableRooms){
                            rooms.append(string).append(", ");
                        }
                        rooms.delete(rooms.length() - 2, rooms.length());
                    }
//                    table.addCell(rooms.toString()).setBold().setFontSize(fontSize);
                    table.addCell(createCellBold(rooms.toString()));

//                    table.addCell("Sorted by: " + Model.getInstance().getOrderCategory() + " " + (Model.getInstance().isASC() ? "ASC" : "DESC")).setBold().setFontSize(fontSize);
                    table.addCell(createCellBold("Sorted by: " + Model.getInstance().getOrderCategory() + " " + (Model.getInstance().isASC() ? "ASC" : "DESC")));
//                    table.addCell("Page " + currentPage + "/" + totalPages).setBold().setFontSize(fontSize);
                    table.addCell(createCellBold("Page " + currentPage + "/" + totalPages));
//                    table.addCell("Total Bookings: " + Model.getInstance().getRecordCount()).setBold().setFontSize(fontSize);
                    table.addCell(createCellBold("Total Bookings: " + Model.getInstance().getRecordCount()));
//                    table.addCell("Total Payment Received: " + Model.getInstance().getTotalPayment() + " (" + Model.getInstance().getTotalUnpaid() + " UNPAID)").setBold().setFontSize(fontSize);
                    table.addCell(createCellBold("Total Payment Received: " + Model.getInstance().getTotalPayment() + " (" + Model.getInstance().getTotalUnpaid() + " UNPAID)"));

                    document.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));

//            document.add(new Paragraph("").setBold().setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph(""));
                    float[] columnWidthsInPoints = {
                            45.8f, //id
                            90.4f, //date
                            208.2f, //name
                            35.2f, //pax
                            41.7f, //vehicle
                            30.9f, //pets
                            42.7f, //videoke
                            132.0f, //payment
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

                    int tableRowCount = Model.getInstance().getTableRowCount();

                    int startIndex = tableRowCount * (currentPage - 1);
                    int end = Math.min(startIndex + tableRowCount, list.size());
//                    System.out.println(tableRowCount);
//                    System.out.println(startIndex + " - " + end);
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


                            }
                            else if (j == 2) {
                                Cell cell = new Cell()
                                        .setWidth(208.2f)
                                        .setFontSize(fontSize)
                                        .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
                                        .setVerticalAlignment(VerticalAlignment.MIDDLE);

                                String text = recordList.get(j);
                                Paragraph paragraph = new Paragraph();

                                if (text.length() > 20) {
                                    paragraph.setFontSize(8f); // Set the font size to 8 if there are more than 20 characters
                                } else {
                                    paragraph.setFontSize(fontSize);
                                }

                                for (char letter : text.toCharArray()) {
                                    String letterText = String.valueOf(letter);
                                    paragraph.add(letterText);
                                }

                                cell.add(paragraph);
                                body.addCell(cell);
                            }
                            else{
                                body.addCell(createCell(recordList.get(j)));
                            }
                        }
                    }

                    document.add(body);
                }
                document.close();

                sqliteModel.incrementReportID();

                File file = new File(path);

                showReportLocation(file.getName(), file.getAbsolutePath());

//            try {
//                Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + "D:\\work\\Java\\Projects\\ResortApp\\Report.pdf");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            } catch (FileNotFoundException e) {
                Model.getInstance().printLog(new RuntimeException(e));
                throw new RuntimeException(e);
            }
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

//        Text textElement = new Text(text)
//                .setFontSize(fontSize)
//                .setTextAlignment(TextAlignment.CENTER)
//                .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                .setBold()

        // Create a Cell and add the Text element to it
//        Cell cell = new Cell().add(textElement);
//
//        return cell;

    }
    public void editing(int id){
        editId = id;
        editing = true;
    }
    public void notEditing(){
        editId = -1;
        editing = false;
    }

    public EscMenuController getEscMenuController() {
        return escMenuController;
    }
}