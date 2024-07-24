package bank.com.bank.adminControllers;

import bank.com.bank.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class admin_historyController {
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
    private TableColumn<?, ?> numbercardotpr;

    @FXML
    private TableColumn<?, ?> numbercardpol;

    @FXML
    private TableColumn<?, ?> operation;

    @FXML
    private TableColumn<?, ?> fio_storyotpr;

    @FXML
    private TableColumn<?, ?> fio_storypol;

    @FXML
    private TableColumn<?, ?> numberpassportopl;

    @FXML
    private TableColumn<?, ?> numberpassportotpr;

    @FXML
    private TextField poisk;

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
        }

        // Сцена -> Заявки
        buttonzayavki.setOnAction(actionEvent -> {
            buttonzayavki.getScene().getWindow().hide();
            Stage currentStage = (Stage) buttonzayavki.getScene().getWindow();

            FXMLLoader load = new FXMLLoader();
            load.setLocation(ReLog.class.getResource("admin_zayavki.fxml"));

            try {
                Parent root = load.load();
                Stage newStage = new Stage();
                newStage.initOwner(currentStage);
                newStage.setTitle("Заявки на проблемы");
                newStage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        poisk.setOnAction(actionEvent -> {
            loadHistory(poisk.getText());
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
        ObservableList<cardHistory> history = FXCollections.observableArrayList(dbHandler.LoadHistoryAdmin(numCard));

        columndate.setCellValueFactory(new PropertyValueFactory<>("date"));
        operation.setCellValueFactory(new PropertyValueFactory<>("operation"));
        numbercardotpr.setCellValueFactory(new PropertyValueFactory<>("numbercardotpr"));
        numbercardpol.setCellValueFactory(new PropertyValueFactory<>("numbercardpol"));
        summa.setCellValueFactory(new PropertyValueFactory<>("summa"));
        fio_storypol.setCellValueFactory(new PropertyValueFactory<>("fioPol"));
        fio_storyotpr.setCellValueFactory(new PropertyValueFactory<>("fioOtpr"));
        numberpassportopl.setCellValueFactory(new PropertyValueFactory<>("numPassPol"));
        numberpassportotpr.setCellValueFactory(new PropertyValueFactory<>("numPassOtpr"));

        table.setItems(history);

    }

}
