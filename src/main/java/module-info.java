module com.resort.resortapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.resort.resortapp to javafx.fxml;
    exports com.resort.resortapp;
}