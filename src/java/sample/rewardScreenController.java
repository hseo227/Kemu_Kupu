package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class rewardScreenController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainLabel, promptLabel, userScore;
    @FXML
    private TextField inputField;
    @FXML
    private Button playAgainBtn, mainMenuBtn;

    @FXML
    private void startQuiz(ActionEvent event) throws IOException {
    	SceneController.goToQuiz();
    }
    
    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        SceneController.goToMainMenu();
    }

}
