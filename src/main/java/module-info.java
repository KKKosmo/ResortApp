module com.resort.resortapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires kernel;
    requires layout;

    opens com.resort.resortapp to javafx.fxml;
    exports com.resort.resortapp;
    exports com.resort.resortapp.Controllers;
    exports com.resort.resortapp.Models;
    exports com.resort.resortapp.Views;
    opens com.resort.resortapp.Models to javafx.fxml;
}