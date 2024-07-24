module bank.com.bank {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;

    opens bank.com.bank to javafx.fxml;
    exports bank.com.bank;
    exports BCrypt;
    opens BCrypt to javafx.fxml;
    exports bank.com.bank.adminControllers;
    opens bank.com.bank.adminControllers to javafx.fxml;
    exports bank.com.bank.jclass;
    opens bank.com.bank.jclass to javafx.fxml;
    exports bank.com.bank.userControllers;
    opens bank.com.bank.userControllers to javafx.fxml;
}