package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class statisticsController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button backButton;
    @FXML
    private TableView<Statistics> table;
    @FXML
    private TableColumn<Statistics, String> wordColumn;
    @FXML
    private TableColumn<Statistics, Integer> masteredColumn, faultedColumn, failedColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get the file location of word history file, and then get the list in the file
        String fileName = SpellingQuiz.getFILES().get("wordHistory_file");
        ArrayList<String> list = SpellingQuiz.getWordsInFile(fileName, true);

        // these two are used to store the words and the rates of that word
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<Integer[]> rates = new ArrayList<>();

        for (String line : list) {
            String[] splited = line.split("_");  // split into the word and the result

            // if the array does not have the word yet, set up a new row for that word
            if (!words.contains(splited[0])) {
                words.add(splited[0]);
                Integer[] temp = {0, 0, 0};
                rates.add(temp);
            }

            int index = words.indexOf(splited[0]);
            rates.get(index)[Integer.parseInt(splited[1])]++;  // rate increments respectively
        }

        // this part of the code is to add the data into the table
        ArrayList<Statistics> statsList = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            statsList.add(new Statistics(words.get(i), rates.get(i)[0], rates.get(i)[1], rates.get(i)[2]));
        }

        wordColumn.setCellValueFactory(new PropertyValueFactory<Statistics, String>("word"));
        masteredColumn.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("mastered"));
        faultedColumn.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("faulted"));
        failedColumn.setCellValueFactory(new PropertyValueFactory<Statistics, Integer>("failed"));

        table.getItems().setAll(statsList);
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        SceneController.goToMainMenu();
    }

}
