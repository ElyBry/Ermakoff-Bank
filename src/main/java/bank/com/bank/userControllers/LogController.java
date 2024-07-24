package bank.com.bank.userControllers;

import java.io.IOException;
import java.sql.SQLException;

import bank.com.bank.ReLog;
import bank.com.bank.jclass.user;
import database.DataBaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LogController {

    @FXML
    private Button chapterlog;

    @FXML
    private Button chapterreg;

    @FXML
    private TextField inplogin;

    @FXML
    private PasswordField inppass;

    @FXML
    private TextField inppassnum;

    @FXML
    private TextField inppassser;

    @FXML
    private AnchorPane loginanchor;

    @FXML
    private Button loginbutton;

    @FXML
    void onLoginClick(ActionEvent event) {
        loginUser(inplogin.getText(), inppass.getText(), inppassser.getText() + " " + inppassnum.getText());
    }

    @FXML
    void initialize() {

        //Переход между сценами
        chapterreg.setOnAction(event -> {
            chapterlog.getScene().getWindow().hide();

            FXMLLoader loadreg = new FXMLLoader();
            loadreg.setLocation(ReLog.class.getResource("signUp.fxml"));

            try {
                loadreg.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loadreg.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Регистрация");
            stage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });
    }
    private void loginUser(String loginText, String passText, String passportText) {
        loginanchor.setDisable(true);
        if (loginText.isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Введите логин");
            errorAlert.showAndWait();
            loginanchor.setDisable(false);
        } else if (passText.isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Введите пароль");
            errorAlert.showAndWait();
            loginanchor.setDisable(false);
        } else if (passportText.isEmpty() || passportText.length() != 11){
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Введите пасспорт");
            errorAlert.showAndWait();
            loginanchor.setDisable(false);
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Загрузка данных");
            errorAlert.showAndWait();
            DataBaseHandler dbHandler = new DataBaseHandler();
            String login = dbHandler.logIn(loginText, passText, passportText);
            if (!login.equals("-1")) {
                int card = 0;
                try {
                    card = dbHandler.checkCards(login);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                chapterlog.getScene().getWindow().hide();
                Stage currentStage = (Stage) chapterlog.getScene().getWindow();

                FXMLLoader loadlog = new FXMLLoader();
                String title = "";
                if (card > 0) {
                    loadlog.setLocation(ReLog.class.getResource("account.fxml"));
                    title = "Переводы";
                } else {
                    loadlog.setLocation(ReLog.class.getResource("createcard.fxml"));
                    title = "Создать карту";
                }
                if (user.getAdmin() == 1){
                    loadlog.setLocation(ReLog.class.getResource("admin_history.fxml"));
                    title = "История переводов";
                }
                try {
                    Parent root = loadlog.load();
                    Stage newStage = new Stage();
                    newStage.initOwner(currentStage);
                    newStage.setTitle(title);
                    newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                    newStage.setScene(new Scene(root));
                    newStage.show();
                } catch (IOException e) {
                    loginanchor.setDisable(false);
                    throw new RuntimeException(e);
                }

            } else {
                loginanchor.setDisable(false);
            }
        }
    }
}
