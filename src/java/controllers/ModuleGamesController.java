package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import spellingQuiz.ModuleGames;
import spellingQuizUtil.FestivalSpeech;
import spellingQuizUtil.QuizState;
import spellingQuizUtil.Result;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class stores only the display (GUI) functionalities of Games Module
 */
public class ModuleGamesController extends ModuleBaseController {

    @FXML
    private TextField inputField;
    @FXML
    private Slider speechSpeed;


    /**
     * Setting up the fxml beforehand
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        settingUp();
    }

    /**
     * Start the quiz and also update the display
     */
    @FXML
    protected void startQuiz() {
        // start a new game
        quiz = new ModuleGames();
        updateStartDisplay();
    }

    /**
     * When the user press 'Enter' key or press the 'Check' button, check the spelling
     * But do not check the spelling when it is speaking the word
     */
    @FXML
    protected void onEnter() {
        if (!inhibitSubmitAction) {
            checkSpelling();
        }
    }

    /**
     * Check the spelling, and then update the display
     * Pause 2 seconds before going to the next question
     */
    protected void checkSpelling() {
        String textColour;

        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());  // set up speech speed
        quiz.setUserInput(inputField.getText());  // get user input/spelling
        quiz.checkSpelling();

        // update the display

        // the quiz is done, so either Mastered, Faulted or Failed
        if (quiz.quizStateEqualsTo(QuizState.READY)) {

            // correct spelling (Mastered and Faulted)
            if (quiz.resultEqualsTo(Result.MASTERED) || quiz.resultEqualsTo(Result.FAULTED)) {
                textColour = GREEN;
                updateScore();

            // incorrect spelling (Failed) OR the word is skipped
            } else {
                textColour = RED;
            }

            pauseBetweenEachQ();

        // incorrect spelling (1st attempt)
        } else {
            textColour = RED;
            inputField.clear();
        }

        updateLabels(textColour);  // update the labels with corresponding text colour
    }

}
