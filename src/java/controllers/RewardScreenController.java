package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;
import spellingQuizUtil.Score;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RewardScreenController implements Initializable {

    @FXML
    private Label userScoreLabel;

    /**
     * Set up the user score label
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF " + Score.getTotalScore());
    }

    /**
     * When 'Play Again' button is pressed, play the quiz again, either practise or games module
     */
    @FXML
    private void playAgain() throws IOException {
        if (Module.moduleTypeEqualsTo(ModuleType.PRACTISE)) {
            SceneManager.goToPractiseModule();
        } else {
            SceneManager.goToGamesModule();
        }
    }

    /**
     * When 'Main menu' button is pressed, go back to main menu
     */
    @FXML
    private void backToMainMenu() throws IOException {
        SceneManager.goToMainMenu();
    }
    

}
