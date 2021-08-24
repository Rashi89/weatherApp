module pl.agatarachanska {
    requires javafx.controls;
    requires javafx.fxml;
    requires  org.json;
    requires java.xml;

    opens pl.agatarachanska to javafx.fxml, java.xml;
    exports pl.agatarachanska;
    exports pl.agatarachanska.controller;
    opens pl.agatarachanska.controller to javafx.fxml;
}