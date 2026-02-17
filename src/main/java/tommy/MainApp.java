package tommy;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tommy.ui.MainWindow;
import tommy.Tommy;

public class MainApp extends Application {

    private final Tommy tommy = new Tommy("data/tommy.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(MainApp.class.getResource("/view/MainWindow.fxml"));

            VBox root = fxmlLoader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);

            fxmlLoader.<MainWindow>getController().setTommy(tommy);

            stage.setTitle("Tommy");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


