package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
    protected final static String GREEN = "#7CFC00";
    protected final static String YELLOW = "#EEDC82";
    protected final static String RED = "#FA8072";
    protected final static String BLUE = "#89CFF0";

    private final ArrayList<TableStatistics> statisticsList = new ArrayList<>();

    @FXML
    private Label userScoreLabel;
    @FXML
    private TableView<TableStatistics> statisticsTable;
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
            statisticsList.add(new TableStatistics(i + 1, words.get(i), results.get(i).name().toLowerCase(), scores.get(i), times.get(i)));
        }

        // setting up the table and the column
        roundCol.setCellValueFactory(new PropertyValueFactory<>("round"));
        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        statisticsTable.getItems().setAll(statisticsList);

        // set the rows' colour correspond to the result
        //
        // From: https://stackoverflow.com/a/56309916
        statisticsTable.setRowFactory(tr -> new TableRow<>() {
            @Override
            protected void updateItem(TableStatistics item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || item.getResult() == null) {
                    setStyle("");
                } else if (item.getResult().equals("mastered")) {
                    setStyle("-fx-background-color: " + GREEN);
                } else if (item.getResult().equals("faulted")) {
                    setStyle("-fx-background-color: " + YELLOW);
                } else if (item.getResult().equals("failed")) {
                    setStyle("-fx-background-color: " + RED);
                } else {
                    setStyle("-fx-background-color: " + BLUE);
                }
            }
        });
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
