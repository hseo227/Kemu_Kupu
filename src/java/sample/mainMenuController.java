package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class mainMenuController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainMenuLabel;
    @FXML
    private Button gamesModule, quit;


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
