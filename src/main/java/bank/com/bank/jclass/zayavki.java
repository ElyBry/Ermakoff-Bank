package bank.com.bank.jclass;

import javafx.beans.property.*;

public class zayavki {
    private StringProperty login_user;
    private StringProperty problem;
    private StringProperty send_date;
    private StringProperty problem_text;
    private StringProperty is_successful;

    public zayavki() {
        this.login_user = new SimpleStringProperty();
        this.problem = new SimpleStringProperty();
        this.send_date = new SimpleStringProperty();
        this.problem_text = new SimpleStringProperty();
        this.is_successful = new SimpleStringProperty();
    }
    // Геттеры
    public String getLogin_user() {
        return login_user.get();
    }

    public String getProblem() {
        return problem.get();
    }

    public String getSend_date() {
        return send_date.get();
    }

    public String getProblem_text() {
        return problem_text.get();
    }

    public String getIs_successful() {
        return is_successful.get();
    }

    // Сеттеры

    public void setLogin(String newNumber) {
        login_user.set(newNumber);
    }

    public void setProblem(String newHolderName) {
        problem.set(newHolderName);
    }

    public void setDate(String newExpiryDate) {
        send_date.set(newExpiryDate);
    }

    public void setTextProblem(String newCvv) {
        problem_text.set(newCvv);
    }

    public void setIs_successful(String isActive) {
        is_successful.set(isActive);
    }

}
