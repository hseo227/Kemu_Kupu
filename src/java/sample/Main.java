package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SpellingQuiz.initialise();

        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        primaryStage.setTitle("Spelling Wiz!");
        primaryStage.setScene(new Scene(root));
        SceneController.initialise(primaryStage.getScene());  // this is for the future use of changing scene
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
