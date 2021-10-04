package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainMenuController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainMenuLabel;
    @FXML
    private Button practiseModule, gamesModule;
    @FXML
    private ImageView quitImg;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // The text when the mouse hover on the quit image
        Tooltip tooltip = new Tooltip("Click to quit the game");
        Tooltip.install(quitImg, tooltip);
    }

    // load New Spelling Quiz
    @FXML
    private void loadGamesModule(ActionEvent event) throws IOException {
        SceneController.goToTopicList();
    }

    // Quit the game
    @FXML
    private void quit(ActionEvent event) {
        Platform.exit();
    }

}
