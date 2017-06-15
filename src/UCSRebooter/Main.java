package UCSRebooter;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


/**
 * Class for start app
 *
 * @author Vyacheslav Y.
 * @version 1.0
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI/GUI.fxml"));
        primaryStage.setTitle("Bug UCS Killer");
        primaryStage.setScene(new Scene(root, 564, 301));
        primaryStage.show();
    }
}
