package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;
import spellingQuizUtil.Words;
import tableUtil.TableTopic;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TopicListController implements Initializable {
    private final ArrayList<TableTopic> topicList = new ArrayList<>();

    @FXML
    private TableView<TableTopic> table;
    @FXML
    private TableColumn<TableTopic, String> topicListColumn;


    /**
     * This method get all the topics' name and then set them into the table
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get the files/topics names
        String[] files = new File("words").list();

        // if there are topic list, then set them into the table
        if (files != null) {
            for (String fileName : files) {
                topicList.add(new TableTopic(fileName));
            }

            // setting up the table and the column
            topicListColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            table.getItems().setAll(topicList);
        }
    }

    /**
     * When 'Select' button is pressed, set the word list topic to the selected one
     * or the default one, which is the first one, if the user did not select any topic
     */
    @FXML
    private void selectTopic() {
        try {
            // if the user selected a topic, then it will continue, otherwise it will throw an exception
            TableTopic selectedTopic = table.getSelectionModel().getSelectedItem();
            Words.setTopic(selectedTopic.getName());

        } catch (Exception e) {
            // if no topic is selected, then the default topic (1st topic) is selected
            Words.setTopic(topicList.get(0).getName());
        }

        // after the topic is selected, time to do the quiz
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
