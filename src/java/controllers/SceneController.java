package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class SceneController {
    private static Scene scene;

    private static final String MAIN_MENU_FXML = "mainMenu.fxml";
    private static final String TOPIC_LIST_FXML = "topicList.fxml";
    private static final String PRACTISE_FXML = "modulePractise.fxml";
    private static final String GAMES_FXML = "moduleGames.fxml";
    private static final String REWARD_SCREEN_FXML = "rewardScreen.fxml";


    public static void initialise(Scene scn) {
        scene = scn;
    }

    public static void goToMainMenu() throws IOException {
        changeScene(MAIN_MENU_FXML);
    }

    public static void goToTopicList() throws IOException {
        changeScene(TOPIC_LIST_FXML);
    }

    public static void goToPractiseModule() throws IOException {
        changeScene(PRACTISE_FXML);
    }

    public static void goToGamesModule() throws IOException {
        changeScene(GAMES_FXML);
    }
    
    public static void goToRewardScreen() throws IOException {
        changeScene(REWARD_SCREEN_FXML);
    }

    private static void changeScene(String fxml) throws IOException {
        URL url = SceneController.class.getResource("../" + fxml);

        // if successful to load FXML file then load it, otherwise print out error message
        if (url != null) {
            Pane pane = FXMLLoader.load(url);
            scene.setRoot(pane);
        } else {
            System.err.println("Failed to load - " + fxml);
        }
    }
}
