module pl.agatarachanska {
    requires javafx.controls;
    requires javafx.fxml;

    opens pl.agatarachanska to javafx.fxml;
    exports pl.agatarachanska;
}