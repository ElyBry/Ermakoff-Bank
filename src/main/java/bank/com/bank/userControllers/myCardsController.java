package bank.com.bank.userControllers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class myCardsController {

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
    private Button buttonaddcard;

    @FXML
    private Button buttonconnectsbp;

    @FXML
    private Button buttonconnectwithbank;

    @FXML
    private Button buttonexit;

    @FXML
    private Button buttonhistory;

    @FXML
    private Button buttonnextcard;

    @FXML
    private Button buttonperevod;

    @FXML
    private Button buttonprevcard;

    @FXML
    private Button buttonyourcards;
    @FXML
    private Button buttonvklads;

    @FXML
    private AnchorPane cardaddpane;

    @FXML
    private Text cardcvv;

    @FXML
    private Text cardexpiry;

    @FXML
    private Text cardfi;

    @FXML
    private Text cardnumber;

    @FXML
    private AnchorPane cardpanelic;

    @FXML
    private AnchorPane cardpaneobr;


    @FXML
    private ChoiceBox<String> cards;

    @FXML
    void initialize() {
        // Заполнение правого верхнего угла
        UserName.setText(user.getFio());
        ObservableList<String> cardsListSave = FXCollections.observableArrayList(cardsNUMB.getnumbercards().split(" "));
        cards.setValue(cardsListSave.get(0));
        cards.setItems(cardsListSave);
        UserMoney.setText(card.getbalance() + "Rub");
        cards.setOnAction(actionEvent -> {
            loadCard(cards.getValue());
        });

        // Загрузка карты
        cardnumber.setText(numtoNum(cardsListSave.get(0)));
        cardexpiry.setText(card.getExpiry_date().replace("20",""));
        cardcvv.setText(card.getCvv());
        String fi = user.getFio();
        fi = fi.split(" ")[0] + " " + fi.split(" ")[1];
        cardfi.setText(fi);
        cardpanelic.setOnMouseClicked(mouseEvent -> {
            disablePane(cardpanelic);
            activePane(cardpaneobr);
        });
        cardpaneobr.setOnMouseClicked(mouseEvent -> {
            activePane(cardpanelic);
            disablePane(cardpaneobr);
        });
        // Скопировать номер карты
        cardnumber.setOnMouseClicked(mouseEvent -> {
            StringSelection stringSelection = new StringSelection(cardnumber.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Номер карты скопирован");
            errorAlert.showAndWait();
        });
        // Кнопки след обратно
        AtomicInteger InCountCards = new AtomicInteger();
        /// След
        buttonnextcard.setOnAction(actionEvent -> {
            InCountCards.getAndIncrement();
            if (cardsListSave.size() == 1){
                if (InCountCards.get() % 2 == 1) {
                    disablePane(cardpanelic);
                    disablePane(cardpaneobr);
                    activePane(cardaddpane);
                } else {
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                }
            } if (cardsListSave.size() == 2){
                if (InCountCards.get() % 3 == 0) {
                    disablePane(cardpanelic);
                    disablePane(cardpaneobr);
                    activePane(cardaddpane);
                } else if (InCountCards.get() % 3 == 1){
                    loadCard(cardsListSave.get(1));
                    cards.setValue(cardsListSave.get(1));
                    cardnumber.setText(numtoNum(cardsListSave.get(1)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                } else {
                    loadCard(cardsListSave.get(0));
                    cards.setValue(cardsListSave.get(0));
                    cardnumber.setText(numtoNum(cardsListSave.get(0)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                }
            } if (cardsListSave.size() == 3){
                if (InCountCards.get() % 3 == 0) {
                    loadCard(cardsListSave.get(0));
                    cards.setValue(cardsListSave.get(0));
                    cardnumber.setText(numtoNum(cardsListSave.get(0)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                } else if (InCountCards.get() % 3 == 1){
                    loadCard(cardsListSave.get(1));
                    cards.setValue(cardsListSave.get(1));
                    cardnumber.setText(numtoNum(cardsListSave.get(1)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                } else {
                    loadCard(cardsListSave.get(2));
                    cards.setValue(cardsListSave.get(2));
                    cardnumber.setText(numtoNum(cardsListSave.get(2)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                }
            }
        });

        /// Обратно
        buttonprevcard.setOnAction(actionEvent -> {
            InCountCards.getAndIncrement();
            if (cardsListSave.size() == 1){
                if (InCountCards.get() % 2 == 1) {
                    disablePane(cardpanelic);
                    disablePane(cardpaneobr);
                    activePane(cardaddpane);
                } else {
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                }
            } if (cardsListSave.size() == 2){
                if (InCountCards.get() % 3 == 0) {
                    disablePane(cardpanelic);
                    disablePane(cardpaneobr);
                    activePane(cardaddpane);
                } else if (InCountCards.get() % 3 == 1){
                    loadCard(cardsListSave.get(1));
                    cards.setValue(cardsListSave.get(1));
                    cardnumber.setText(numtoNum(cardsListSave.get(1)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                } else {
                    loadCard(cardsListSave.get(0));
                    cards.setValue(cardsListSave.get(0));
                    cardnumber.setText(numtoNum(cardsListSave.get(0)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                }
            } if (cardsListSave.size() == 3){
                if (InCountCards.get() % 3 == 0) {
                    loadCard(cardsListSave.get(0));
                    cards.setValue(cardsListSave.get(0));
                    cardnumber.setText(numtoNum(cardsListSave.get(0)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                } else if (InCountCards.get() % 3 == 1){
                    loadCard(cardsListSave.get(1));
                    cards.setValue(cardsListSave.get(1));
                    cardnumber.setText(numtoNum(cardsListSave.get(1)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
                } else {
                    loadCard(cardsListSave.get(2));
                    cards.setValue(cardsListSave.get(2));
                    cardnumber.setText(numtoNum(cardsListSave.get(2)));
                    cardexpiry.setText(card.getExpiry_date().replace("20",""));
                    cardcvv.setText(card.getCvv());
                    activePane(cardpanelic);
                    disablePane(cardpaneobr);
                    disablePane(cardaddpane);
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
                newStage.setTitle("Связь с банком");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.initOwner(currentStage);
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
        // Сцена -> Создание карты
        buttonaddcard.setOnAction(actionEvent -> {
            buttonaddcard.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonaddcard.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("createcard.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("Создание карты");
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

        // СБП
        buttonconnectsbp.setOnAction(actionEvent -> {
            buttonperevod.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonperevod.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("sbpConnect.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("Подключение СБП");
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

    private String numtoNum(String inp){
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < inp.length(); i++) {
            result.append(inp.charAt(i));
            if ((i + 1) % 4 == 0 && i != inp.length() - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }
    private void activePane(AnchorPane scene){
        scene.setDisable(false);
        scene.setOpacity(1);
    }
    private void disablePane(AnchorPane scene){
        scene.setDisable(true);
        scene.setOpacity(0);
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
