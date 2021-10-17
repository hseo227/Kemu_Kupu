package controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;
import spellingQuizUtil.Result;
import spellingQuizUtil.Score;
import spellingQuizUtil.Statistics;
import tableUtil.Leaderboard;
import tableUtil.StatsTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RewardScreenController implements Initializable {
    private final String WHITE = "#FFF";
    private final String GREEN = "#7CFC00";
    private final String YELLOW = "#EEDC82";
    private final String RED = "#FA8072";
    private final String BLUE = "#89CFF0";

    private final String LEADERBOARD_FILE = ".hide/leaderboard";

    private final ArrayList<StatsTable> statisticsList = new ArrayList<>();
    private final ArrayList<Leaderboard> leaderboardList = new ArrayList<>();

    @FXML
    private Label userScoreLabel;
    @FXML
    private TableView<StatsTable> statisticsTable;
    @FXML
    private TableColumn<StatsTable, Integer> roundCol, scoreCol, timeCol;
    @FXML
    private TableColumn<StatsTable, String> wordCol, resultCol;
    @FXML
    private TableView<Leaderboard> leaderboardTable;
    @FXML
    private TableColumn<Leaderboard, String> rankCol, nameCol, totalScoreCol, totalTimeCol;
    @FXML
    private ToggleButton leaderboardTogBtn;

    /**
     * Set up the user score label
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF " + Score.getTotalScore());

        settingUpStatsTable();
        settingUpLeaderboardTable();
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

    @FXML
    private void showHideLeaderboard() {
        leaderboardTable.setVisible(leaderboardTogBtn.isSelected());
    }

    private void settingUpStatsTable() {
        ArrayList<String> words = Statistics.getTestedWords();
        ArrayList<Result> results = Statistics.getWordResult();
        ArrayList<Integer> scores = Statistics.getWordScore();
        ArrayList<Integer> times = Statistics.getWordTime();

        for (int i = 0 ; i < words.size(); i++) {
            statisticsList.add(new StatsTable(i + 1, words.get(i), results.get(i).name().toLowerCase(), scores.get(i), times.get(i)));
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
            protected void updateItem(StatsTable item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || item.getResult() == null) {
                    setStyle("-fx-background-color: " + WHITE);
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

    private void settingUpLeaderboardTable() {
        try {
            File file = new File(LEADERBOARD_FILE);
            file.createNewFile();
        } catch(IOException e) {
            System.err.println("Unable to create scheme file \"" + LEADERBOARD_FILE + "\"");
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(p -> {
            TextInputDialog dialog = new TextInputDialog("Player 1");
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
            dialog.setTitle("What is your name?");
            dialog.setHeaderText("Enter your name! (Don't include this character *)");

            dialog.setOnCloseRequest(d -> {

                // First, get all the words in the file
                try {
                    BufferedReader readFile = new BufferedReader(new FileReader(LEADERBOARD_FILE));
                    String line;
                    int rankIndex = 1;

                    // go through all the lines in the file and add into the list
                    while ((line = readFile.readLine()) != null) {
                        String[] splitted = line.split("\\*\\*\\*");

                        // if current user score is higher, add the current user stats first and then the old users stats
                        if (Score.getScore() > Integer.parseInt(splitted[1])) {
                            leaderboardList.add(new Leaderboard(rankIndex, dialog.getEditor().getText(), String.valueOf(Score.getScore()), String.valueOf(Statistics.getTotalTime())));
                            rankIndex++;
                        }

                        leaderboardList.add(new Leaderboard(rankIndex, splitted[0], splitted[1], splitted[2]));
                        rankIndex++;
                    }
                    readFile.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // setting up the table and the column
                rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
                nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                totalScoreCol.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
                totalTimeCol.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
                leaderboardTable.getItems().setAll(leaderboardList);
            });

            dialog.show();
        });
        pause.play();

    }

}
