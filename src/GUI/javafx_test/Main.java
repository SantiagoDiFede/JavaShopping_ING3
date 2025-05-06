package GUI.javafx_test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;


public class Main extends Application {

    public void start(@NotNull Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

        Scene scene = new Scene(root, 1115, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("SHOPPING.JAVA");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}