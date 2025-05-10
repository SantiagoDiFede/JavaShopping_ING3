module GUI.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;
    requires java.sql;


    opens GUI.javafx to javafx.fxml;
    opens Controller to javafx.fxml;
    exports GUI.javafx;
}