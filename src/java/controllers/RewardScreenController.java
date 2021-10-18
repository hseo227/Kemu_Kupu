package controllers;

import fileManager.FileManager;
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

import java.io.*;
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
     * Set up the reward screen
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF " + Score.getTotalScore());

        settingUpStatsTable();

        // only set up and shows the leaderboard if it is in games module
        if (Module.moduleTypeEqualsTo(ModuleType.GAMES)) {
            leaderboardTogBtn.setVisible(true);
            settingUpLeaderboardTable();
        }
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

        if (leaderboardTogBtn.isSelected()) {
            leaderboardTogBtn.setText("Statistics");
        } else {
            leaderboardTogBtn.setText("Leaderboard");
        }
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
            System.err.println("Unable to create file that stores the leaderboard \"" + LEADERBOARD_FILE + "\"");
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(p -> {
            TextInputDialog dialog = new TextInputDialog("Player 1");
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
            dialog.setTitle("What is your name?");
            dialog.setHeaderText("Enter your name! (Don't include this character *)");

            dialog.setOnCloseRequest(d -> {

                // First, prepare the leaderboard list by sorting the previous leaderboard and current user stats
                prepareLeaderboardList(dialog.getEditor().getText());

                // setting up the table and the column
                rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
                nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                totalScoreCol.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
                totalTimeCol.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
                leaderboardTable.getItems().setAll(leaderboardList);

                // Now store the leaderboard
                storeLeaderboardIntoFile();
            });

            dialog.show();
        });
        pause.play();
    }

    private void prepareLeaderboardList(String userName) {
        // get all the contents in the leaderboard file
        ArrayList<String> listOfItems = FileManager.readFile(LEADERBOARD_FILE);

        int rankIndex = 1;
        boolean currentUserIsNotAdded = true;

        // go through all the lines in the file and add into the list
        for (String item : listOfItems) {
            String[] splitted = item.split("\\*\\*\\*");

            // if current user score is higher, add the current user stats first and then the old users stats
            if (currentUserIsNotAdded && Score.getScore() > Integer.parseInt(splitted[1])) {
                leaderboardList.add(new Leaderboard(rankIndex, userName, String.valueOf(Score.getScore()), String.valueOf(Statistics.getTotalTime())));
                rankIndex++;
                currentUserIsNotAdded = false;
            }

            leaderboardList.add(new Leaderboard(rankIndex, splitted[0], splitted[1], splitted[2]));
            rankIndex++;
        }

        // if current user score is lower than everyone's score, then add it at the last
        if (currentUserIsNotAdded) {
            leaderboardList.add(new Leaderboard(rankIndex, userName, String.valueOf(Score.getScore()), String.valueOf(Statistics.getTotalTime())));
        }
    }

    private void storeLeaderboardIntoFile() {
        try {
            PrintWriter writeFile = new PrintWriter(new FileWriter(LEADERBOARD_FILE));

            for (Leaderboard i : leaderboardList) {
                writeFile.println(i.getAllStats());
            }

            writeFile.close();

        } catch (IOException e) {
            System.err.println("Failed to write into " + LEADERBOARD_FILE);
        }
    }

}
