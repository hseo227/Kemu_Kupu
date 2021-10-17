package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import spellingQuiz.ModulePractise;
import spellingQuizUtil.FestivalSpeech;
import spellingQuizUtil.QuizState;
import spellingQuizUtil.Result;
import spellingQuizUtil.Score;
import spellingQuizUtil.Words;

import java.net.URL;
import java.util.ResourceBundle;

public class ModulePractiseController extends ModuleBaseController {

    @FXML
    private Label shortCutLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Button skipBtn;
    @FXML
    private Slider speechSpeed;
    @FXML
    private ToggleButton togSpdSlider;
    @FXML
    private ChoiceBox<Integer> numOfQCheckBox;
    @FXML
    private VBox startVBox, gameVBox;


    /**
     * Setting up the check box
     * Reset the score
     * Setting up and Format the speed slider
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // add check box from 1 to number of words in the words list, maximum is 10
        for (int i = 1; i <= Math.min(Words.getNumOfWordsInWordsList(), 10); i++) {
            numOfQCheckBox.getItems().add(i);
        }
        // set default number to 3
        numOfQCheckBox.setValue(3);


        // Set up for speed slider
        // hide the slider
        speechSpeed.setVisible(togSpdSlider.isSelected());

        // format the vertical slider
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
     * Start the quiz with number of question that user picked
     * And then update the display
     */
    @FXML
    protected void startQuiz() {

        // start a new game with specific number of questions
        quiz = new ModulePractise(numOfQCheckBox.getValue());

        // Display score
        updateScore();

        // otherwise, continue the game
        startVBox.setVisible(false);
        gameVBox.setVisible(true);
        shortCutLabel.setVisible(true);

        newQuestion();
    }

    /**
     * When the user press 'Enter' key or press the 'Check' button,
     * if the question is finished, go to next question
     * otherwise, check the spelling
     * But do not go to next question or check the spelling when it is speaking the word
     */
    @FXML
    protected void onEnter() {
        if (!inhibitSubmitAction) {
            if (quiz.quizStateEqualsTo(QuizState.READY)) {
                skipBtn.setDisable(false);
                newQuestion();
            } else {
                checkSpelling();
            }
        }
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

                updateScore();

                pauseBetweenEachQ();

            // incorrect spelling (Failed) OR the word is skipped
            } else {
                colour = "#FF2715";  // set text colour to red
                skipBtn.setDisable(true);  // cannot press 'skip' when showing the answer
            }

        // incorrect spelling (1st attempt)
        } else {
            colour = "#FF2715";  // set text colour to red
            inputField.clear();
            disableButtonsWhenSpeaking();
        }

        updateLabels(colour);  // update the labels with corresponding colour
    }

}
