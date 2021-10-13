package controllers;

import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label mainMenuLabel, mainMenuLabel1;
    @FXML
    private Button practiseModule, gamesModule, quitBtn;


    // load Practise Module
    @FXML
    private void loadPractiseModule(ActionEvent event) throws IOException {
        Module.setModuleType(ModuleType.practise);
        SceneController.goToTopicList();
    }

    // load Games Module
    @FXML
    private void loadGamesModule(ActionEvent event) throws IOException {
        Module.setModuleType(ModuleType.games);
        SceneController.goToTopicList();
    }

    // Quit the game
    @FXML
    private void quit() {
        Platform.exit();
    }

}
