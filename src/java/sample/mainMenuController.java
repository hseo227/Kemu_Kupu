package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class mainMenuController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainMenuLabel;
    @FXML
    private Button newSQ, quit;


    // load New Spelling Quiz
    @FXML
    private void loadNew(ActionEvent event) throws IOException {
        SceneController.goToTopicList();
    }

    // Quit the game
    @FXML
    private void quit(ActionEvent event) {
        Platform.exit();
    }

}
