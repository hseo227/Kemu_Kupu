package controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import spellingQuiz.Module;
import spellingQuizUtil.FestivalSpeech;
import spellingQuizUtil.Result;

abstract public class ModuleBaseController implements Initializable {
    protected Module quiz;

    private final PauseTransition pause = new PauseTransition(Duration.seconds(2));

    @FXML
    private Label mainLabel, promptLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Button playbackBtn;
    @FXML
    private Slider speechSpeed;
    @FXML
    private ToggleButton togSpdSlider;
    @FXML
    private VBox inputVBox;



    abstract protected void startQuiz();
    abstract protected void onEnter();
    abstract protected void checkSpelling();

    // this method set up the Server thread for quiz.newQuestion and then run it
    protected void newQuestion() {

        disablePlaybackBtnTemp();

        inputField.clear();
        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());  // set up speech speed

        // if the quiz is not finished, continue the game (return true), otherwise false
        if (quiz.newQuestion()) {
            updateLabels("#FFF");  // update the labels with colour white

        } else {
            // if the game is finished, go to reward screen
            rewardScreen();
        }
    }


    @FXML
    protected void speakAgain() {
        disablePlaybackBtnTemp();

        // set up speech speed and then speak
        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());
        quiz.speakWordAgain();
    }

    protected void disablePlaybackBtnTemp() {
        // disable playback button for 2 second, avoid the user spam it
        playbackBtn.setDisable(true);
        pause.setOnFinished(e -> playbackBtn.setDisable(false));
        pause.play();
    }

    @FXML
    protected void showHideSpeedSlider() {
        speechSpeed.setVisible(togSpdSlider.isSelected());
    }

    @FXML
    protected void skipWord(ActionEvent event) {
        quiz.setResult(Result.SKIPPED);
        checkSpelling();
    }

    // this method is to pause before each new question, also while pausing it disables the quiz related utilities
    protected void pauseBetweenEachQ() {
        inputVBox.setDisable(true);

        pause.setOnFinished(e -> {
            inputVBox.setDisable(false);

            newQuestion();
        });
        pause.play();
    }

    // this method is to update the labels with specific colour
    protected void updateLabels(String colour) {
        mainLabel.setStyle("-fx-text-fill: " + colour + ";");  // change to green text
        promptLabel.setStyle("-fx-text-fill: " + colour + ";");  // change to green text

        mainLabel.setText(quiz.getMainLabelText());
        promptLabel.setText(quiz.getPromptLabelText());
    }

    // this method is called when the quiz is finished
    protected void rewardScreen() {
        SceneManager.goToRewardScreen();
    }

    @FXML
    protected void backToMainMenu() {
        // kill all the festival, so no festival in playing when it is in main menu screen
        FestivalSpeech.shutDownAllFestival();
        SceneManager.goToMainMenu();
    }

    @FXML
    protected void checkMacronShortCut() {
        String word = inputField.getText();
        String change = "";

        // check each dash in the word
        for (int indexOfDash = word.indexOf("-"); indexOfDash >= 0; indexOfDash = word.indexOf("-", indexOfDash + 1)) {
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
    protected void addMacronsCharacter(ActionEvent event) {
        String macronsCharacter = ((Button) event.getSource()).getText();
        inputField.appendText(macronsCharacter);
    }


}
