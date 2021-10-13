import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import spellingQuizUtil.FestivalSpeech;
import controllers.SceneManager;

import java.io.InputStream;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FestivalSpeech.settingUp();


        URL mainMenuFxmlUrl = getClass().getResource("mainMenu.fxml");
        InputStream iconImageStream = getClass().getResourceAsStream("media/window-icon.png");

        // if successful to load main menu FXML and icon image then load them, otherwise print out error message
        if (mainMenuFxmlUrl != null && iconImageStream != null) {

            Parent root = FXMLLoader.load(mainMenuFxmlUrl);
            // set title and icon
            primaryStage.setTitle("Kēmu Kupu");
            primaryStage.getIcons().add(new Image(iconImageStream));
            primaryStage.setScene(new Scene(root));
            SceneManager.settingUp(primaryStage.getScene());  // this is for the future use of changing scene
            primaryStage.setResizable(false);
            primaryStage.show();

        } else {
            System.err.println("Failed to load to either main menu FXML or game icon image");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
