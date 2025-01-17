package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.StringConverter;
import controllers.commonControllers.CommonControllers;
import spellingQuiz.Module;
import spellingQuizUtil.*;

import static spellingQuizUtil.FestivalSpeech.numOfRunningFestival;

/**
 * This class contains all the common display (GUI) functionalities of both Games and Practise Module
 */
abstract public class ModuleBaseController extends CommonControllers implements Initializable {
    protected final static String WHITE = "#FFF";
    protected final static String GREEN = "#00A804";
    protected final static String RED = "#C8220D";

    // 2 seconds pause
    private final PauseTransition pause = new PauseTransition(Duration.seconds(2));

    protected Module quiz;
    protected boolean inhibitSubmitAction = false;
    protected Timeline timeline;

    @FXML
    private Label mainLabel, promptLabel, userScoreLabel, timeLabel, shortCutLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Button playbackBtn, checkBtn, skipBtn;
    @FXML
    private Slider speechSpeed;
    @FXML
    private VBox startVBox, gameVBox, inputVBox;


    /**
     * Call this method when initialize the fxml and set up necessary stuff
     *      Reset the timer
     *      Set up the timeline
     *      Set numOfRunningFestival listener
     *      Format the speed slider
     */
    protected void settingUp() {
        Timer.reset();

        // Set up the timeline - update the time label constantly
        timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            timeLabel.setText("Time taken:  " + Timer.getTime());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);  // no time limit, so run forever
        timeline.play();

        // Set listener for numOfRunningFestival
        // When there are running festivals, disable buttons (playback, check and skip buttons)
        numOfRunningFestival = new SimpleIntegerProperty(0);
        numOfRunningFestival.addListener((observableValue, oldV, newValue) -> {

            // if there are no running festival, then false
            boolean isSpeaking = ((int) newValue != 0);
            // if the input components are already disable, then don't need to change the opacity, so false
            boolean opacityChange = !inputVBox.isDisable();

            disableItems(isSpeaking, opacityChange, playbackBtn, checkBtn, skipBtn);
            inhibitSubmitAction = isSpeaking;

            // only disable 'skip' if the user got the word wrong (Failed and Skipped) in Practise module
            if (isWrongInPractiseModule()) {
                disableItems(true, opacityChange, skipBtn);
            }

            // if it started a new question after the festival is finished, then starts the timer
            if (!isSpeaking && quiz.quizStateEqualsTo(QuizState.JUST_STARTED)) {
                Timer.start();
            }
        });

