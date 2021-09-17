package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SceneController {
    private static Scene scene;

    private static final String mainMenuFXML = "mainMenu.fxml";
    private static final String quizFXML = "quiz.fxml";
    private static final String statisticsFXML = "statistics.fxml";


    public static void initialise(Scene scn) {
        scene = scn;
    }

    public static void goToMainMenu() throws IOException {
        changeScene(mainMenuFXML);
    }

    public static void goToQuiz() throws IOException {
        changeScene(quizFXML);
    }

    public static void goToStatistics() throws IOException {
        changeScene(statisticsFXML);
    }

    private static void changeScene(String fxml) throws IOException {
        AnchorPane pane = FXMLLoader.load(SceneController.class.getResource(fxml));
        scene.setRoot(pane);
    }
}
