package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SceneController {
    private static Scene scene;

    private static final String mainMenuFXML = "mainMenu.fxml";
    private static final String topicListFXML = "topicList.fxml";
    private static final String practiseFXML = "modulePractise.fxml";
    private static final String gamesFXML = "moduleGames.fxml";
    private static final String rewardScreenFXML = "rewardScreen.fxml";


    public static void initialise(Scene scn) {
        scene = scn;
    }

    public static void goToMainMenu() throws IOException {
        changeScene(mainMenuFXML);
    }

    public static void goToTopicList() throws IOException {
        changeScene(topicListFXML);
    }

    public static void goToPractiseModule() throws IOException {
        changeScene(practiseFXML);
    }

    public static void goToGamesModule() throws IOException {
        changeScene(gamesFXML);
    }
    
    public static void goToRewardScreen() throws IOException {
        changeScene(rewardScreenFXML);
    }

    private static void changeScene(String fxml) throws IOException {
        Pane pane = FXMLLoader.load(SceneController.class.getResource(fxml));
        scene.setRoot(pane);
    }
}
