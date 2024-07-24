package bank.com.bank.userControllers;

import BCrypt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;

import bank.com.bank.ReLog;
import javafx.scene.image.*;
import java.util.ResourceBundle;
import database.DataBaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button chapterlog;

    @FXML
    private Button chapterreg;

    @FXML
    private DatePicker inpbirthday;

    @FXML
    private TextField inpfamily;

    @FXML
    private TextField inplogin;

    @FXML
    private TextField inpname;

    @FXML
    private TextField inpotch;

    @FXML
    private PasswordField inppass;

    @FXML
    private PasswordField inppasscheck;

    @FXML
    private TextField inppassportnum;

    @FXML
    private TextField inppassportser;

    @FXML
    private RadioButton inpsexm;

    @FXML
    private RadioButton inpsexw;

    @FXML
    private AnchorPane reganchor;

    @FXML
    private Button regbutton;

    @FXML
    private ToggleGroup y;



    @FXML
    void initialize() {

        //Кнопка зарегистрироваться
        regbutton.setOnAction(event -> {
            String loginText = inplogin.getText().trim();
            String passText = inppass.getText().trim();
            LocalDate birthdate = inpbirthday.getValue();

            String passcheckText = inppasscheck.getText().trim();
            String fioText = inpfamily.getText().trim() + " " + inpname.getText().trim() + " " + inpotch.getText().trim();
            String passportText = inppassportser.getText().trim() + " " + inppassportnum.getText().trim();
            int err = 0;
            if (passText.length() < 8){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Ваш пароль слишком короткий");
                errorAlert.setContentText("Пожалуйста добавьте символов (минимум 8 символов)");
                errorAlert.showAndWait();
                err ++;
            }
            else if (loginText.isEmpty() || loginText.length() < 4 || !loginText.matches("[а-яА-Яa-zA-Z]+")){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Логин не введён");
                errorAlert.setContentText("Пожалуйста введите логин из БУКВ! (Минимальная длина логина 4)");
                errorAlert.showAndWait();
                err ++;
            } else if (inpname.getText().isEmpty() || inpfamily.getText().isEmpty() || inpotch.getText().isEmpty() || !inpname.getText().matches("[а-яА-Яa-zA-Z]+") || !inpfamily.getText().matches("[а-яА-Яa-zA-Z]+") || !inpotch.getText().matches("[а-яА-Яa-zA-Z]+")){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Провертье ввод ФИО");
                errorAlert.setContentText("Пожалуйста введите ФИО");
                errorAlert.showAndWait();
                err ++;
            } else if (inppassportser.getText().isEmpty() || inppassportnum.getText().isEmpty() || !inppassportser.getText().matches("\\d+") || inppassportser.getText().length() != 4 || inppassportnum.getText().length() != 6 || !inppassportnum.getText().matches("\\d+")){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Провертье ввод Паспортных данных");
                errorAlert.setContentText("Пожалуйста введите Паспортные данные");
                errorAlert.showAndWait();
                err ++;
            } else if (!passText.equals(passcheckText)){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Пароли не совпадают");
                errorAlert.showAndWait();
                err ++;
            } else if (passText.isEmpty() || passcheckText.isEmpty()){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Пароль пустой");
                errorAlert.showAndWait();
                err ++;
            } else if (birthdate == null){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Введите дату рождения");
                errorAlert.showAndWait();
                err ++;
            } else if (!inpsexm.isSelected() && !inpsexw.isSelected()) {
                System.out.println("Пол не выбран");
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Пол не выбран");
                errorAlert.setContentText("Пожалуйста выберите пол");
                errorAlert.showAndWait();
                err ++;
            }

            if (err == 0) {
                String sex = "Ж";
                if (inpsexm.isSelected()) {
                    sex = "М";
                }
                Stage currentStage = (Stage) chapterlog.getScene().getWindow();
                regUser(loginText, BCrypt.hashpw(passText,BCrypt.gensalt()), passportText, fioText,sex,birthdate,currentStage);
            } else{ System.out.println("Ошибка ввода");}

        });
        //Переход между сценами
        chapterlog.setOnAction(event -> {
            chapterlog.getScene().getWindow().hide();
            Stage currentStage = (Stage) chapterlog.getScene().getWindow();

            FXMLLoader loadlog = new FXMLLoader();
            loadlog.setLocation(ReLog.class.getResource("login.fxml"));

            try {
                Parent root = loadlog.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("Войти");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

    private void regUser(String loginText, String passText, String passportText, String fioText, String sex, LocalDate birthdate, Stage currentStage ) {
        reganchor.setDisable(true);
        DataBaseHandler dbHandler = new DataBaseHandler();
        boolean reg = dbHandler.signUp(loginText,passText,fioText,passportText,sex,birthdate);
        if (reg) {
            chapterlog.getScene().getWindow().hide();


            FXMLLoader loadlog = new FXMLLoader();
            loadlog.setLocation(ReLog.class.getResource("login.fxml"));

            try {
                Parent root = loadlog.load();
                Stage newStage = new Stage();
                newStage.setTitle("Войти");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.initOwner(currentStage);
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            reganchor.setDisable(false);
        }
    }
}
