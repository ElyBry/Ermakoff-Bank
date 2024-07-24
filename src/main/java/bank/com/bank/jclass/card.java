package bank.com.bank.jclass;

import javafx.beans.property.*;

public class card {
    private static IntegerProperty id_card;
    private static StringProperty number;
    private static StringProperty holder_name;
    private static StringProperty expiry_date;
    private static StringProperty cvv;
    private static StringProperty balance;
    private static BooleanProperty is_active;

    public card() {
        this.id_card = new SimpleIntegerProperty();
        this.number = new SimpleStringProperty();
        this.holder_name = new SimpleStringProperty();
        this.expiry_date = new SimpleStringProperty();
        this.cvv = new SimpleStringProperty();
        this.balance = new SimpleStringProperty();
        this.is_active = new SimpleBooleanProperty();
    }
    // Геттеры
    public static Integer getId_card() {
        return id_card.get();
    }
    public static String getNumber() {
        return number.get();
    }
    public static String getHolder_name() {
        return holder_name.get();
    }

    public static String getExpiry_date() {
        return expiry_date.get();
    }

    public static String getCvv() {
        return cvv.get();
    }
    public static Boolean getIs_active() {
        return is_active.get();
    }
    public static String getbalance() {
        return balance.get();
    }

    // Сеттеры
    public static void setId_card(int newId) {
        id_card.set(newId);
    }

    public static void setNumber(String newNumber) {
        number.set(newNumber);
    }

    public static void setHolder_name(String newHolderName) {
        holder_name.set(newHolderName);
    }

    public static void setExpiry_date(String newExpiryDate) {
        expiry_date.set(newExpiryDate);
    }

    public static void setCvv(String newCvv) {
        cvv.set(newCvv);
    }

    public static void setIs_active(boolean isActive) {
        is_active.set(isActive);
    }

    public static void setBalance(String newBalance) {
        balance.set(newBalance);
    }
}
