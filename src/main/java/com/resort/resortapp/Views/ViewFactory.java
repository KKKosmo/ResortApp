package com.resort.resortapp.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewFactory {
    private AnchorPane visualsMonth;

    public ViewFactory(){}

    public AnchorPane getVisualsMonth() {
        if (visualsMonth == null){
            try{
                visualsMonth = new FXMLLoader(getClass().getResource("/Fxml/visualsMonth.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return visualsMonth;
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("J&G Resort App");
        stage.show();
    }


}
