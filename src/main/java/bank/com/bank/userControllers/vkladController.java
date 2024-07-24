package bank.com.bank.userControllers;

import bank.com.bank.ReLog;
import bank.com.bank.jclass.*;
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

public class vkladController {
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
    private Button buttoncreate;

    @FXML
    private Button buttonexit;

    @FXML
    private Button buttonhistory;

    @FXML
    private Button buttonperevod;

    @FXML
    private Button buttonpopolnit;

    @FXML
    private Button buttonvklads;

    @FXML
    private Button buttonyourcards;

    @FXML
    private ChoiceBox<String> cards;

    @FXML
    private ChoiceBox<String> pickervklad;

    @FXML
    private TextField fieldsumm;

    @FXML
    private AnchorPane panecreatevklad;

    @FXML
    private AnchorPane panecreatevklad1;

    @FXML
    private Text textnumcard;

    @FXML
    private Text textnumvklad;

    @FXML
    private Text textpercent;

    @FXML
    private Text textpribil;

    @FXML
    private Text textrub;
    @FXML
    private Text textdate;
    @FXML
    private Text textdateopen;

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

        // Проверка наличия вклада + отображение
        if (checkVklad()){
            panecreatevklad.setOpacity(0);
            panecreatevklad.setDisable(true);
            panecreatevklad1.setOpacity(1);
            panecreatevklad1.setDisable(false);
            textrub.setText(String.valueOf(vklad.getSum()) + "Rub");
            textpercent.setText(String.valueOf(vklad.getPercent()) + "%");
            textpribil.setText(String.valueOf(vklad.getGetmon())  + "Rub");
            textnumvklad.setText(vklad.getNumber_vklad());
            textdate.setText(String.valueOf(vklad.getEnd_date()));
            textdateopen.setText(String.valueOf(vklad.getCreate_date()));
        } else{
            panecreatevklad.setOpacity(1);
            ObservableList<String> percentanddate = FXCollections.observableArrayList(percents.getPercents().split(";"));
            panecreatevklad.setDisable(false);
            pickervklad.setItems(percentanddate);
            panecreatevklad1.setOpacity(0);
            panecreatevklad1.setDisable(true);
            buttoncreate.setOnAction(actionEvent -> {
                if (pickervklad.getValue() == null){
                    Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                    errorAlert.setHeaderText("Выберите процентную ставку");
                    errorAlert.setContentText("Пожалуйста Выберите процентную ставку");
                    errorAlert.showAndWait();
                } else {
                    createVklad();
                    panecreatevklad.setOpacity(0);
                    panecreatevklad.setDisable(true);
                    panecreatevklad1.setOpacity(1);
                    panecreatevklad1.setDisable(false);
                    textrub.setText(String.valueOf(vklad.getSum()) + "Rub");
                    textpercent.setText(String.valueOf(vklad.getPercent()) + "%");
                    textpribil.setText(String.valueOf(vklad.getGetmon())  + "Rub");
                    textnumvklad.setText(vklad.getNumber_vklad());
                    textdate.setText(String.valueOf(vklad.getEnd_date()));
                    textdateopen.setText(String.valueOf(vklad.getCreate_date()));
                }
            });
        }
        // Пополнить вклад
        buttonpopolnit.setOnAction(actionEvent -> {
            String value = fieldsumm.getText();
            if (value.isEmpty()){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Некорректная сумма");
                errorAlert.setContentText("Пожалуйста проверьте корректность ввода суммы");
                errorAlert.showAndWait();
            } else if (Double.valueOf(value) < 0) {
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Провертье ввод суммы");
                errorAlert.setContentText("Пожалуйста введите сумму без лишних символов, только цифры");
                errorAlert.showAndWait();
            } else if (value.isEmpty()) {
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Провертье ввод суммы");
                errorAlert.setContentText("Пожалуйста введите сумму без лишних символов, только цифры");
                errorAlert.showAndWait();
            } else if (Double.parseDouble(value) > Double.parseDouble(card.getbalance())){
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("Не хватает средств");
                errorAlert.setContentText("Пожалуйста пополните счёт для проведения данной операции");
                errorAlert.showAndWait();
            } else {
                popolnitVklad();
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

    private void popolnitVklad() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        if (dbHandler.PopolnitVklad(fieldsumm.getText(),user.getLogin())){
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("Успешно");
            errorAlert.setContentText("Перевод успешно выполнен!");
            errorAlert.showAndWait();
            loadCard(cards.getValue());
            checkVklad();
            textrub.setText(String.valueOf(vklad.getSum()) + "Rub");
            textpercent.setText(String.valueOf(vklad.getPercent()) + "%");
            textpribil.setText(String.valueOf(vklad.getGetmon())  + "Rub");
        }
    }

    private void createVklad() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        dbHandler.CreateVklad(user.getLogin(),pickervklad.getValue().split(" ")[4].replace("%",""),pickervklad.getValue().split(" ")[2]);

    }

    private boolean checkVklad() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        if (dbHandler.CheckVklad(user.getLogin())){
            return true;
        }
        return false;
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
