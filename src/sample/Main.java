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

//        // atlieka veiksmus prieš nutraukiant programos veikimą
//        Controller controller = load.getController();
//        primaryStage.setOnHidden(event -> controller.onCloseEvent());

        primaryStage.setScene(new Scene(root));
        primaryStage.show();


// pagal bucky video sukuriamas mygtukas ir parodomas lange.
//        Button but = new Button();
//        but.setText("paspausk mane");
//        Pane pane = new Pane();
//        pane.getChildren().add(but);
//
//        Scene scene = new Scene(pane,500,500);
//        primaryStage.setScene(scene);
//        primaryStage.show();


    }
}






