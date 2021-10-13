package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;
import spellingQuizUtil.Words;
import topicList.Topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TopicListController implements Initializable {
    private final ArrayList<Topic> topicList = new ArrayList<>();

    @FXML
    private TableView<Topic> table;
    @FXML
    private TableColumn<Topic, String> topicListColumn;


    // the method get all the topics' name and then set them into the table
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // get all the topics, the filename is the name of the topic
            String command = "ls words";
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);

            Process process = pb.start();

            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            int exitStatus = process.waitFor();

            if (exitStatus == 0) {
                // get the name of the topic (filename) and then add it into the list
                String name;
                while ((name = stdout.readLine()) != null) {
                    topicList.add(new Topic(name));
                }

            } else {  // otherwise print out the error
                String line;
                while ((line = stderr.readLine()) != null) {
                    System.err.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // setting up the table and the column
        topicListColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getItems().setAll(topicList);
    }

    @FXML
    private void selectTopic() throws IOException {
        try {
            // if the user selected a topic, then it will continue, otherwise it will throw an exception
            Topic selectedTopic = table.getSelectionModel().getSelectedItem();
            Words.setTopic(selectedTopic.getName());

        } catch (Exception e) {
            // if no topic is selected, then the default topic (1st topic) is selected
            Words.setTopic(topicList.get(0).getName());
        }

        // after the topic is selected, time to do the quiz
        if (Module.moduleTypeEqualsTo(ModuleType.practise)) {
            SceneController.goToPractiseModule();
        } else {
            SceneController.goToGamesModule();
        }
    }
    
    @FXML
    private void backToMainMenu() throws IOException {
        SceneController.goToMainMenu();
    }
}
