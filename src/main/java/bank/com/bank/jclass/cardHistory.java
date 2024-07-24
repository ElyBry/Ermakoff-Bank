package bank.com.bank.jclass;

import javafx.beans.property.*;

public class cardHistory {
    private StringProperty summa;
    private StringProperty date;
    private StringProperty operation;
    private StringProperty numbercardotpr;
    private StringProperty numbercardpol;
    private BooleanProperty success;
    private StringProperty fio_storyotpr;
    private StringProperty fio_storypol;
    private StringProperty numberpassportopl;
    private StringProperty numberpassportotpr;

    public cardHistory() {
        this.summa = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
        this.operation = new SimpleStringProperty();
        this.numbercardotpr = new SimpleStringProperty();
        this.numbercardpol = new SimpleStringProperty();
        this.success = new SimpleBooleanProperty();
        this.fio_storyotpr = new SimpleStringProperty();
        this.fio_storypol = new SimpleStringProperty();
        this.numberpassportopl = new SimpleStringProperty();
        this.numberpassportotpr = new SimpleStringProperty();
    }
    // Геттеры
    public String getFioOtpr() {
        return fio_storyotpr.get();
    }
    public String getFioPol() {
        return fio_storypol.get();
    }
    public String getNumPassOtpr() {
        return numberpassportopl.get();
    }
    public String getNumPassPol() {
        return numberpassportotpr.get();
    }
    public String getSumma() {
        return summa.get();
    }
    public String getDate() {
        return date.get();
    }
    public String getOperation() {
        return operation.get();
    }

    public String getNumbercardotpr() {
        return numbercardotpr.get();
    }

    public String getNumbercardpol() {
        return numbercardpol.get();
    }
    public Boolean getSuccess() {
        return success.get();
    }

    // Сеттеры
    public void setFioOtpr(String newfiootpr) {
        fio_storyotpr.set(newfiootpr);
    }
    public void setFioPol(String newfiopol) {
        fio_storypol.set(newfiopol);
    }
    public void setNumPassOtpr(String newNumPassOtpr) {
        numberpassportopl.set(newNumPassOtpr);
    }
    public void setNumPassPol(String newNumPassPol) {
        numberpassportotpr.set(newNumPassPol);
    }
    public void setSumma(String newSumma) {
        summa.set(newSumma);
    }
    public void setDate(String newNumber) {
        date.set(newNumber);
    }
    public void setOperation(String newHolderName) {
        operation.set(newHolderName);
    }
    public void setNumbercardotpr(String newExpiryDate) {
        numbercardotpr.set(newExpiryDate);
    }
    public void setNumbercardpol(String newCvv) {
        numbercardpol.set(newCvv);
    }
    public void setSuccess(boolean isActive) {
        success.set(isActive);
    }

}
