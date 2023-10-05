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
import javafx.scene.text.Text;

import java.net.URL;
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
    String currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(event ->
            {
                if(getUser()){
                    if(sqliteModel.auth(currentUser, password_field.getText())){
                        Model.getInstance().setUser(currentUser);
                        if(Model.getInstance().getViewFactory().getEscMenuController() != null){
                            Model.getInstance().getViewFactory().getEscMenuController().setUser(currentUser);
                        }
                        Model.getInstance().getViewFactory().setSceneTable(false);
                    }
                    else{
                        Model.getInstance().getViewFactory().showErrorPopup("Error: INCORRECT PASSWORD FOR USER " + currentUser);
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
        exit_btn.setOnAction(actionEvent -> Platform.exit());

        password_field.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login_btn.fire();
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
            Model.getInstance().getViewFactory().showErrorPopup("Error: NO USER SELECTED");
            result = false;
        }
        return result;
    }

//    private void setButton(ToggleButton button) {
//        button.setOnAction(event -> {
//            if (!button.getStyleClass().contains("selected-button")) {
//                button.getStyleClass().add("selected-button");
//            } else {
//                button.getStyleClass().remove("selected-button");
//            }
//
//            group.getToggles().forEach(toggle -> {
//                if (toggle != button) {
//                    ((ToggleButton) toggle).getStyleClass().remove("selected-button");
//                }
//            });
//
////            System.out.println(group.getSelectedToggle());
//        });
//    }


}