package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public PasswordField password_field;
    public Button login_btn;
    public Button exit_btn;
    public ImageView logo_img;
    public Text user1_txt;
    public Text user2_txt;
    public Text user3_txt;
    public Text user4_txt;
    public Text user5_txt;
    public ToggleButton user1_btn;
    public ToggleGroup group;
    public ToggleButton user2_btn;
    public ToggleButton user3_btn;
    public ToggleButton user4_btn;
    public ToggleButton user5_btn;
    public Button changePw_btn;
    public Button forgotPw_btn;
    public Text user6_txt;
    public ToggleButton user6_btn;
    public AnchorPane root;
    public Button seePw;
    String currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        seePw.setOnMousePressed(event -> {
            password_field.setPromptText(password_field.getText());
            password_field.clear();
        });
        seePw.setOnMouseReleased(event -> {
            password_field.setText(password_field.getPromptText());
            password_field.setPromptText("PASSWORD");
        });


        login_btn.setOnAction(event ->
            {
                if(getUser()){
                    if(sqliteModel.auth(currentUser, password_field.getText())){
                        Model.getInstance().setUser(currentUser);

                        Model.getInstance().setAdmin(Objects.equals(currentUser, "Glorifina") || Objects.equals(currentUser, "Roy"));

                        if(Model.getInstance().getViewFactory().getEscMenuController() != null){
                            Model.getInstance().getViewFactory().getEscMenuController().setUser(currentUser);
                        }
                        Model.getInstance().getViewFactory().setSceneTable(true);
                    }
                    else{
                        Model.getInstance().getViewFactory().showErrorPopup("INCORRECT PASSWORD FOR USER " + currentUser);
                    }
                }
            }
        );
        changePw_btn.setOnAction(event ->{
            if(getUser()){
                Model.getInstance().setUser(currentUser);
                Model.getInstance().getViewFactory().setSceneChangePw(currentUser);
            }
        });
        forgotPw_btn.setOnAction(event -> sqliteModel.forgotPw());
        exit_btn.setOnAction(actionEvent -> {
            if(Model.getInstance().getViewFactory().showConfirmPopup("Are you sure you want to exit?")){
                Platform.exit();
            }
        });

        password_field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login_btn.fire();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                root.requestFocus();
                event.consume();
            }
        });

        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                exit_btn.fire();
            }
        });



//        setButton(user1_btn);
//        setButton(user2_btn);
//        setButton(user3_btn);
//        setButton(user4_btn);
//        setButton(user5_btn);
//        setButton(user6_btn);
    }

    private boolean getUser(){
        boolean result = true;
        if(group.getSelectedToggle() == user1_btn){
            currentUser = user1_txt.getText();
        }
        else if(group.getSelectedToggle() == user2_btn){
            currentUser = user2_txt.getText();
        }
        else if(group.getSelectedToggle() == user3_btn){
            currentUser = user3_txt.getText();
        }
        else if(group.getSelectedToggle() == user4_btn){
            currentUser = user4_txt.getText();
        }
        else if(group.getSelectedToggle() == user5_btn) {
            currentUser = user5_txt.getText();
        }
        else if(group.getSelectedToggle() == user6_btn){
                currentUser = user6_txt.getText();
        } else {
            Model.getInstance().getViewFactory().showErrorPopup("NO USER SELECTED");
            result = false;
        }
        return result;
    }

//    private void setButton(ToggleButton button) {
//        RotateTransition rotateTransition = new RotateTransition(Duration.millis(100), button);
//        rotateTransition.setByAngle(20); // Rotate by 20 degrees
//        rotateTransition.setCycleCount(1);
//
//        // Handle hover events
//        button.setOnMouseEntered(event -> {
//            rotateTransition.setRate(1.0); // Normal speed
//            rotateTransition.play();
//        });
//
//        button.setOnMouseExited(event -> {
//            rotateTransition.setRate(-1.0); // Reverse speed
//            rotateTransition.play();
//        });
//    }


}