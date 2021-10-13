package controllers;


import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import spellingQuizUtil.FestivalSpeech;
import spellingQuizUtil.QuizState;
import spellingQuizUtil.Result;
import spellingQuizUtil.Score;
import spellingQuizUtil.Words;
import spellingQuiz.ModulePractise;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModulePractiseController implements Initializable {

    private ModulePractise quiz;

    private final PauseTransition pause = new PauseTransition(Duration.seconds(2));


    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainLabel, promptLabel, userScore, numOfLettersLabel, shortCutLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Button backBtn, macronsA, macronsE, macronsI, macronsO, macronsU, skipBtn, checkBtn, playbackBtn;
    @FXML
    private Slider speechSpeed;
    @FXML
    private ToggleButton togSpdSlider;
    @FXML
    private ChoiceBox<Integer> numOfQCheckBox;
    @FXML
    private HBox macronsBtnsHBox;
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
    private void startQuiz(ActionEvent event) {

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
    private void onEnter(ActionEvent event) {
        // when the user press enter key or press the 'check' button...
        if (quiz.quizStateEqualsTo(QuizState.ready)) {
            skipBtn.setDisable(false);
            newQuestion();
        } else {
            checkSpelling();
        }
    }

    // this method set up the Server thread for quiz.newQuestion and then run it
    private void newQuestion() {

        // disable playback button for 2 second, avoid the user spam it
        playbackBtn.setDisable(true);
        pause.setOnFinished(e -> playbackBtn.setDisable(false));
        pause.play();

        inputField.clear();
        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());  // set up speech speed

        // if the quiz is not finished, continue the game (return true), otherwise false
        if (quiz.newQuestion()) {
            updateLabels("#FFF");  // update the labels with colour white
            numOfLettersLabel.setText("Number of letters of the word is " + quiz.getNumOfLettersOfWord());

        } else {
            // if the game is finished, go to reward screen
            rewardScreen();
        }
    }

    // this method set up the Server thread for quiz.checkSpelling and then run it
    private void checkSpelling() {
        String colour = "#FFF";  // set default text colour to white

        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());  // set up speech speed
        quiz.setUserInput(inputField.getText());  // get user input/spelling
        quiz.checkSpelling();

        // update the display

        // the quiz is done, so either Mastered, Faulted or Failed
        if (quiz.quizStateEqualsTo(QuizState.ready)) {

            // correct spelling (Mastered and Faulted)
            if (quiz.resultEqualsTo(Result.mastered) || quiz.resultEqualsTo(Result.faulted)) {
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
        }

        updateLabels(colour);  // update the labels with corresponding colour
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        SceneController.goToMainMenu();
    }

    @FXML
    private void speakAgain() {
        // disable playback button for 2 second, avoid the user spam it
        playbackBtn.setDisable(true);
        pause.setOnFinished(e -> playbackBtn.setDisable(false));
        pause.play();

        // set up speech speed and then speak
        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());
        quiz.speakWordAgain();
    }

    @FXML
    private void showHideSpeedSlider() {
        speechSpeed.setVisible(togSpdSlider.isSelected());
    }

    @FXML
    private void checkMacronShortCut() {
        String word = inputField.getText();
        String change = "";

        // check each dash in the word
        for (int indexOfDash = word.indexOf("-"); indexOfDash >= 0; indexOfDash = word.indexOf("-", indexOfDash + 1)){
            // if '-' is at first index, ignore it
            if (indexOfDash != 0) {
                // check if the character before '-' is a vowel
                switch (word.charAt(indexOfDash - 1)) {
                    case 'a':
                        change = "ā";
                        break;
                    case 'e':
                        change = "ē";
                        break;
                    case 'i':
                        change = "ī";
                        break;
                    case 'o':
                        change = "ō";
                        break;
                    case 'u':
                        change = "ū";
                        break;
                    default:
                        continue;
                }

                // replace the vowel and '-' with the macron character
                inputField.replaceText(indexOfDash - 1, indexOfDash + 1, change);
            }
        }
    }

    @FXML
    private void addMacronsCharacter(ActionEvent event) {
        String macronsCharacter = ((Button)event.getSource()).getText();
        inputField.appendText(macronsCharacter);
    }
    
    @FXML
    private void skipWord(ActionEvent event) {
    	quiz.setResult(Result.skipped);
        checkSpelling();
    }

    // this method is called when the quiz is finished
    private void rewardScreen() {
    	try {
        	SceneController.goToRewardScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    // this method is to pause before each new question, also while pausing it disables the quiz related utilities
    private void pauseBetweenEachQ() {
        inputVBox.setDisable(true);

        pause.setOnFinished(e -> {
            inputVBox.setDisable(false);

            newQuestion();
        });
        pause.play();
    }

    // this method is to update the labels with specific colour
    private void updateLabels(String colour) {
        mainLabel.setStyle("-fx-text-fill: " + colour + ";");  // change to green text
        promptLabel.setStyle("-fx-text-fill: " + colour + ";");  // change to green text

        mainLabel.setText(quiz.getMainLabelText());
        promptLabel.setText(quiz.getPromptLabelText());
    }

}
