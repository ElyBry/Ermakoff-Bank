package bank.com.bank.userControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import bank.com.bank.ReLog;
import database.DataBaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class createCardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button createCard;

    @FXML
    void initialize() {
        createCard.setOnAction(event -> {
           createCard();
        });
    }

    private void createCard() {
        DataBaseHandler dbHandler = new DataBaseHandler();
        boolean createCardB = dbHandler.createCard();
        if (createCardB){
            createCard.getScene().getWindow().hide();

            FXMLLoader loadreg = new FXMLLoader();
            loadreg.setLocation(ReLog.class.getResource("account.fxml"));
            dbHandler.LoadCards();
            try {
                loadreg.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loadreg.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Переводы");
            stage.getIcons().add(new Image(ReLog.class.getResourceAsStream("/img/E.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }
    }
}
