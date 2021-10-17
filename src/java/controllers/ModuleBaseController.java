package controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;
import spellingQuiz.Module;
import spellingQuizUtil.FestivalSpeech;
import spellingQuizUtil.Result;
import spellingQuizUtil.Score;

abstract public class ModuleBaseController implements Initializable {
    // 2 seconds pause
    private final PauseTransition pause = new PauseTransition(Duration.seconds(2));

    protected Module quiz;
    protected boolean inhibitSubmitAction = false;

    @FXML
    private Label mainLabel, promptLabel, userScore;
    @FXML
    private TextField inputField;
    @FXML
    private Button playbackBtn, checkBtn, skipBtn;
    @FXML
    private Slider speechSpeed;
    @FXML
    private ToggleButton togSpdSlider;
    @FXML
    private VBox inputVBox;


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
        inputField.clear();
        disableButtonsWhenSpeaking();
        FestivalSpeech.setSpeechSpeed((int) speechSpeed.getValue());  // set up speech speed

        // if the quiz is not finished, continue the game (return true), otherwise false
        if (quiz.newQuestion()) {
            updateLabels("#FFF");  // update the labels with colour white

        } else {
            // if the game is finished, go to reward screen
            rewardScreen();
        }
    }

    /**
     * Speak the word again, only the maori word
     */
    @FXML
    protected void speakAgain() {
        disableButtonsWhenSpeaking();

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
     * Show/Hide the speed slider by clicking the toggle button
     */
    @FXML
    protected void showHideSpeedSlider() {
        speechSpeed.setVisible(togSpdSlider.isSelected());
    }

    /**
     * 2 seconds pause before each new question and while pausing it disables the quiz related utilities/buttons
     * after the pause, call method newQuestion()
     */
    protected void pauseBetweenEachQ() {
        inputVBox.setDisable(true);

        pause.setOnFinished(e -> {
            inputVBox.setDisable(false);

            newQuestion();
        });
        pause.play();
    }

    /**
     * Disable some buttons for 2 second when the festival starts running
     * It is to avoid the user spam those buttons
     */
    protected void disableButtonsWhenSpeaking() {
        playbackBtn.setDisable(true);
        checkBtn.setDisable(true);
        skipBtn.setDisable(true);
        inhibitSubmitAction = true;

        pause.setOnFinished(e -> {
            playbackBtn.setDisable(false);
            checkBtn.setDisable(false);
            skipBtn.setDisable(false);
            inhibitSubmitAction = false;
        });
        pause.play();
    }

    /**
     * Update the labels with given colour
     * Also change the size of the text so the label fits the text perfectly
     * @param colour Colour of the label text
     */
    protected void updateLabels(String colour) {
        String mainLabelText = quiz.getMainLabelText();
        String promptLabelText = quiz.getPromptLabelText();
        int mainLabelSize, promptLabelSize;

        // change the size of the text according to the length of the text
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

        // change the text
        mainLabel.setText(mainLabelText);
        promptLabel.setText(promptLabelText);
    }

    /**
     * Update the score in the score label
     * set userScore label to the current score
     */
    protected void updateScore() {
        userScore.setText(Score.getScore() + "");
    }

    /**
     * Go to Reward Screen when the quiz is finished
     */
    protected void rewardScreen() {
        SceneManager.goToRewardScreen();
    }

    /**
     * When 'Main menu' button is pressed, go back to main menu
     * Also, if the festival is still playing, it will terminate all of them
     * So no festival in playing when it is in main menu screen
     */
    @FXML
    protected void backToMainMenu() {
        FestivalSpeech.shutDownAllFestival();
        SceneManager.goToMainMenu();
    }

    /**
     * The method is called everytime the user hit a key in the text field
     * It is to check macron character short cut
     * Shortcut: a vowel followed by '-'
     * E.g. "a-" -> "ā"
     */
    @FXML
    protected void checkMacronShortCut() {
        String word = inputField.getText();
        String change;

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

    /**
     * Allow the user to click the buttons to add macron character instead of short cut
     * @param event This parameter is used to get the macron character that the user want to add in
     */
    @FXML
    protected void addMacronsCharacter(ActionEvent event) {
        String macronsCharacter = ((Button) event.getSource()).getText();
        inputField.appendText(macronsCharacter);
    }
}
