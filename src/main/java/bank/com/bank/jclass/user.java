package bank.com.bank.jclass;

import javafx.beans.property.*;
import java.sql.Date;

public class user {
    private static IntegerProperty id;
    private static StringProperty login;
    private static StringProperty fio;
    private static StringProperty passport;
    private static StringProperty dateOfBirth;
    private static StringProperty sex;
    private static StringProperty numberPhone;
    private static IntegerProperty admin;
    public user() {
        this.id = new SimpleIntegerProperty();
        this.login = new SimpleStringProperty();
        this.fio = new SimpleStringProperty();
        this.passport = new SimpleStringProperty();
        this.dateOfBirth = new SimpleStringProperty();
        this.sex = new SimpleStringProperty();
        this.numberPhone = new SimpleStringProperty();
        this.admin = new SimpleIntegerProperty();
    }
    // Геттеры
    public static Integer getAdmin() {
        return admin.get();
    }
    public static Integer getId() {
        return id.get();
    }
    public static String getSex() { return sex.get(); }
    public static String getLogin() {
        return login.get();
    }
    public static String getnumberPhone() {
        return numberPhone.get();
    }
    public static String getFio() {
        return fio.get();
    }

    public static String getPassport() {
        return passport.get();
    }

    public static String getDateOfBirth() {
        return dateOfBirth.get();
    }

    // Сеттеры
    public void setAdmin(Integer id) {
        this.admin.set(id);
    }
    public void setId(Integer id) {
        this.id.set(id);
    }
    public void setSex(String sex) {
        this.sex.set(sex);
    }
    public void setLogin(String login) {
        this.login.set(login);
    }
    public void setnumberPhone(String login) {
        this.numberPhone.set(login);
    }
    public void setFio(String fio) {
        this.fio.set(fio);
    }

    public void setPassport(String passport) {
        this.passport.set(passport);
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }
}
