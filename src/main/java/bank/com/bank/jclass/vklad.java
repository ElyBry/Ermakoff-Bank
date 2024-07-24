package bank.com.bank.jclass;

import javafx.beans.property.*;

import java.time.LocalDate;

public class vklad {
    private static DoubleProperty sum;
    private static IntegerProperty percent;
    private static DoubleProperty getmon;
    private static StringProperty number_vklad;
    private static StringProperty create_date;
    private static StringProperty end_date;
    private static StringProperty user_login;

    public vklad() {
        this.sum = new SimpleDoubleProperty();
        this.percent = new SimpleIntegerProperty();
        this.getmon = new SimpleDoubleProperty();
        this.number_vklad = new SimpleStringProperty();
        this.create_date = new SimpleStringProperty();
        this.end_date = new SimpleStringProperty();
        this.user_login = new SimpleStringProperty();
    }

    // Геттеры
    public static double getSum() {
        return sum.get();
    }

    public static int getPercent() {
        return percent.get();
    }

    public static double getGetmon() {
        return getmon.get();
    }

    public static String getNumber_vklad() {
        return number_vklad.get();
    }

    public static String getCreate_date() {
        return create_date.get();
    }

    public static String getEnd_date() {
        return end_date.get();
    }

    public static String getUser_login() {
        return user_login.get();
    }

    // Сеттеры
    public static void setSum(double newSum) {
        sum.set(newSum);
    }

    public static void setPercent(int newPercent) {
        percent.set(newPercent);
    }

    public static void setGetmon(double newGetmon) {
        getmon.set(newGetmon);
    }

    public static void setNumber_vklad(String newNumber) {
        number_vklad.set(newNumber);
    }

    public static void setCreate_date(String newCreateDate) {
        create_date.set(newCreateDate);
    }

    public static void setEnd_date(String newEndDate) {
        end_date.set(newEndDate);
    }

    public static void setUser_login(String newUserLogin) {
        user_login.set(newUserLogin);
    }
}