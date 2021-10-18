package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
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
    private Label shortCutLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Slider speechSpeed;
    @FXML
    private VBox startVBox, gameVBox;


    /**
     * Format the speed slider
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // format the speed slider
        speechSpeed.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double n) {
                if (n == speechSpeed.getMin()) {  // slowest speed
                    return "Slow";
                } else if (n == (speechSpeed.getMin() + speechSpeed.getMax()) / 2) {  // normal speed, in the middle
                    return "Default";
                } else if (n == speechSpeed.getMax()) {  // fastest speed
                    return "Fast";
                }

                return null;
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
    }

    /**
     * Start the quiz and then update the display
     */
    @FXML
    protected void startQuiz() {

        // start a new game
        quiz = new ModuleGames();

        // Update the display
        updateScore();
        startVBox.setVisible(false);
        gameVBox.setVisible(true);
        shortCutLabel.setVisible(true);

        newQuestion();
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
            disableButtonsWhenSpeaking();
        }

        updateLabels(textColour);  // update the labels with corresponding text colour
    }

}
