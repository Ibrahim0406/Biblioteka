module com.biblioteka.biblioteka {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires kernel;
    requires layout;
    requires io;

    opens com.biblioteka to javafx.fxml;
    opens com.biblioteka.controller to javafx.fxml;
    exports com.biblioteka;
}