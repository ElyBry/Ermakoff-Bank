package bank.com.bank.jclass;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class percents {
    private static StringProperty percents = new SimpleStringProperty();

    public percents() {
        this.percents = new SimpleStringProperty();
    }
    // Геттеры
    public static String getPercents() {
        return percents.get();
    }

    // Сеттеры

    public static void setPercents(String newNumber) {
        percents.set(newNumber);
    }
}
