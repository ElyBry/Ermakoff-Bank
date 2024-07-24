package bank.com.bank.userControllers;

import bank.com.bank.ReLog;
import bank.com.bank.jclass.card;
import bank.com.bank.jclass.cardHistory;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class historyController {
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
    private Button buttonconnectwithbank;

    @FXML
    private Button buttonexit;

    @FXML
    private Button buttonhistory;

    @FXML
    private Button buttonperevod;

    @FXML
    private Button buttonvklads;
    @FXML
    private Button buttonyourcards;

    @FXML
    private ChoiceBox<String> cards;

    @FXML
    private TableColumn<?, ?> columndate;

    @FXML
    private TableColumn<?, ?> numbercardotpr;

    @FXML
    private TableColumn<?, ?> numbercardpol;

    @FXML
    private TableColumn<?, ?> operation;

    @FXML
    private TableColumn<?, ?> summa;

    @FXML
    private TableView<cardHistory> table;

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
        loadHistory(cards.getValue());
        cards.setOnAction(actionEvent -> {
            loadCard(cards.getValue());
            loadHistory(cards.getValue());
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

        // Сцена -> СБП
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
    private void loadHistory(String numCard){
        DataBaseHandler dbHandler = new DataBaseHandler();
        List<cardHistory> cardHistoryList = dbHandler.LoadHistory(numCard);
        ObservableList<cardHistory> history = FXCollections.observableArrayList(cardHistoryList);

        // Привязка данных к столбцам таблицы
        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));
        operation.setCellValueFactory(new PropertyValueFactory<>("operation"));
        numbercardotpr.setCellValueFactory(new PropertyValueFactory<>("numbercardotpr"));
        numbercardpol.setCellValueFactory(new PropertyValueFactory<>("numbercardpol"));
        summa.setCellValueFactory(new PropertyValueFactory<>("summa"));

        // Заполнение таблицы данными
        table.setItems(history);

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
