package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;
import spellingQuizUtil.Score;
import spellingQuizUtil.TestedWords;

import java.net.URL;
import java.util.ResourceBundle;

public class RewardScreenController implements Initializable {

    @FXML
    private Label userScoreLabel, word1Label, word2Label, word3Label, word4Label, word5Label;
private Text text;
    /**
     * Set up the user score label
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF " + Score.getTotalScore());
        
        word1Label.setText(TestedWords.get(0));
        word2Label.setText(TestedWords.get(1));
        word3Label.setText(TestedWords.get(2));
        word4Label.setText(TestedWords.get(3));
        word5Label.setText(TestedWords.get(4));
        
        if  (TestedWords.isPracticeMode(0)) {
            word1Label.setTextFill(Color.WHITE);
            word2Label.setTextFill(Color.WHITE);
            word3Label.setTextFill(Color.WHITE);
            word4Label.setTextFill(Color.WHITE);
            word5Label.setTextFill(Color.WHITE);
        } else if (TestedWords.isCorrect(0)) {
            word1Label.setTextFill(Color.LIME);
        }
        if (TestedWords.isCorrect(1)) {
            word2Label.setTextFill(Color.LIME);
        }
        if (TestedWords.isCorrect(2)) {
            word3Label.setTextFill(Color.LIME);
        }
        if (TestedWords.isCorrect(3)) {
            word4Label.setTextFill(Color.LIME);
        }
        if (TestedWords.isCorrect(4)) {
            word5Label.setTextFill(Color.LIME);
        }
    
    }
    

    /**
     * When 'Play Again' button is pressed, play the quiz again, either practise or games module
     */
    @FXML
    private void playAgain() {
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
    private void backToMainMenu() {
        SceneManager.goToMainMenu();
    }
    

}
