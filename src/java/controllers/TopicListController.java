package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import controllers.commonControllers.CommonControllers;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;
import spellingQuizUtil.Words;
import tableUtil.Topic;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TopicListController extends CommonControllers implements Initializable {
    private final String GREY1 = "#EBEBEB";
    private final String GREY2 = "#DFDFDF";
    private final String BLACK = "#000000";

    private final ArrayList<Topic> topicList = new ArrayList<>();

    @FXML
    private TableView<Topic> table;
    @FXML
    private TableColumn<Topic, String> topicListColumn;


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
                topicList.add(new Topic(fileName));
            }

            // setting up the table and the column
            topicListColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            table.getItems().setAll(topicList);

            // colour the table, alternate row colour and the selected row has a different colour
            table.setRowFactory(tr -> new TableRow<>() {
                @Override
                protected void updateItem(Topic item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || item.getName() == null) {
                        setStyle("");
                    } else if (item.equals(table.getSelectionModel().getSelectedItem())) {
                        setStyle("-fx-background-color: " + BLACK);
                    } else if (topicList.indexOf(item) % 2 == 0) {
                        setStyle("-fx-background-color: " + GREY1);
                    } else {
                        setStyle("-fx-background-color: " + GREY2);
                    }
                }
            });
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
            Topic selectedTopic = table.getSelectionModel().getSelectedItem();
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