        // format the speed slider
        speechSpeed.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double n) {
                if (n == speechSpeed.getMin()) {  // slowest speed
                    return "Slow";
                } else if (n == 100) {  // normal speed
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
     * Update the start display and then start a new question
     */
    protected void updateStartDisplay() {
        // Update the display
        updateScore();
        startVBox.setVisible(false);
        gameVBox.setVisible(true);
        timeLabel.setVisible(true);
        shortCutLabel.setVisible(true);

        newQuestion();
    }

    /**
     * Different module start the quiz differently.
     *      Games module always starts with 5 questions;
     *      Practise module starts with the amount of what user has picked
     */
    abstract protected void startQuiz();

    /**
     * Different module behaves differently when the user pressed 'Enter' or clicked 'Check' button.
     *      Games module checks the spelling only;
     *      Practise module either checks the spelling or go to next question
     */
    abstract protected void onEnter();

    /**
     * This method is called to check the spelling and then update the display.
     * Different module behaves differently after checking the spelling.
     *      Games module will always go to next question automatically;
     *      Practise module either wait for the user to input or go to next question automatically
     */
    abstract protected void checkSpelling();

    /**
     * This method is called to generate new question and then update the display.
     * If the quiz is finished, it will take the user to the Reward Screen.
     */
    protected void newQuestion() {
        Timer.reset();
        inputField.clear();
        inputField.requestFocus();
        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());  // set up speech speed

        // if the quiz is not finished, continue the game (return true), otherwise false
        if (quiz.newQuestion()) {
            updateLabels(WHITE);  // update the labels with colour white

        } else {
            // if the game is finished, go to reward screen
            rewardScreen();
        }
    }

    /**
     * When playback button is pressed, speak the word again, only the maori word
     */
    @FXML
    protected void speakAgain() {
        inputField.requestFocus();
        inputField.positionCaret(inputField.getText().length());

        // set up speech speed and then speak
        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());
        quiz.speakWordAgain();
    }

    /**
     * Skip the word and then update the display by calling the method checkSpelling(),
     * because skipped word is treated as incorrect twice
     */
    @FXML
    protected void skipWord() {
        quiz.setResult(Result.SKIPPED);
        checkSpelling();
    }

    /**
     * Helper method that tells whether the user gets it wrong in Practise module
     *
     * @return True if wrong in Practise module, otherwise false
     */
    private boolean isWrongInPractiseModule() {
        return Module.moduleTypeEqualsTo(ModuleType.PRACTISE)
                && (quiz.resultEqualsTo(Result.FAILED) || quiz.resultEqualsTo(Result.SKIPPED));
    }

    /**
     * Show/Hide the speed slider by clicking the button
     */
    @FXML
    protected void showHideSpeedSlider() {
        speechSpeed.setVisible(!speechSpeed.isVisible());
    }

    /**
     * 2 seconds pause before each new question and
     * while pausing it disables the utilities/buttons that related to the quiz
     * after the pause, it automatically goes to the next question
     */
    protected void pauseBetweenEachQ() {
        disableItems(true, inputVBox);

        pause.setOnFinished(e -> {
            disableItems(false, inputVBox);

            newQuestion();
        });
        pause.play();
    }

    /**
     * Update the labels with given colour
     * Also change the size of the text so the label fits the text perfectly
     *
     * @param colour Colour of the label text
     */
    protected void updateLabels(String colour) {
        String mainLabelText = quiz.getMainLabelText();
        String promptLabelText = quiz.getPromptLabelText();
        int mainLabelSize, promptLabelSize;

        // set the size of the text according to the length of the text
        // longer the word length, smaller the size of the text in the labels
        // main label text
        if (mainLabelText.length() < 30) {
            mainLabelSize = 50;
        } else if (mainLabelText.length() < 45) {
            mainLabelSize = 40;
        } else {
            mainLabelSize = 25;
        }

        // prompt label text
        if (promptLabelText.length() < 80) {
            promptLabelSize = 25;
        } else {
            promptLabelSize = 20;
        }

        // change the size of the text
        mainLabel.setFont(new Font("Heiti TC Medium", mainLabelSize));
        promptLabel.setFont(new Font("Heiti TC Medium", promptLabelSize));

        // change the colour of the text
        mainLabel.setStyle("-fx-text-fill: " + colour + ";");
        promptLabel.setStyle("-fx-text-fill: " + colour + ";");

        // update the text
        mainLabel.setText(mainLabelText);
        promptLabel.setText(promptLabelText);
    }

    /**
     * Update the score in the score label
     * set userScore label to the current score
     */
    protected void updateScore() {
        userScoreLabel.setText(Score.getScore() + "");
    }

    /**
     * Go to Reward Screen when the quiz is finished
     */
    protected void rewardScreen() {
        timeline.stop();
        SceneManager.goToRewardScreen();
    }

    /**
     * When 'Main menu' button is pressed, go back to main menu
     * Also, if the festival is still playing, it will terminate all of them
     * So no festival in playing when it is in main menu screen
     */
    @FXML
    protected void backToMainMenu() {
        timeline.stop();
        FestivalSpeech.shutDownAllFestival();
        SceneManager.goToMainMenu();
    }

    /**
     * The method is called everytime the user hit a key in the text field
     * It is to check macron character short cut
     * Shortcut: a vowel followed by '='
     * E.g. "a=" -> "ā"
     */
    @FXML
    protected void checkMacronShortCut() {
        String word = inputField.getText();
        String change;

        // check each dash in the word
        for (int indexOfEqual = word.indexOf("="); indexOfEqual >= 0; indexOfEqual = word.indexOf("=", indexOfEqual + 1)) {
            // if '=' is at first index, ignore it
            if (indexOfEqual != 0) {
                // check if the character before '=' is a vowel
                switch (word.charAt(indexOfEqual - 1)) {
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

                // replace the vowel and '=' with the macron character
                inputField.replaceText(indexOfEqual - 1, indexOfEqual + 1, change);
            }
        }
    }

    /**
     * Allow the user to click the buttons to add macron character instead of short cut
     *
     * @param event This parameter is used to get the macron character that the user want to add in
     */
    @FXML
    protected void addMacronsCharacter(ActionEvent event) {
        String macronsCharacter = ((Button) event.getSource()).getText();
        inputField.appendText(macronsCharacter);
        inputField.requestFocus();
        inputField.positionCaret(inputField.getText().length());
    }
}
