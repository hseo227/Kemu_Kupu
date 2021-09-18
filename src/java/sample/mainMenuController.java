package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class mainMenuController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainMenuLabel;
    @FXML
    private Button newSQ, reviewM, viewS, clearS, quit;


    // load New Spelling Quiz
    @FXML
    private void loadNew(ActionEvent event) throws IOException {
        SpellingQuiz.setMode(Mode.newSpellingQuiz);
        SceneController.goToTopicList();
    }

    // load Review Mistakes
    @FXML
    private void loadReview(ActionEvent event) throws IOException {
        SpellingQuiz.setMode(Mode.reviewMistakes);
        SceneController.goToQuiz();
    }

    // load View Statistics
    @FXML
    private void viewStats(ActionEvent event) throws IOException {
        SceneController.goToStatistics();
    }

    // Clear Statistics
    @FXML
    private void clearStats(ActionEvent event) {
        try {
            // ask the user for confirmation, if the user press ok then clear, otherwise do nothing
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Clear Statistics");
            a.setHeaderText("Are you sure you want to clear the statistics?");
            a.showAndWait();
            if (a.getResult() != ButtonType.OK) {
                return;
            }

            // get all the file locations
            HashMap<String, String> FILES = SpellingQuiz.getFILES();

            // clear the content for each file, except the word list file
            PrintWriter writeFile = null;

            for (String fileName : FILES.values()) {
                if (!fileName.equals(FILES.get("wordList_file"))) {
                    writeFile = new PrintWriter(new FileWriter(fileName));
                    writeFile.print("");
                }
            }

            writeFile.close();

            // display a message to let the user knows that the statistics is cleared
            a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Clear Statistics");
            a.setHeaderText("All statistics are clear!");
            a.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Quit the game
    @FXML
    private void quit(ActionEvent event) {
        Platform.exit();
    }

}
