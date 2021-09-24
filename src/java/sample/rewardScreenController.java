package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class rewardScreenController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label userScoreLabel, messageLabel, promptLabel;
    @FXML
    private TextField inputField;
    @FXML
    private Button playAgainBtn, mainMenuBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Integer.toString(Score.score) + " OUT OF 100");
        userScoreLabel.setVisible(true);
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
