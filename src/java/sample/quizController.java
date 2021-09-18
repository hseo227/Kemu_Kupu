package sample;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Button startButton, backButton;


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
        inputField.setVisible(true);
        startButton.setVisible(false);
        newQuestion();
    }

    @FXML
    private void onEnter(ActionEvent event) {
        // if the game is finished, ask the user to leave
        if (quiz.quizStateEqualsTo(QuizState.finished)) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Finished the quizzes");
            a.setHeaderText("You finished the quizzes! Go back to main menu!");
            a.showAndWait();

        // or if the quiz is ready for next question, then generate the next question
        } else if (quiz.quizStateEqualsTo(QuizState.ready)) {
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

            // if the game is finished, a button will appear, which lead the user back to the main menu
            if (quiz.quizStateEqualsTo(QuizState.finished)) {
                backButton.setVisible(true);
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
