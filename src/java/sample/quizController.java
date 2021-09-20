package sample;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private Button startBtn, finishBackBtn, homeBtn, playAgainBtn;
    @FXML
    private ImageView playbackImg;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (SpellingQuiz.modeEqualsTo(Mode.newSpellingQuiz)) {
            mainLabel.setText("New Spelling Quiz");
        } else {
            mainLabel.setText("Review Mistakes");
        }
    }

    @FXML
    private void startQuiz(ActionEvent event) throws IOException {
        // start a new game, either new spelling quiz or review mistakes
        quiz = new SpellingQuiz();

        // if no failed words, display a message and then back main menu
        if (quiz.quizStateEqualsTo(QuizState.noFailed)) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Too smart!");
            a.setHeaderText("You have 0 failed word!");
            a.showAndWait();

            SceneController.goToMainMenu();
            return;
        }

        // otherwise, continue the game
        finishBackBtn.setVisible(false);
        playAgainBtn.setVisible(false);
        homeBtn.setVisible(true);
        inputField.setVisible(true);
        playbackImg.setVisible(true);
        startBtn.setVisible(false);

        // The text when the mouse hover on the playback image
        Tooltip tooltip = new Tooltip("Click to playback");
        Tooltip.install(playbackImg, tooltip);

        newQuestion();
    }

    @FXML
    private void onEnter(ActionEvent event) {
        // if the quiz is ready for next question, then generate the next question
        if (quiz.quizStateEqualsTo(QuizState.ready)) {
            rootPane.setStyle("-fx-background-color: #FFFFFF;");  // change to white background
            inputField.clear();

            newQuestion();

        // otherwise, check the spelling
        } else {
            checkSpelling();
        }
    }

    // this method set up the Server thread for quiz.newQuestion and then run it
    private void newQuestion() {
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
                inputField.setVisible(false);
                playbackImg.setVisible(false);
            }

            quiz.reset();
        });

        quiz.start();
    }

    // this method set up the Server thread for quiz.checkSpelling and then run it
    private void checkSpelling() {
        quiz.setUserInput(inputField.getText());
        mainLabel.textProperty().bind(quiz.titleProperty());
        promptLabel.textProperty().bind(quiz.messageProperty());

        ChangeListener<String> stateListener = (obs, oldValue, newValue) -> {
            // the quiz is done, so either Mastered, Faulted or Failed
            if (quiz.quizStateEqualsTo(QuizState.ready)) {
                // correct spelling (Mastered and Faulted)
                if (quiz.resultEqualsTo(Result.mastered) || quiz.resultEqualsTo(Result.faulted)) {
                    rootPane.setStyle("-fx-background-color: #1AFF1A;");  // change to green background

                // incorrect spelling (Failed)
                } else {
                    rootPane.setStyle("-fx-background-color: #FF2715;");  // change to red background
                }

            // incorrect spelling (1st attempt)
            } else {
                rootPane.setStyle("-fx-background-color: #FF2715;");  // change to red background
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
        quiz.speakAgain();
    }

}
