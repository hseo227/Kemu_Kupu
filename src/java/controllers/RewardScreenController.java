package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;
import spellingQuizUtil.Result;
import spellingQuizUtil.Score;
import spellingQuizUtil.Statistics;
import tableUtil.TableStatistics;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RewardScreenController implements Initializable {
    private final ArrayList<TableStatistics> statisticsList = new ArrayList<>();

    @FXML
    private Label userScoreLabel;
    @FXML
    private TableView<TableStatistics> table;
    @FXML
    private TableColumn<TableStatistics, Integer> roundCol, scoreCol, timeCol;
    @FXML
    private TableColumn<TableStatistics, String> wordCol, resultCol;

    /**
     * Set up the user score label
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF " + Score.getTotalScore());

        ArrayList<String> words = Statistics.getTestedWords();
        ArrayList<Result> results = Statistics.getWordResult();
        ArrayList<Integer> scores = Statistics.getWordScore();
        ArrayList<Integer> times = Statistics.getWordTime();

        for (int i = 0 ; i < words.size(); i++) {
            statisticsList.add(new TableStatistics(i + 1, words.get(i), results.get(i).name(), scores.get(i), times.get(i)));
        }

        // setting up the table and the column
        roundCol.setCellValueFactory(new PropertyValueFactory<>("round"));
        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        table.getItems().setAll(statisticsList);
    }
    

    /**
     * When 'Play Again' button is pressed, play the quiz again, either practise or games module
     */
    @FXML
    private void playAgain() {
        if (Module.moduleTypeEqualsTo(ModuleType.PRACTISE)) {
            SceneManager.goToPractiseModule();
        } else {
            SceneManager.goToGamesModule();
        }
    }

    /**
     * When 'Main menu' button is pressed, go back to main menu
     */
    @FXML
    private void backToMainMenu() {
        SceneManager.goToMainMenu();
    }
    

}
