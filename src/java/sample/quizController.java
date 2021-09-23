package sample;

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
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class quizController implements Initializable {

    private SpellingQuiz quiz;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainLabel, promptLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Button startBtn, finishBackBtn, homeBtn, playAgainBtn, macronsA, macronsE, macronsI, macronsO, macronsU, skipBtn, checkBtn;
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
        // start a new game, either new spelling quiz or review mistakes
        quiz = new SpellingQuiz();

        // otherwise, continue the game
        finishBackBtn.setVisible(false);
        playAgainBtn.setVisible(false);
        homeBtn.setVisible(true);
        skipBtn.setVisible(true);
        playbackImg.setVisible(true);
        togSpdSlider.setVisible(true);
        macronsBtnsHBox.setVisible(true);
        startBtn.setVisible(false);
        inputField.setVisible(true);
        checkBtn.setVisible(true);

        // The text when the mouse hover on the playback image
        Tooltip tooltip = new Tooltip("Click to playback");
        Tooltip.install(playbackImg, tooltip);

        newQuestion();
    }

    @FXML
    private void onEnter(ActionEvent event) {
        // if the quiz is ready for next question, then generate the next question
        
    	if (quiz.quizStateEqualsTo(QuizState.ready)) {
        	mainLabel.setStyle("-fx-text-fill: #000;");  // change back to white text
            promptLabel.setStyle("-fx-text-fill: #000;");  // change back to white text
            inputField.clear();
            newQuestion();

        // otherwise, check the spelling
        } else {
            checkSpelling();
            
            if (quiz.resultEqualsTo(Result.faulted)) {
                skipBtn.setDisable(true);
                checkBtn.setDisable(true);
            }
        }
    }

    // this method set up the Server thread for quiz.newQuestion and then run it
    private void newQuestion() {
    	
    	if (quiz.quizStateEqualsTo(QuizState.ready)) {
            skipBtn.setDisable(false);
            checkBtn.setDisable(false);
    	}
    	
        quiz.setSpeechSpeed((int) speechSpeed.getValue());

        mainLabel.textProperty().bind(quiz.titleProperty());
        promptLabel.textProperty().bind(quiz.messageProperty());
        
        quiz.setOnSucceeded(e -> {
            mainLabel.textProperty().unbind();
            promptLabel.textProperty().unbind();
            
            // if the game is finished, some buttons will appear
            // while other utilities are disappear
            if (quiz.quizStateEqualsTo(QuizState.finished)) {
                finishBackBtn.setVisible(true);
                playAgainBtn.setVisible(true);
                homeBtn.setVisible(false);
                playbackImg.setVisible(false);
                togSpdSlider.setVisible(false);
                macronsBtnsHBox.setVisible(false);
                skipBtn.setVisible(false);
                inputField.setVisible(true);
                checkBtn.setVisible(true);

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

                // incorrect spelling (Failed)
                } else {
                    mainLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
                    promptLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
                }

            // incorrect spelling (1st attempt)
            } else {
                mainLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
                promptLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
                inputField.clear();
            }
        };

        quiz.titleProperty().addListener(stateListener);
        quiz.setOnSucceeded(e -> {
            quiz.titleProperty().removeListener(stateListener);
            mainLabel.textProperty().unbind();
            promptLabel.textProperty().unbind();

            quiz.reset();
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
    	mainLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
        promptLabel.setStyle("-fx-text-fill: #FF2715;");  // change to red text
        skipBtn.setDisable(true);
        checkBtn.setDisable(true);
        newQuestion();
    }

}
