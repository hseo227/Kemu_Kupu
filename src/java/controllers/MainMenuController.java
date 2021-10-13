package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;

import java.io.IOException;

public class MainMenuController {

    /**
     * Load Practise Module
     */
    @FXML
    private void loadPractiseModule() throws IOException {
        Module.setModuleType(ModuleType.PRACTISE);
        SceneManager.goToTopicList();
    }

    /**
     * Load Games Module
     */
    @FXML
    private void loadGamesModule() throws IOException {
        Module.setModuleType(ModuleType.GAMES);
        SceneManager.goToTopicList();
    }

    /**
     * Quit the game
     */
    @FXML
    private void quit() {
        Platform.exit();
    }

}
