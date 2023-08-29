package com.resort.resortapp.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class ViewFactory {

    private Scene visualsMonth;
    private Scene login;

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    private Calendar calendar;


    public Scene getSceneVisualsMonth() {
        if (visualsMonth == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/VisualsMonth.fxml"));
                visualsMonth = new Scene(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return visualsMonth;
    }

    public Scene getSceneLogin() {
        if (login == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
                login = new Scene(loader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return login;
    }
}