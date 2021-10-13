package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import spellingQuiz.Module;
import spellingQuizUtil.ModuleType;

public class MainMenuController {

    /**
     * Load Practise Module
     */
    @FXML
    private void loadPractiseModule() {
        Module.setModuleType(ModuleType.PRACTISE);
        SceneManager.goToTopicList();
    }

    /**
     * Load Games Module
     */
    @FXML
    private void loadGamesModule() {
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
