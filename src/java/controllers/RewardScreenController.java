package controllers;

import fileManager.FileControl;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;
import spellingQuizUtil.Score;
import spellingQuizUtil.Statistics;
import tableUtil.Leaderboard;
import tableUtil.StatsTable;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static fileManager.FileManager.LEADERBOARD_FILE;

public class RewardScreenController implements Initializable {
    private final String GREEN = "#7CFC00";
    private final String YELLOW = "#EEDC82";
    private final String RED = "#FA8072";
    private final String BLUE = "#89CFF0";

    private final ArrayList<StatsTable> statisticsList = new ArrayList<>();
    private final ArrayList<Leaderboard> leaderboardList = new ArrayList<>();
    private final ArrayList<String> leaderboardListToSave = new ArrayList<>();

    @FXML
    private Label userScoreLabel, practiseMod;
    @FXML
    private Button leaderboardBtn, statsBtn, clearBtn;
    @FXML
    private HBox gamesMod;

    @FXML
    private TableView<StatsTable> statisticsTable;
    @FXML
    private TableColumn<StatsTable, String> roundCol, wordCol, resultCol, scoreCol, timeCol;

    @FXML
    private TableView<Leaderboard> leaderboardTable;
    @FXML
    private TableColumn<Leaderboard, String> rankCol, nameCol, totalScoreCol, totalTimeCol;

    /**
     * Set up the reward screen:
     *      - user score
     *      - statistics table
     *      - leaderboard table if it is games module
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText(Score.getScore() + " OUT OF " + Score.getTotalScore());

        settingUpStatsTable();

        // only set up the leaderboard if it is in games module
        if (Module.moduleTypeEqualsTo(ModuleType.GAMES)) {
            practiseMod.setVisible(false);
        	gamesMod.setVisible(true);
            settingUpLeaderboardTable();
        }
    }

    /**
     * When 'Play Again' button is pressed, play the quiz again, either Practise or Games module
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

    /**
     * Switch between statistics table and leaderboard table
     * by clicking corresponding buttons
     */
    @FXML
    private void switchTable(ActionEvent event) {
        if (event.getSource().equals(statsBtn)) {
            leaderboardTable.setVisible(false);
            clearBtn.setVisible(false);
            leaderboardBtn.setDisable(false);
            statsBtn.setDisable(true);
            leaderboardBtn.setOpacity(0.5);
            statsBtn.setOpacity(1);
        } else {
            leaderboardTable.setVisible(true);
            clearBtn.setVisible(true);
            leaderboardBtn.setDisable(true);
            statsBtn.setDisable(false);
            leaderboardBtn.setOpacity(1);
            statsBtn.setOpacity(0.5);
        }
    }

    /**
     * Clear both the leaderboard table and the leaderboard file
     */
    @FXML
    private void clearLeaderboard() {
        leaderboardTable.getItems().clear();
        FileControl.clearFile(LEADERBOARD_FILE);
    }

