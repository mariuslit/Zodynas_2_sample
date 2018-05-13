package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader load = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = load.load();
        primaryStage.setTitle("Žodynas Ltit");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}






