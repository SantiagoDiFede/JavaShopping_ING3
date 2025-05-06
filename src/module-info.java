module GUI.javafx_test {
    requires javafx.controls;
    requires javafx.fxml;
    requires annotations;
    requires java.sql;


    opens GUI.javafx_test to javafx.fxml;
    exports GUI.javafx_test;
}