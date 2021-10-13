package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RewardScreenController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label userScoreLabel, messageLabel, promptLabel;
    @FXML
    private Button playAgainBtn, mainMenuBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF " + Score.getTotalScore());
    }
    
    @FXML
    private void newQuiz(ActionEvent event) throws IOException {
        if (Module.moduleTypeEqualsTo(ModuleType.practise)) {
            SceneController.goToPractiseModule();
        } else {
            SceneController.goToGamesModule();
        }
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        SceneController.goToMainMenu();
    }
    

}
