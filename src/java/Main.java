import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import spellingQuizUtil.FestivalSpeech;
import controllers.SceneController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FestivalSpeech.settingUp();

        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        // set title and icon
        primaryStage.setTitle("Kēmu Kupu");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("media/window-icon.png")));
        primaryStage.setScene(new Scene(root));
        SceneController.initialise(primaryStage.getScene());  // this is for the future use of changing scene
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
