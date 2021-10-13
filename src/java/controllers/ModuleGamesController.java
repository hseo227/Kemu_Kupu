package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import spellingQuiz.ModuleGames;
import spellingQuizUtil.FestivalSpeech;
import spellingQuizUtil.QuizState;
import spellingQuizUtil.Result;
import spellingQuizUtil.Score;

import java.net.URL;
import java.util.ResourceBundle;

public class ModuleGamesController extends ModuleBaseController {

    @FXML
    private Label userScore, shortCutLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Slider speechSpeed;
    @FXML
    private ToggleButton togSpdSlider;
    @FXML
    private VBox startVBox, inputVBox;


    /**
     * Reset the score
     * Format the speed slider
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // reset the score
        Score.reset();

        // Set up for speed slider
        // hide the slider
        speechSpeed.setVisible(togSpdSlider.isSelected());

        // format the vertical slider
        speechSpeed.setLabelFormatter(new StringConverter<Double>() {
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

        // Display score
        userScore.setText("SCORE : " + Score.getScore());

        // otherwise, continue the game
        startVBox.setVisible(false);
        inputVBox.setVisible(true);
        shortCutLabel.setVisible(true);

        newQuestion();
    }

    /**
     * When the user press 'Enter' key or press the 'Check' button, check the spelling
     */
    @FXML
    protected void onEnter() {
        checkSpelling();
    }

    /**
     * Check the spelling, and then update the display and pause 2 seconds if needed
     */
    protected void checkSpelling() {
        String colour;

        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());  // set up speech speed
        quiz.setUserInput(inputField.getText());  // get user input/spelling
        quiz.checkSpelling();

        // update the display

        // the quiz is done, so either Mastered, Faulted or Failed
        if (quiz.quizStateEqualsTo(QuizState.READY)) {

            // correct spelling (Mastered and Faulted)
            if (quiz.resultEqualsTo(Result.MASTERED) || quiz.resultEqualsTo(Result.FAULTED)) {
                colour = "#00A804";  // set text colour to green

                // set userScore label to the current score
                userScore.setText("SCORE : " + Score.getScore());

            // incorrect spelling (Failed) OR the word is skipped
            } else {
                colour = "#FF2715";  // set text colour to red
            }

            pauseBetweenEachQ();

        // incorrect spelling (1st attempt)
        } else {
            colour = "#FF2715";  // set text colour to red
            inputField.clear();
            disablePlaybackBtnTemp();
        }

        updateLabels(colour);  // update the labels with corresponding colour
    }

}
