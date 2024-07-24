package bank.com.bank.jclass;

import javafx.beans.property.*;

public class cardsNUMB {
    private static StringProperty numbercards = new SimpleStringProperty();

    public cardsNUMB() {
        this.numbercards = new SimpleStringProperty();
    }
    // Геттеры
    public static String getnumbercards() {
        return numbercards.get();
    }

    // Сеттеры

    public static void setnumbercards(String newNumber) {
        numbercards.set(newNumber);
    }
}
