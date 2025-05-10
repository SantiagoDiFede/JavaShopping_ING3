package GUI.javafx;

import Controller.ConnexionControlleur;
import Dao.DaoFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class Main extends Application {

    public void start(@NotNull Stage stage) throws Exception {

        DaoFactory daoFactory = DaoFactory.getInstance("shoppingjava", "root", "");

        ConnexionControlleur controller = new ConnexionControlleur(daoFactory);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        loader.setController(controller);
        Parent root = loader.load();


        Scene scene = new Scene(root, 1115, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        stage.setTitle("SHOPPING.JAVA");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}