package sample;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class quizController implements Initializable {

    private SpellingQuiz quiz;
  
    private final PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
    
    private boolean firstAttempt = true;


    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainLabel, promptLabel, userScore;
    @FXML
    private TextField inputField;
    @FXML
    private Button startBtn, backBtn, macronsA, macronsE, macronsI, macronsO, macronsU, skipBtn, checkBtn;
    @FXML
    private ImageView playbackImg;
    @FXML
    private Slider speechSpeed;
    @FXML
    private ToggleButton togSpdSlider;
    @FXML
    private HBox macronsBtnsHBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	
    	// reset the score
    	Score.reset();
    	
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
                    return "fast";
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
    	
    	// Display score
    	userScore.setText("SCORE : " + Score.score);
        userScore.setVisible(true);
    	
        // start a new game, either new spelling quiz or review mistakes
        quiz = new SpellingQuiz();

        // otherwise, continue the game
        startBtn.setVisible(false);
        backBtn.setVisible(true);
        playbackImg.setVisible(true);
        togSpdSlider.setVisible(true);
        macronsBtnsHBox.setVisible(true);
        skipBtn.setVisible(true);
        inputField.setVisible(true);
        checkBtn.setVisible(true);

        // The text when the mouse hover on the playback image
        Tooltip tooltip = new Tooltip("Click to playback");
        Tooltip.install(playbackImg, tooltip);

        newQuestion();
    }

    @FXML
    private void onEnter(ActionEvent event) {
        // when the user press enter key or press the 'check' button, check spelling
        checkSpelling();
        
    }

    // this method set up the Server thread for quiz.newQuestion and then run it
    private void newQuestion() {

        mainLabel.setStyle("-fx-text-fill: #000;");  // change back to black text
        promptLabel.setStyle("-fx-text-fill: #000;");  // change back to black text
        inputField.clear();
    	
        quiz.setSpeechSpeed((int) speechSpeed.getValue());

        mainLabel.textProperty().bind(quiz.titleProperty());
        promptLabel.textProperty().bind(quiz.messageProperty());

        quiz.setOnSucceeded(e -> {
            mainLabel.textProperty().unbind();
            promptLabel.textProperty().unbind();

            // if the game is finished, some buttons will appear
            // while other utilities are disappear
            if (quiz.quizStateEqualsTo(QuizState.finished)) {
            	rewardScreen();
            }

            quiz.reset();
        });

        quiz.start();
    }

    // this method set up the Server thread for quiz.checkSpelling and then run it
    private void checkSpelling() {

        quiz.setSpeechSpeed((int) speechSpeed.getValue());
        quiz.setUserInput(inputField.getText());

        mainLabel.textProperty().bind(quiz.titleProperty());
        promptLabel.textProperty().bind(quiz.messageProperty());

        ChangeListener<String> stateListener = (obs, oldValue, newValue) -> {
            // the quiz is done, so either Mastered, Faulted or Failed
            if (quiz.quizStateEqualsTo(QuizState.ready)) {

                // correct spelling (Mastered and Faulted)
                if (quiz.resultEqualsTo(Result.mastered) || quiz.resultEqualsTo(Result.faulted)) {
                    mainLabel.setStyle("-fx-text-fill: #00A804;");  // change to green text
                    promptLabel.setStyle("-fx-text-fill: #00A804;");  // change to green text
                    
                    if (firstAttempt) {
                    	// Increase the score
                        Score.increase20();
                    } else {
                    	// Increase the score
                        Score.increase10();
                        firstAttempt = true;
                    }

                // incorrect spelling (Failed) OR the word is skipped
                } else {
                    mainLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
                    promptLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
                }

                pauseBetweenEachQ();

            // incorrect spelling (1st attempt)
            } else {
                mainLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
                promptLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
                inputField.clear();
                firstAttempt = false;
            }
        };

        quiz.titleProperty().addListener(stateListener);
        quiz.setOnSucceeded(e -> {
            quiz.titleProperty().removeListener(stateListener);
            mainLabel.textProperty().unbind();
            promptLabel.textProperty().unbind();

            quiz.reset();
            // set userScore label to the current score
            userScore.setText("SCORE : " + Score.score);
        });

        quiz.start();
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        SceneController.goToMainMenu();
    }

    @FXML
    private void speakAgain() {
        quiz.setSpeechSpeed((int) speechSpeed.getValue());
        quiz.speakWordAgain();
    }

    @FXML
    private void showHideSpeedSlider() {
        speechSpeed.setVisible(togSpdSlider.isSelected());
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
    
    @FXML
    private void rewardScreen() {
    	try {
        	SceneController.goToRewardScreen();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    // this method is to pause before each new question, also while pausing it disables the quiz related utilities
    private void pauseBetweenEachQ() {
        skipBtn.setDisable(true);
        checkBtn.setDisable(true);
        macronsBtnsHBox.setDisable(true);
        inputField.setDisable(true);
        playbackImg.setDisable(true);

        pause.setOnFinished(e -> {
            skipBtn.setDisable(false);
            checkBtn.setDisable(false);
            macronsBtnsHBox.setDisable(false);
            inputField.setDisable(false);
            playbackImg.setDisable(false);

            newQuestion();
        });
        pause.play();
    }

}
