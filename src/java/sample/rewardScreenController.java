package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class rewardScreenController implements Initializable {

    @FXML
    private VBox rootPane;
    @FXML
    private Label userScoreLabel, messageLabel, promptLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Button playAgainBtn, mainMenuBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF 100");
    }
    
    @FXML
    private void newQuiz(ActionEvent event) throws IOException {
    	SceneController.goToQuiz();
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        SceneController.goToMainMenu();
    }
    

}
