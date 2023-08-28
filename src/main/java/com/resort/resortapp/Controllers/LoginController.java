package com.resort.resortapp.Controllers;
import com.resort.resortapp.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Scene visualsMonth;
    public PasswordField password_field;
    public Button login_btn;
    public Text error_txt;
    public Button exit_btn;
    public ImageView logo_img;
    Model model = new Model();

    public void setSceneLogin(Stage stage) {
        stage.setTitle("J&G Resort App");
        stage.setResizable(false);
        stage.show();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
            Scene login = new Scene(loader.load());
            stage.setScene(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_btn.setOnAction(event -> {
            if (visualsMonth == null) {
                try {
                    Stage stage = (Stage)login_btn.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/VisualsMonth.fxml"));
                    visualsMonth = new Scene(loader.load());
                    stage.setScene(visualsMonth);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}