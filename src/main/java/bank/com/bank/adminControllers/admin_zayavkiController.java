package bank.com.bank.adminControllers;

import bank.com.bank.ReLog;
import bank.com.bank.jclass.card;
import bank.com.bank.jclass.cardsNUMB;
import bank.com.bank.jclass.user;
import bank.com.bank.jclass.zayavki;
import database.DataBaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class admin_zayavkiController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text UserName;

    @FXML
    private ImageView avatar;

    @FXML
    private Button buttonexit;

    @FXML
    private Button buttonhistory;

    @FXML
    private Button buttonzayavki;

    @FXML
    private TableColumn<?, ?> columndate;

    @FXML
    private TableColumn<?, ?> login;

    @FXML
    private TextField poisk;

    @FXML
    private TableColumn<?, ?> problem;

    @FXML
    private TableColumn<?, ?> success;

    @FXML
    private TableView<zayavki> table;

    @FXML
    private TableColumn<?, ?> textproblem;

    @FXML
    void initialize() {
        UserName.setText(user.getFio());
        boolean checker = false;
        try {
            String cardNumbers = cardsNUMB.getnumbercards();
            if (cardNumbers != null) {
                checker = true;
            }
        } catch (Exception e) {
        }

        if (checker) {
            UserName.setText(user.getFio());
        }

        // Сцена -> Заявки
        buttonhistory.setOnAction(actionEvent -> {
            buttonhistory.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonhistory.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("admin_history.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("История переводов");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        poisk.setOnAction(actionEvent -> {
            loadZayavki(poisk.getText());
        });

        // Выйти
        buttonexit.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход");
            alert.setHeaderText("Вы уверены что вы хотите выйти из аккаунта?");
            alert.setContentText("После выхода все данные о вашем аккаунте будут удалены на данном устройстве!");
            ButtonType buttonTypeEx = new ButtonType("Выйти");
            ButtonType buttonTypeCancel = new ButtonType("Вернуться", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeEx, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeEx){
                user user = new user();
                card card = new card();
                cardsNUMB cardsNUMB = new cardsNUMB();
                buttonexit.getScene().getWindow().hide();

                FXMLLoader loadacc = new FXMLLoader();
                loadacc.setLocation(ReLog.class.getResource("login.fxml"));

                try {
                    loadacc.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Parent root = loadacc.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Войти");
                stage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } else{
                alert.close();
            }
        });
    }

    private void loadZayavki(String loginUser) {
        DataBaseHandler dbHandler = new DataBaseHandler();
        ObservableList<zayavki> zayavkiList = FXCollections.observableArrayList(dbHandler.LoadZayavki(loginUser));

        columndate.setCellValueFactory(new PropertyValueFactory<>("send_date"));
        problem.setCellValueFactory(new  PropertyValueFactory<>("problem"));
        login.setCellValueFactory(new PropertyValueFactory<>("login_user"));
        textproblem.setCellValueFactory(new PropertyValueFactory<>("problem_text"));
        success.setCellValueFactory(new PropertyValueFactory<>("is_successful"));

        table.setItems(zayavkiList);
    }


}
