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
    private Label mainLabel, promptLabel, userScore, shortCutLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Button skipBtn, playbackBtn;
    @FXML
    private Slider speechSpeed;
    @FXML
    private ToggleButton togSpdSlider;
    @FXML
    private ChoiceBox<Integer> numOfQCheckBox;
    @FXML
    private VBox inputVBox, startVBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // add check box from 1 to number of words in the words list, maximum is 10
        for (int i = 1; i <= Math.min(Words.getNumOfWordsInWordsList(), 10); i++) {
            numOfQCheckBox.getItems().add(i);
        }
        // set default number to 3
        numOfQCheckBox.setValue(3);

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

    @FXML
    protected void startQuiz() {

        // start a new game with specific number of questions
        quiz = new ModulePractise(numOfQCheckBox.getValue());

        // Display score
        userScore.setText("SCORE : " + Score.getScore());

        // otherwise, continue the game
        startVBox.setVisible(false);
        inputVBox.setVisible(true);
        shortCutLabel.setVisible(true);

        newQuestion();
    }

    @FXML
    protected void onEnter() {
        // when the user press enter key or press the 'check' button...
        if (quiz.quizStateEqualsTo(QuizState.READY)) {
            skipBtn.setDisable(false);
            newQuestion();
        } else {
            checkSpelling();
        }
    }

    // this method set up the Server thread for quiz.checkSpelling and then run it
    protected void checkSpelling() {
        String colour = "#FFF";  // set default text colour to white

        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());  // set up speech speed
        quiz.setUserInput(inputField.getText());  // get user input/spelling
        quiz.checkSpelling();

        // update the display

        // the quiz is done, so either Mastered, Faulted or Failed
        if (quiz.quizStateEqualsTo(QuizState.READY)) {

            // correct spelling (Mastered and Faulted)
            if (quiz.resultEqualsTo(Result.MASTERED) || quiz.resultEqualsTo(Result.FAULTED)) {
                colour = "#00A804";  // change text colour to green

                // set userScore label to the current score
                userScore.setText("SCORE : " + Score.getScore());

                pauseBetweenEachQ();

            // incorrect spelling (Failed) OR the word is skipped
            } else {
                colour = "#FF2715";  // change text colour to red
                skipBtn.setDisable(true);  // cannot press 'skip' when showing the answer
            }

        // incorrect spelling (1st attempt)
        } else {
            colour = "#FF2715";  // change text colour to red
            inputField.clear();
            disablePlaybackBtnTemp();
        }

        updateLabels(colour);  // update the labels with corresponding colour
    }

}
