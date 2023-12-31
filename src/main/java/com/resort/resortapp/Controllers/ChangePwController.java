package com.resort.resortapp.Controllers;

import com.resort.resortapp.Models.Model;
import com.resort.resortapp.Models.sqliteModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePwController implements Initializable {
    public Text user_txt;
    public PasswordField newPassword_fld;
    public Button submit_btn;
    public Button cancel_btn;
    public PasswordField oldPassword_fld;
    public PasswordField confirmNewPassword_fld;
    public AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancel_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().setSceneLogin());
        submit_btn.setOnAction(actionEvent -> {
            if(sqliteModel.auth(user_txt.getText(), oldPassword_fld.getText())){
                if(newPassword_fld.getText().isEmpty()){
                    Model.getInstance().getViewFactory().showErrorPopup("NEW PASSWORD CANNOT BE EMPTY.");
                }
                else{
                    if(newPassword_fld.getText().equals(confirmNewPassword_fld.getText())){
                        if(sqliteModel.changePw(user_txt.getText(), newPassword_fld.getText())){
                            Model.getInstance().getViewFactory().showSuccessPopup("Password for user " + user_txt.getText() + " changed successfully.");
                            Model.getInstance().getViewFactory().setSceneLogin();
                        }
                    }
                    else{
                        Model.getInstance().getViewFactory().showErrorPopup("NEW PASSWORD FIELDS DO NOT MATCH.");
                    }
                }
            }
            else{
                Model.getInstance().getViewFactory().showErrorPopup("OLD PASSWORD IS INCORRECT.");
            }
        });


        oldPassword_fld.getParent().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submit_btn.fire();
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                root.requestFocus();
                event.consume();
            }
        });

        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                cancel_btn.fire();
            }
        });
    }

    public void setValues(String user){
        user_txt.setText(user);
    }
}