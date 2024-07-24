package database;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import BCrypt.BCrypt;
import bank.com.bank.jclass.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import static bank.com.bank.jclass.card.*;

public class DataBaseHandler extends Config{
    Connection dbConnection;
    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String DB_URL = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, dbUser, dbPass);
            return conn;
        } catch(Exception ex) {ex.printStackTrace();}
        return null;
    }
    public boolean signUp(String loginText, String passText, String fioText, String passportText, String sex, LocalDate birthdate) {
        String checkReg = "SELECT COUNT(*) FROM users WHERE login = ? OR passport = ?";
        String insert = "INSERT INTO users (login,password,fio,passport,date_of_birth,gender,age,number) VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(checkReg);
            selectStatement.setString(1, loginText);
            selectStatement.setString(2, passportText);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Пользователь с таким логином или паспортными данными уже существует");
                alert.showAndWait();
                return false;
            }
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(birthdate,currentDate);
            PreparedStatement prst = getDbConnection().prepareStatement(insert);
            prst.setString(1,loginText);
            prst.setString(2,passText);
            prst.setString(3,fioText);
            prst.setString(4,passportText);
            prst.setString(5,birthdate.toString());
            prst.setString(6,sex);
            prst.setString(7, String.valueOf(period.getYears()));
            prst.setString(8, "0");
            prst.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Успешно");
            alert.setContentText("Вы успешно зарегистрировались");
            alert.setHeaderText(null);
            alert.showAndWait();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public String logIn(String loginText, String passText, String passportText) {
        String checkUser = "SELECT * FROM users WHERE login = ? AND passport = ?";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(checkUser);
            selectStatement.setString(1, loginText);
            selectStatement.setString(2, passportText);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()){
                if (BCrypt.checkpw(passText,resultSet.getString("password"))) {
                    user user = new user();
                    user.setId(resultSet.getInt("id"));
                    user.setFio(resultSet.getString("fio"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPassport(resultSet.getString("passport"));
                    user.setDateOfBirth(resultSet.getString("date_of_birth"));
                    user.setnumberPhone(resultSet.getString("number"));
                    user.setSex(resultSet.getString("gender"));
                    user.setAdmin(resultSet.getInt("admin"));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Вы успешно авторизовались!");
                    alert.setContentText("Вы успешно авторизовались");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    return resultSet.getString("login");
                } else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Пароль не верный");
                    alert.showAndWait();
                    return "-1";
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Пользователь с таким логином или паспортными данными уже существует");
                alert.showAndWait();
                return "-1";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int checkCards(String login) throws SQLException, ClassNotFoundException {
        String checkCard = "SELECT * FROM cards WHERE holder_name = ?";
        PreparedStatement selectStatement = getDbConnection().prepareStatement(checkCard);
        selectStatement.setString(1, login);
        ResultSet resultSet = selectStatement.executeQuery();
        if (resultSet.next()){
            return 1;
        } else {
            return 0;
        }
    }
    public StringBuilder generatorCardNum(){
        // Генерация номера карты
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        // Генерация первых 6 цифр (BIN - Bank Identification Number)
        for (int i = 0; i < 6; i++) {
            cardNumber.append(random.nextInt(10));
        }
        // Генерация оставшихся 10 цифр (Account Number)
        for (int i = 0; i < 10; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber;
    }
    public StringBuilder generatorCVV(){
        Random random = new Random();
        StringBuilder cvv = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cvv.append(random.nextInt(10));
        }
        return cvv;
    }
    public String LoadCards(){
        String card = "SELECT * FROM cards WHERE holder_name = ?";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(card);
            selectStatement.setString(1, user.getLogin());
            ResultSet resultSet = selectStatement.executeQuery();
            String cardNumbers = "";
            while (resultSet.next()) {
                String cardNumber = resultSet.getString("card_number");
                cardNumbers += cardNumber + " ";
            }
            cardsNUMB cardsNUMB = new cardsNUMB();
            cardsNUMB.setnumbercards(cardNumbers.trim());
            return cardNumbers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean createCard() {
        String createCard = "INSERT INTO cards (card_number,holder_name,expiry_date,cvv,balance,is_active,number) VALUES(?,?,?,?,?,?,?)";
        String checkNumCard = "SELECT COUNT(*) FROM cards WHERE card_number = ?";
        try{
            while (true){
                PreparedStatement selectStatement = getDbConnection().prepareStatement(checkNumCard);
                StringBuilder cardNumber = generatorCardNum();
                selectStatement.setString(1, String.valueOf(cardNumber));
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) == 0){
                    break;
                }
            }
            PreparedStatement selectStatement = getDbConnection().prepareStatement(createCard);
            selectStatement.setString(1, String.valueOf(generatorCardNum()));
            selectStatement.setString(2, user.getLogin());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 10);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
            selectStatement.setString(3, sdf.format(calendar.getTime()));
            selectStatement.setString(4, generatorCVV().toString());
            selectStatement.setInt(5,0);
            selectStatement.setBoolean(6,true);
            selectStatement.setString(7,"0");
            selectStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            card card = new card();
            card.setNumber(String.valueOf(generatorCardNum()));
            card.setHolder_name(user.getLogin());
            card.setExpiry_date(sdf.format(calendar.getTime()));
            card.setCvv(generatorCVV().toString());
            card.setBalance(String.valueOf(0.00));
            card.setIs_active(true);
            alert.setTitle("Карта успешно создана");
            alert.setHeaderText(null);
            alert.showAndWait();
            return true;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String LoadCard(String numCard) {
        String cardz = "SELECT * FROM cards WHERE card_number = ?";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(cardz);
            selectStatement.setString(1, numCard.trim());
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                card card = new card();
                card.setId_card(resultSet.getInt(1));
                setNumber(resultSet.getString(2));
                card.setCvv(resultSet.getString("cvv"));
                card.setExpiry_date(resultSet.getString("expiry_date"));
                card.setBalance(resultSet.getBigDecimal("balance").toString());
                return resultSet.getBigDecimal("balance").toString();
            } else{
                System.out.println("Карта не найдена");
                return "---------------";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateNumberPhone(String numberphone) {
        String number = "update users SET number = ? WHERE login = ?";
        String checkNum = "SELECT count(*) FROM users WHERE number = ?";
        String sbpActive = "update cards SET sbp = ? WHERE holder_name = ?";
        try {
            //ПРОВЕРКА НОМЕРА
            PreparedStatement selectStatement = getDbConnection().prepareStatement(checkNum);
            selectStatement.setString(1, String.valueOf(numberphone));
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) != 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Пользователь с таким номером уже существует");
                alert.showAndWait();
                return false;
            }
            // ОБНОВЛЕНИЕ В USERS
            selectStatement = getDbConnection().prepareStatement(number);
            selectStatement.setString(1, numberphone.trim());
            selectStatement.setString(2, user.getLogin());
            selectStatement.executeUpdate();
            // ОБНОВЛЕНИЕ В CARDS
            selectStatement = getDbConnection().prepareStatement(sbpActive);
            selectStatement.setBoolean(1, true);
            selectStatement.setString(2, user.getLogin());
            selectStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean disconnectSBP() {
        String sbpActive = "update cards SET sbp = ? WHERE holder_name = ?";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(sbpActive);
            selectStatement.setBoolean(1, false);
            selectStatement.setString(2,user.getLogin());
            selectStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public double getBalance(String card_number){
        String cardBalance = "SELECT * FROM cards WHERE card_number = ?";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(cardBalance);
            selectStatement.setString(1, card_number);
            ResultSet resultCard = selectStatement.executeQuery();
            if (resultCard.next()) {
                return Double.parseDouble(resultCard.getBigDecimal("balance").toString());
            } else{
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean perevod(String kakperevod, String number, String value, String numberotkuda) {
        Double balance = getBalance(numberotkuda.trim());
        Double valuePer = Double.parseDouble(value);
        if (balance <= valuePer){
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("У вас недостаточно средств для перевода");
            errorAlert.setContentText("Пожалуйста пополните счёт для проведения операции");
            errorAlert.showAndWait();
            return false;
        } else {
            if (kakperevod.equals("Система быстрых платежей(По номеру)")) {
                String user = "SELECT * FROM users WHERE number = ?";
                try {
                    PreparedStatement selectStatement = getDbConnection().prepareStatement(user);
                    selectStatement.setString(1, number);
                    ResultSet resultUser = selectStatement.executeQuery();
                    if (!resultUser.next()){
                        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                        errorAlert.setHeaderText("Пользователь не найден");
                        errorAlert.setContentText("Пожалуйста проверьте корректность введённого номера");
                        errorAlert.showAndWait();
                        return false;
                    }
                    String card = "SELECT * FROM cards WHERE holder_name = ? LIMIT 1";
                    selectStatement = getDbConnection().prepareStatement(card);
                    selectStatement.setString(1, resultUser.getString("login"));
                    ResultSet resultCard = selectStatement.executeQuery();
                    if (!resultCard.next()){
                        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                        errorAlert.setHeaderText("Данный пользователь не имеет счетов");
                        errorAlert.setContentText("Возможно у него заблокирован счёт либо его не существует");
                        errorAlert.showAndWait();
                        return false;
                    }
                    String userfio = resultUser.getString("fio");
                    if (!resultCard.getBoolean("sbp")) {
                        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                        errorAlert.setHeaderText("У пользователя не подключена Система Быстрых Платежей");
                        errorAlert.setContentText("Пожалуйста скажите " + userfio + " о возможности подключения СБП");
                        errorAlert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Вы уверены?");
                        alert.setHeaderText("Перевод будет осуществлён " + userfio + " на сумму " + valuePer + " руб");
                        alert.setContentText("Данную операцию не возможно будет отменить");
                        ButtonType buttonTypeYes = new ButtonType("Перевести");
                        ButtonType buttonTypeNo = new ButtonType("Изменить", ButtonBar.ButtonData.CANCEL_CLOSE);
                        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTypeYes) {
                            String addHistory = "INSERT INTO historytransfer (sender_number_card,recipient_number_card,transfer_amount,date) VALUES(?,?,?,?)";
                            String updateBalancePrihod = "update cards SET balance = ? WHERE holder_name = ? LIMIT 1";
                            String updateBalanceOtpr = "update cards SET balance = ? WHERE card_number = ? LIMIT 1";
                            String findNumCard = "Select * From cards where holder_name = ? LIMIT 1";
                            Double newBalanceOtpr = balance - valuePer;
                            Double newBalancePrihod = Double.parseDouble(String.valueOf(resultCard.getBigDecimal("balance"))) + valuePer;
                            PreparedStatement selectStatementoptr = getDbConnection().prepareStatement(updateBalanceOtpr);
                            selectStatementoptr.setBigDecimal(1, BigDecimal.valueOf(newBalanceOtpr));
                            selectStatementoptr.setString(2, numberotkuda);
                            selectStatementoptr.executeUpdate();
                            PreparedStatement selectStatementprhiod = getDbConnection().prepareStatement(updateBalancePrihod);
                            selectStatementprhiod.setBigDecimal(1, BigDecimal.valueOf(newBalancePrihod));
                            selectStatementprhiod.setString(2, resultUser.getString("login"));
                            selectStatementprhiod.executeUpdate();
                            PreparedStatement findNumCardState = getDbConnection().prepareStatement(findNumCard);
                            findNumCardState.setString(1, resultUser.getString("login"));
                            ResultSet resultFindNumCard = findNumCardState.executeQuery();
                            if (!resultFindNumCard.next()){
                                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                                errorAlert.setHeaderText("Произошла ошибка");
                                errorAlert.setContentText("Приносим свои извинения, попробуйте повторить операцию позже (");
                                errorAlert.showAndWait();
                                return false;
                            }
                            PreparedStatement addHistoryState = getDbConnection().prepareStatement(addHistory);
                            addHistoryState.setString(1,numberotkuda);
                            addHistoryState.setString(2,resultFindNumCard.getString("card_number"));
                            addHistoryState.setBigDecimal(3, BigDecimal.valueOf(valuePer));
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            addHistoryState.setString(4, sdf.format(calendar.getTime()));
                            addHistoryState.executeUpdate();
                            return true;
                        } else{
                            alert.close();
                        }
                    }
                } catch(SQLException e){
                    throw new RuntimeException(e);
                } catch(ClassNotFoundException e){
                    throw new RuntimeException(e);
                }

            } else{
                String card = "SELECT * FROM cards WHERE card_number = ?";
                try {
                    PreparedStatement selectStatement = getDbConnection().prepareStatement(card);
                    selectStatement.setString(1, number);
                    ResultSet resultCard = selectStatement.executeQuery();
                    if (!resultCard.next()){
                        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                        errorAlert.setHeaderText("Счёт не найден");
                        errorAlert.setContentText("Пожалуйста проверьте корректность введённого номера карты");
                        errorAlert.showAndWait();
                        return false;
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Вы уверены?");
                    Double newBalanceOtpr = balance - valuePer;
                    alert.setHeaderText("Перевод будет осуществлён на номер " + number + " на сумму " + valuePer + " руб. У вас останется на счету " + newBalanceOtpr);
                    alert.setContentText("Данную операцию не возможно будет отменить");
                    ButtonType buttonTypeYes = new ButtonType("Перевести");
                    ButtonType buttonTypeNo = new ButtonType("Изменить", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeYes) {
                        String addHistory = "INSERT INTO historytransfer (sender_number_card,recipient_number_card,transfer_amount,date) VALUES(?,?,?,?)";
                        String updateBalancePrihod = "update cards SET balance = ? WHERE card_number = ? LIMIT 1";
                        String updateBalanceOtpr = "update cards SET balance = ? WHERE card_number = ? LIMIT 1";
                        Double newBalancePrihod = Double.parseDouble(String.valueOf(resultCard.getBigDecimal("balance"))) + valuePer;
                        PreparedStatement selectStatementoptr = getDbConnection().prepareStatement(updateBalanceOtpr);
                        selectStatementoptr.setBigDecimal(1, BigDecimal.valueOf(newBalanceOtpr));
                        selectStatementoptr.setString(2, numberotkuda);
                        selectStatementoptr.executeUpdate();
                        PreparedStatement selectStatementprhiod = getDbConnection().prepareStatement(updateBalancePrihod);
                        selectStatementprhiod.setBigDecimal(1, BigDecimal.valueOf(newBalancePrihod));
                        selectStatementprhiod.setString(2, number);
                        selectStatementprhiod.executeUpdate();
                        PreparedStatement addHistoryState = getDbConnection().prepareStatement(addHistory);
                        addHistoryState.setString(1,numberotkuda);
                        addHistoryState.setString(2,number);
                        addHistoryState.setBigDecimal(3, BigDecimal.valueOf(valuePer));
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        addHistoryState.setString(4, sdf.format(calendar.getTime()));
                        addHistoryState.executeUpdate();
                        return true;
                    } else{
                        alert.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return false;
            }
        }
        return false;
    }

    public List<cardHistory> LoadHistory(String numCard) {
        List<cardHistory> historyList = new ArrayList<>();
        String history = "SELECT * FROM historytransfer WHERE sender_number_card = ? || recipient_number_card = ?";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(history);
            selectStatement.setString(1, numCard);
            selectStatement.setString(2, numCard);
            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                cardHistory historyEntry = new cardHistory();
                historyEntry.setDate(resultSet.getString("date"));
                historyEntry.setNumbercardotpr(resultSet.getString("sender_number_card"));
                historyEntry.setNumbercardpol(resultSet.getString("recipient_number_card"));
                historyEntry.setSumma(resultSet.getString("transfer_amount"));
                String senderNumberCard = resultSet.getString("sender_number_card");
                if (senderNumberCard.equals(numCard)) {
                    historyEntry.setOperation("Отправление");
                } else {
                    historyEntry.setOperation("Зачисление");
                }
                historyList.add(historyEntry);
            }
            return historyList;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public List<cardHistory> LoadHistoryAdmin(String numCard) {
        List<cardHistory> historyList = new ArrayList<>();
        String history = "SELECT * FROM historytransfer WHERE sender_number_card LIKE CONCAT('%', ?, '%') OR recipient_number_card LIKE CONCAT('%', ?, '%')";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(history);
            selectStatement.setString(1, numCard);
            selectStatement.setString(2, numCard);
            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                cardHistory historyEntry = new cardHistory();
                historyEntry.setDate(resultSet.getString("date"));
                historyEntry.setNumbercardotpr(resultSet.getString("sender_number_card"));
                historyEntry.setNumbercardpol(resultSet.getString("recipient_number_card"));
                historyEntry.setSumma(resultSet.getString("transfer_amount"));
                String senderNumberCard = resultSet.getString("sender_number_card");
                if (senderNumberCard.equals(numCard)) {
                    historyEntry.setOperation("Отправление");
                } else {
                    historyEntry.setOperation("Зачисление");
                }

                String getnick = "SELECT * FROM cards WHERE card_number = ?";
                selectStatement = getDbConnection().prepareStatement(getnick);
                selectStatement.setString(1, resultSet.getString("sender_number_card"));
                ResultSet senderResult = selectStatement.executeQuery();
                String userStr = "select * FROM users WHERE login = ?";
                if (senderResult.next()) {
                    String nickOtpr = senderResult.getString("holder_name");
                    selectStatement = getDbConnection().prepareStatement(userStr);
                    selectStatement.setString(1, nickOtpr);
                    ResultSet Otpr = selectStatement.executeQuery();
                    if (Otpr.next()){
                        historyEntry.setFioOtpr(Otpr.getString("fio"));
                        historyEntry.setNumPassOtpr(Otpr.getString("passport"));
                    } else {
                        historyEntry.setFioOtpr("-");
                        historyEntry.setNumPassOtpr("-");
                    }
                }
                selectStatement = getDbConnection().prepareStatement(getnick);
                selectStatement.setString(1, resultSet.getString("recipient_number_card"));
                ResultSet recipientResult = selectStatement.executeQuery();
                if (recipientResult.next()) {
                    String nickPol = recipientResult.getString("holder_name");
                    selectStatement = getDbConnection().prepareStatement(userStr);
                    selectStatement.setString(1, nickPol);
                    ResultSet Poluch = selectStatement.executeQuery();
                    if (Poluch.next()) {
                        historyEntry.setFioPol(Poluch.getString("fio"));
                        historyEntry.setNumPassPol(Poluch.getString("passport"));
                    } else {
                        historyEntry.setFioPol("-");
                        historyEntry.setNumPassPol("-");
                    }
                }
                historyList.add(historyEntry);
            }
            return historyList;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean sendProblem(String textproblem, Object valuewhereproblem) {
        String createCard = "INSERT INTO admin_banking (login_user, problem,problem_text ,send_date ,is_successful) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(createCard);
            selectStatement.setString(1, user.getLogin());
            selectStatement.setString(2, (String) valuewhereproblem);
            selectStatement.setString(3, textproblem);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            selectStatement.setString(4, sdf.format(calendar.getTime()));
            selectStatement.setBoolean(5, false);
            int resultSet = selectStatement.executeUpdate();
            if (resultSet > 0){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<zayavki> LoadZayavki(String loginUser) {
        List<zayavki> zayavkaList = new ArrayList<>();
        String history = "SELECT * FROM admin_banking WHERE login_user LIKE CONCAT('%', ?, '%')";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(history);
            selectStatement.setString(1, loginUser);
            ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                zayavki zayavkiEnter = new zayavki();
                zayavkiEnter.setLogin(resultSet.getString("login_user"));
                zayavkiEnter.setDate(resultSet.getString("send_date"));
                zayavkiEnter.setProblem(resultSet.getString("problem"));
                zayavkiEnter.setTextProblem(resultSet.getString("problem_text"));
                int check = resultSet.getInt("is_successful");
                if (check == 1){
                    zayavkiEnter.setIs_successful("Решено");
                } else{
                    zayavkiEnter.setIs_successful("Нерешено");
                }

                zayavkaList.add(zayavkiEnter);
            }
            return zayavkaList;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean CheckVklad(String loginUser) {
        String checkVklad = "SELECT * FROM vklads WHERE user_login = ?";
        String percentdate = "SELECT * FROM interest_rates";
        try {
            PreparedStatement selectStatement = getDbConnection().prepareStatement(checkVklad);
            selectStatement.setString(1, loginUser);
            ResultSet resultVklad = selectStatement.executeQuery();
            selectStatement = getDbConnection().prepareStatement(percentdate);
            ResultSet resultSet = selectStatement.executeQuery();
            String perc = "";
            while (resultSet.next()) {
                String VkladPerc = "К-во мес: " + resultSet.getString("term") +" Процент: " + resultSet.getDouble("interest") + "%";
                perc += VkladPerc + ";";
            }
            percents percents = new percents();
            percents.setPercents(perc.trim());
            if (resultVklad.next()){
                vklad vklad = new vklad();
                vklad.setSum(resultVklad.getDouble("sum"));
                vklad.setPercent(resultVklad.getInt("percent"));
                vklad.setGetmon(resultVklad.getDouble("getmon"));
                vklad.setNumber_vklad(resultVklad.getString("number_vklad"));
                vklad.setCreate_date(String.valueOf(resultVklad.getDate("create_date")));
                vklad.setEnd_date(String.valueOf(resultVklad.getDate("end_date")));
                vklad.setUser_login(resultVklad.getString("user_login"));

                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean CreateVklad(String login, String percent, String c_month) {
        String createVklad = "INSERT INTO vklads (sum,percent,getmon,number_vklad,create_date,end_date,user_login) VALUES(?,?,?,?,?,?,?)";
        String checkNumVklad = "SELECT COUNT(*) FROM vklads WHERE number_vklad = ?";
        try{
            while (true){
                PreparedStatement selectStatement = getDbConnection().prepareStatement(checkNumVklad);
                StringBuilder cardNumber = generatorCardNum();
                selectStatement.setString(1, String.valueOf(cardNumber));
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) == 0){
                    break;
                }
            }
            PreparedStatement selectStatement = getDbConnection().prepareStatement(createVklad);
            vklad vklad = new vklad();
            selectStatement.setInt(1, 0);
            selectStatement.setDouble(2, Double.parseDouble(percent));
            Calendar calendar = Calendar.getInstance();
            selectStatement.setInt(3, 0);
            String vkladNum = String.valueOf(generatorCardNum());
            selectStatement.setString(4, String.valueOf(vkladNum));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            vklad.setCreate_date(sdf.format(calendar.getTime()));
            selectStatement.setDate(5, Date.valueOf(sdf.format(calendar.getTime())));
            calendar.add(Calendar.MONTH, Integer.parseInt(c_month));
            selectStatement.setDate(6, Date.valueOf(sdf.format(calendar.getTime())));
            selectStatement.setString(7,login);
            selectStatement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            vklad.setNumber_vklad(vkladNum);
            vklad.setSum(0);
            vklad.setGetmon(0);
            vklad.setPercent((int) Double.parseDouble(percent));
            vklad.setEnd_date(sdf.format(calendar.getTime()));
            vklad.setUser_login(login);
            alert.setTitle("Вклад успешно открыт");
            alert.setHeaderText(null);
            alert.showAndWait();
            return true;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean PopolnitVklad(String summ, String login) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Вы уверены?");
        Double newBalanceCard = Double.parseDouble(card.getbalance()) - Double.parseDouble(summ);
        alert.setHeaderText("Перевод будет осуществлён на вклад " + vklad.getNumber_vklad() + " на сумму " + summ + " руб. У вас останется на счету " + newBalanceCard);
        alert.setContentText("Данную операцию не возможно будет отменить");
        ButtonType buttonTypeYes = new ButtonType("Перевести");
        ButtonType buttonTypeNo = new ButtonType("Изменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            String addHistory = "INSERT INTO historytransfer (sender_number_card,recipient_number_card,transfer_amount,date) VALUES(?,?,?,?)";
            String updateBalanceVklad = "update vklads SET sum = ? WHERE number_vklad = ? LIMIT 1";
            String updateBalanceCard = "update cards SET balance = ? WHERE card_number = ? LIMIT 1";
            try {
                Double newBalanceVklad = vklad.getSum() + Double.parseDouble(summ);
                PreparedStatement selectStatementoptr = getDbConnection().prepareStatement(updateBalanceCard);
                selectStatementoptr.setBigDecimal(1, BigDecimal.valueOf(newBalanceCard));
                selectStatementoptr.setString(2, card.getNumber());
                selectStatementoptr.executeUpdate();
                PreparedStatement selectStatementprhiod = getDbConnection().prepareStatement(updateBalanceVklad);
                selectStatementprhiod.setBigDecimal(1, BigDecimal.valueOf(newBalanceVklad));
                selectStatementprhiod.setString(2, vklad.getNumber_vklad());
                selectStatementprhiod.executeUpdate();
                PreparedStatement addHistoryState = getDbConnection().prepareStatement(addHistory);
                addHistoryState.setString(1,card.getNumber());
                addHistoryState.setString(2, vklad.getNumber_vklad());
                addHistoryState.setBigDecimal(3, BigDecimal.valueOf(Long.parseLong(summ)));
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                addHistoryState.setString(4, sdf.format(calendar.getTime()));
                addHistoryState.executeUpdate();
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else{
            alert.close();
        }
        return false;
    }
}