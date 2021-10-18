package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class SceneManager {
    private static final String MAIN_MENU_FXML = "mainMenu.fxml";
    private static final String TOPIC_LIST_FXML = "topicList.fxml";
    private static final String PRACTISE_FXML = "modulePractise.fxml";
    private static final String GAMES_FXML = "moduleGames.fxml";
    private static final String REWARD_SCREEN_FXML = "rewardScreen.fxml";

    private static Scene scene;

    /**
     * This method will only run once and will run at the start of the program.
     * Set up the scene, so can change the scene in the future.
     */
    public static void settingUp(Scene scn) {
        scene = scn;
    }

    /**
     * goTo...
     * These methods are helper functions of changing the scene
     */
    public static void goToMainMenu() {
        changeScene(MAIN_MENU_FXML);
    }

    public static void goToTopicList() {
        changeScene(TOPIC_LIST_FXML);
    }

    public static void goToPractiseModule() {
        changeScene(PRACTISE_FXML);
    }

    public static void goToGamesModule() {
        changeScene(GAMES_FXML);
    }
    
    public static void goToRewardScreen() {
        changeScene(REWARD_SCREEN_FXML);
    }

    /**
     * Change the scene to the provided FXML file
     * @param fxml The FXML file that is going to change to
     */
    private static void changeScene(String fxml) {

        // if successful to load FXML file then load it, otherwise print out error message
        try {
            URL url = SceneManager.class.getResource("../" + fxml);
            if (url != null) {
                Pane pane = FXMLLoader.load(url);
                scene.setRoot(pane);

            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.err.println("Failed to load " + fxml);
        }
    }
}