    /**
     * Get all the statistics of the current game and then set them into the stats table
     */
    private void settingUpStatsTable() {
        ArrayList<String> words = Statistics.getTestedWords();
        ArrayList<String> results = Statistics.getWordResult();
        ArrayList<Integer> scores = Statistics.getWordScore();
        ArrayList<Integer> times = Statistics.getWordTime();

        // getting all the statistics
        for (int i = 0 ; i < words.size(); i++) {
            statisticsList.add(new StatsTable(i + 1, words.get(i), results.get(i), scores.get(i), times.get(i)));
        }

        // setting up the table and the columns
        roundCol.setCellValueFactory(new PropertyValueFactory<>("round"));
        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        statisticsTable.getItems().setAll(statisticsList);

        // codes from: https://stackoverflow.com/a/56309916
        //
        // This chunk of code colours the rows in stat table
        // the colour is corresponded to the result
        //      master = green, faulted = yellow, failed = red, skipped = blue
        statisticsTable.setRowFactory(tr -> new TableRow<>() {
            @Override
            protected void updateItem(StatsTable item, boolean empty) {
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
     * Get the old leaderboard and add the current user stats, and then set them into the leaderboard table
     */
    private void settingUpLeaderboardTable() {

        // the pause is needed to have the TextInputDialog in front of the main window
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(p -> {

            // TextInputDialog asks the user to enter their name
            TextInputDialog dialog = new TextInputDialog("Player 1");
            dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
            dialog.setTitle("What is your name?");
            dialog.setHeaderText("Enter your name! (Don't include this character *)");

            // when TextInputDialog is closed, get the user's name and then store it into the leaderboard
            dialog.setOnCloseRequest(d -> {

                // sorting out the leaderboard list and also get the rank of the current user
                int currentUserRank = prepareLeaderboardList(dialog.getEditor().getText());

                // setting up the table and the columns
                rankCol.setCellValueFactory(new PropertyValueFactory<>("rank"));
                nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                totalScoreCol.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
                totalTimeCol.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
                leaderboardTable.getItems().setAll(leaderboardList);
                leaderboardTable.scrollTo(currentUserRank);  // shows where the current user is at in leaderboard

                // This chunk of code colours the row of current user in leaderboard table
                // So the user can see their current rank easily
                leaderboardTable.setRowFactory(tr -> new TableRow<>() {
                    @Override
                    protected void updateItem(Leaderboard item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || item.getRank() != currentUserRank) {
                            setStyle("");
                        } else if (item.getRank() == currentUserRank) {
                            setStyle("-fx-background-color: " + BLUE);
                        }
                    }
                });

            });

            dialog.show();
        });
        pause.play();
    }

    /**
     * This method prepares the leaderboard list by sorting the previous leaderboard and current user stats.
     * While sorting it, get the rank of the current user
     * @param userName Current name's name
     * @return The rank of the current user
     */
    private int prepareLeaderboardList(String userName) {
        // get all the contents in the leaderboard file
        ArrayList<String> listOfItems = FileControl.readFile(LEADERBOARD_FILE);

        int rankIndex = 1;
        int currentUserRank = 0;
        boolean currentUserIsNotAdded = true;

        // go through all the contents of the file and add them into the leaderboard list
        for (String item : listOfItems) {
            String[] splitted = item.split("\\*\\*\\*");

            // if current user score is higher, then add the current user stats first and then the old users stats
            if (currentUserIsNotAdded && Score.getScore() > Integer.parseInt(splitted[1])) {
                addCurrentUser(rankIndex, userName);
                currentUserRank = rankIndex++;
                currentUserIsNotAdded = false;

            // if current user score is equal to the leaderboard one, then compare the time taken
            // the shorter the time, the better
            } else if (currentUserIsNotAdded && Score.getScore() == Integer.parseInt(splitted[1])) {
                if (Statistics.getTotalTime() <= Integer.parseInt(splitted[2])) {
                    addCurrentUser(rankIndex, userName);
                    currentUserRank = rankIndex++;
                    currentUserIsNotAdded = false;
                }
            }

            leaderboardList.add(new Leaderboard(rankIndex++, splitted[0], splitted[1], splitted[2]));
            leaderboardListToSave.add(item);
        }

        // if current user has not been added into the leaderboard (because the score is lower than everyone's score)
        // then add it at the last place
        if (currentUserIsNotAdded) {
            addCurrentUser(rankIndex, userName);
            currentUserRank = rankIndex;
        }

        // Now store/save the leaderboard into the file
        // The statistics is stored in the form of
        //      NAME***TOTAL_SCORE***TOTAL_TIME
        FileControl.writeFile(LEADERBOARD_FILE, leaderboardListToSave);

        return currentUserRank;
    }

    /**
     * This helper method add the current user stats into the leaderboard list
     * and also the list that will save into the file later
     * @param rankIndex The rank of current user
     * @param userName The name of current user
     */
    private void addCurrentUser(int rankIndex, String userName) {
        leaderboardList.add(new Leaderboard(rankIndex, userName, String.valueOf(Score.getScore()), String.valueOf(Statistics.getTotalTime())));
        leaderboardListToSave.add(String.join("***", leaderboardList.get(leaderboardList.size() - 1).getAllStats()));
    }
}
