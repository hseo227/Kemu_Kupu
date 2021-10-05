package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class topicListController implements Initializable {
    private ArrayList<Topic> topicList = new ArrayList<>();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainLabel;
    @FXML
    private Button selectBtn, backBtn;
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
        topicListColumn.setCellValueFactory(new PropertyValueFactory<Topic, String>("name"));
        table.getItems().setAll(topicList);
    }

    @FXML
    private void selectTopic(ActionEvent event) throws IOException {
        try {
            // if the user selected a topic, then it will continue, otherwise it will throw an exception
            Topic selectedTopic = table.getSelectionModel().getSelectedItem();
            Module.setTopic(selectedTopic.getName());

        } catch (Exception e) {
            // if no topic is selected, then the default topic (1st topic) is selected
            Module.setTopic(topicList.get(0).getName());
        }

        // after the topic is selected, time to do the quiz
        SceneController.goToQuiz();
    }
    
    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        SceneController.goToMainMenu();
    }
}
