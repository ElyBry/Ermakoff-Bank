package bank.com.bank.userControllers;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import bank.com.bank.ReLog;
import bank.com.bank.jclass.card;
import bank.com.bank.jclass.cardsNUMB;
import bank.com.bank.jclass.user;
import database.DataBaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class sbpConnectController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text UserMoney;

    @FXML
    private Text UserName;

    @FXML
    private ImageView avatar;

    @FXML
    private Button buttonconnectsbp;

    @FXML
    private Button buttonconnectsbpsubmit;

    @FXML
    private Button buttonconnectwithbank;

    @FXML
    private Button buttondisconnectsbp;

    @FXML
    private Button buttonexit;

    @FXML
    private Button buttonhistory;

    @FXML
    private Button buttonperevod;

    @FXML
    private Button buttonyourcards;
    @FXML
    private Button buttonvklads;

    @FXML
    private TextField inputnumber;
    @FXML
    private ChoiceBox<String> cards;

    @FXML
    private AnchorPane sbpconnectpane;

    @FXML
    private Text textnum;

    @FXML
    private Text textsbp;

    @FXML
    private Text samnumactive;

    @FXML
    private Text textnumactive;

    @FXML
    private Text textsbp1;

    @FXML
    private Text textsbp11;

    @FXML
    void initialize() {
        UserName.setText(user.getFio());
        ObservableList<String> cardsListSave = FXCollections.observableArrayList(cardsNUMB.getnumbercards().split(" "));
        cards.setValue(cardsListSave.get(0));
        cards.setItems(cardsListSave);
        UserMoney.setText(card.getbalance() + "Rub");
        cards.setOnAction(actionEvent -> {
            loadCard(cards.getValue());
        });
        // Проверка на подключение СБП
        if (user.getnumberPhone() != null && !Objects.equals(user.getnumberPhone(), "0")){
            inputnumber.setDisable(true);
            inputnumber.setOpacity(0);
            buttonconnectsbpsubmit.setDisable(true);
            buttonconnectsbpsubmit.setOpacity(0);
            textsbp.setDisable(true);
            textsbp.setOpacity(0);
            textnum.setDisable(true);
            textnum.setOpacity(0);
            textsbp1.setDisable(false);
            textsbp1.setOpacity(1);
            textsbp11.setDisable(false);
            textsbp11.setOpacity(1);
            buttondisconnectsbp.setDisable(false);
            buttondisconnectsbp.setOpacity(1);
            textnumactive.setDisable(false);
            textnumactive.setOpacity(1);
            samnumactive.setDisable(false);
            samnumactive.setOpacity(1);
            samnumactive.setText(user.getnumberPhone());
        }
        // Отключение СБП
        buttondisconnectsbp.setOnAction(actionEvent -> {
            disconnectSBP();
        });
        // Подключение СБП
        buttonconnectsbpsubmit.setOnAction(actionEvent -> {
            String numberphone = inputnumber.getText();
            if (numberphone.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Введите номер телефона");
                alert.showAndWait();
            } else{
                if (numberphone.matches("[а-яА-Яa-zA-Z]+")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Проверьте корректность номера");
                    alert.showAndWait();
                } else{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Проверьте верно ли вы ввели номер");
                    alert.setHeaderText("Вы подтверждаете что это - "+ numberphone + " ваш номер?");
                    alert.setContentText("Данный номер нельзя будет изменить!");
                    ButtonType buttonTypeYes = new ButtonType("Согласен");
                    ButtonType buttonTypeNo = new ButtonType("Изменить", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeYes){
                        updateNumberPhone(numberphone);
                    } else{
                        alert.close();
                    }
                }
            }
        });
        // Сцена -> Вклады
        buttonvklads.setOnAction(actionEvent -> {
            buttonvklads.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonvklads.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("vklads.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("Вклады");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Сцена -> Связь с банком
        buttonconnectwithbank.setOnAction(actionEvent -> {
            buttonconnectwithbank.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonconnectwithbank.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("connectWithBank.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("Связь с банком");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Сцена -> история
        buttonhistory.setOnAction(actionEvent -> {
            buttonyourcards.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonyourcards.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("history.fxml"));

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
        // Сцена -> карты
        buttonyourcards.setOnAction(actionEvent -> {
            buttonyourcards.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonyourcards.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("MyCards.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("Мои карты");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Сцена -> Переводы
        buttonperevod.setOnAction(actionEvent -> {
            buttonperevod.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonperevod.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("account.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("Переводы");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

    private void disconnectSBP() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        if (dbHandler.disconnectSBP()){
            inputnumber.setDisable(false);
            inputnumber.setOpacity(1);
            buttonconnectsbpsubmit.setDisable(false);
            buttonconnectsbpsubmit.setOpacity(1);
            textsbp.setDisable(false);
            textsbp.setOpacity(1);
            textnum.setDisable(false);
            textnum.setOpacity(1);
            textsbp1.setDisable(true);
            textsbp1.setOpacity(0);
            textsbp11.setDisable(true);
            textsbp11.setOpacity(0);
            buttondisconnectsbp.setDisable(true);
            buttondisconnectsbp.setOpacity(0);
            textnumactive.setDisable(true);
            textnumactive.setOpacity(0);
            samnumactive.setDisable(true);
            samnumactive.setOpacity(0);
        }
    }

    private void updateNumberPhone(String numberphone) {
        DataBaseHandler dbHandler = new DataBaseHandler();
        if (dbHandler.updateNumberPhone(numberphone)){
            inputnumber.setDisable(true);
            inputnumber.setOpacity(0);
            buttonconnectsbpsubmit.setDisable(true);
            buttonconnectsbpsubmit.setOpacity(0);
            textsbp.setDisable(true);
            textsbp.setOpacity(0);
            textnum.setDisable(true);
            textnum.setOpacity(0);
            textsbp1.setDisable(false);
            textsbp1.setOpacity(1);
            textsbp11.setDisable(false);
            textsbp11.setOpacity(1);
            buttondisconnectsbp.setDisable(false);
            buttondisconnectsbp.setOpacity(1);
            textnumactive.setDisable(false);
            textnumactive.setOpacity(1);
            samnumactive.setDisable(false);
            samnumactive.setOpacity(1);
            samnumactive.setText("89530577454");
        }
    }

    private void loadCard(String numCard) {
        DataBaseHandler dbHandler = new DataBaseHandler();
        UserMoney.setText(dbHandler.LoadCard(numCard) + " Rub");
    }

    private void loadCards() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        ObservableList<String> cardsList = FXCollections.observableArrayList(dbHandler.LoadCards().split(" "));
        cards.setItems(cardsList);
        cards.setValue(cardsList.get(0));
        loadCard(cardsList.get(0));
    }
}
