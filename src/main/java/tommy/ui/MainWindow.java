package tommy.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import tommy.Tommy;

public class MainWindow {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    private Tommy tommy;

    // Profile pictures
    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image tommyImage = new Image(this.getClass().getResourceAsStream("/images/tommy.png"));

    @FXML
    public void initialize() {
        // Scroll to bottom whenever dialogContainer grows
        dialogContainer.heightProperty().addListener((obs, oldVal, newVal) -> scrollPane.setVvalue(1.0));
    }

    public void setTommy(Tommy tommy) {
        this.tommy = tommy;

        // Display welcome message from Tommy
        dialogContainer.getChildren().add(getDialog("Hello! I'm Tommy. How can I help you?", tommyImage));
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isEmpty()) return;

        // Display user input
        dialogContainer.getChildren().add(getDialog(input, userImage));

        // Get Tommy's response
        String response = tommy.getResponse(input);
        dialogContainer.getChildren().add(getDialog(response, tommyImage));

        userInput.clear();
    }

    /**
     * Returns a HBox containing a Label (message) and an ImageView (profile picture)
     * styled as a dialog bubble.
     */
    private HBox getDialog(String text, Image img) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setStyle("-fx-background-color: lightgray; -fx-padding: 5; -fx-background-radius: 10;");

        ImageView displayPicture = new ImageView(img);
        displayPicture.setFitWidth(50);
        displayPicture.setFitHeight(50);

        HBox hbox = new HBox(10);

        if (img == tommyImage) {
            hbox.getChildren().addAll(displayPicture, label); // Tommy: image on left
            hbox.setAlignment(Pos.TOP_LEFT);
        } else {
            hbox.getChildren().addAll(label, displayPicture); // User: image on right
            hbox.setAlignment(Pos.TOP_RIGHT);
        }

        return hbox;
    }
}
