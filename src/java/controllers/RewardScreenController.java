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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF " + Score.getTotalScore());
    }
    
    @FXML
    private void newQuiz() throws IOException {
        if (Module.moduleTypeEqualsTo(ModuleType.practise)) {
            SceneManager.goToPractiseModule();
        } else {
            SceneManager.goToGamesModule();
        }
    }

    @FXML
    private void backToMainMenu() throws IOException {
        SceneManager.goToMainMenu();
    }
    

}
