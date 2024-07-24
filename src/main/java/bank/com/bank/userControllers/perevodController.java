package bank.com.bank.userControllers;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class perevodController {
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
    private Button buttonconnectwithbank;

    @FXML
    private Button buttonconnectsbp;

    @FXML
    private Button buttonhistory;

    @FXML
    private Button buttonperevod;

    @FXML
    private Button buttonexit;

    @FXML
    private Button buttonperevodsubmit;

    @FXML
    private Button buttonyourcards;
    @FXML
    private Button buttonvklads;

    @FXML
    private ChoiceBox<String> cards;

    @FXML
    private TextField inputnumcard;

    @FXML
    private TextField inputsumma;

    @FXML
    private ChoiceBox<String> pickerkakperevod;

    @FXML
    private Text textnumcard;

    @FXML
    private Text textnumcard1;

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
            ObservableList<String> cardsListSave = FXCollections.observableArrayList(cardsNUMB.getnumbercards().split(" "));
            cards.setValue(cardsListSave.get(0));
            cards.setItems(cardsListSave);
            UserMoney.setText(card.getbalance() + "Rub");
        } else{
            loadCards();
        }
        cards.setOnAction(actionEvent -> {
            loadCard(cards.getValue());
        });
        ObservableList<String> kakperevodit = FXCollections.observableArrayList("Система быстрых платежей(По номеру)","По номеру карты");
        pickerkakperevod.setItems(kakperevodit);
        pickerkakperevod.setValue(kakperevodit.get(0));
        textnumcard.setText("Введите номер телефона");
        pickerkakperevod.setOnAction(actionEvent -> {
            if (pickerkakperevod.getValue().equals("Система быстрых платежей(По номеру)")) {
                textnumcard.setText("Введите номер телефона");
            } else{
                textnumcard.setText("Введите номер карты");
            }
        });

        // Перевод
        buttonperevodsubmit.setOnAction(actionEvent -> {
            String kakperevod = pickerkakperevod.getValue();
            String number = inputnumcard.getText();
            String value = inputsumma.getText();
            if (number.isEmpty()){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Счёт не найден");
                errorAlert.setContentText("Пожалуйста проверьте корректность ввода номера карты или номера телефона");
                errorAlert.showAndWait();
            } else if (value.isEmpty()){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Некорректная сумма");
                errorAlert.setContentText("Пожалуйста проверьте корректность ввода суммы");
                errorAlert.showAndWait();
            } else if (number.equals(user.getnumberPhone()) || number.equals(card.getNumber())) {
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Вы пытаетесь перевести на ваш счёт");
                errorAlert.setContentText("Пожалуйста введите другой номер");
                errorAlert.showAndWait();
            } else if (Double.valueOf(value) < 0) {
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Провертье ввод суммы");
                errorAlert.setContentText("Пожалуйста введите сумму без лишних символов, только цифры");
                errorAlert.showAndWait();
            } else if (number.isEmpty()){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                if (kakperevod.equals("Система быстрых платежей(По номеру)")) {
                    errorAlert.setHeaderText("Провертье ввод номера телефона");
                    errorAlert.setContentText("Пожалуйста введите номер телефона");
                } else {
                    errorAlert.setHeaderText("Провертье ввод номера телефона");
                    errorAlert.setContentText("Пожалуйста введите номер телефона");
                }
                errorAlert.showAndWait();
            } else if (value.isEmpty()) {
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Провертье ввод суммы");
                errorAlert.setContentText("Пожалуйста введите сумму без лишних символов, только цифры");
                errorAlert.showAndWait();
            } else {
                perevod(kakperevod,number,value);
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
        // Сцена -> Мои карты
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

        // Сцена -> СБП
        buttonconnectsbp.setOnAction(actionEvent -> {
            buttonperevod.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonperevod.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("sbpConnect.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.setTitle("Подключение СБП");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.initOwner(currentStage);
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

    private void perevod(String kakperevod, String number, String value) {
        DataBaseHandler dbHandler = new DataBaseHandler();
        if (dbHandler.perevod(kakperevod,number,value,cards.getValue())){
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Успешно");
            errorAlert.setContentText("Перевод успешно выполнен!");
            errorAlert.showAndWait();
            loadCard(cards.getValue());
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
