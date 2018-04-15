package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class Main extends Application {
    //    Button butT;
    //    Stage winD;
    //    Scene sceN;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Žodynas Ltit");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
//        winD = primaryStage;
//        butT = new Button("hellow world");
//        StackPane layout = new StackPane();
//        layout.getChildren().add(butT);
//        Scene cscen = new Scene(layout, 500, 500);
//        primaryStage.setScene(cscen);
//        primaryStage.show();
//        butT.setOnAction(e->winD.setScene(sceN));


    }

//    public void showEditWindow(Stage primaryStage) throws Exception {
//        Parent root2 = FXMLLoader.load(getClass().getResource("sample2.fxml"));
//        primaryStage.setTitle("Žodynų redagavimas");
//        primaryStage.setScene(new Scene(root2));
//        primaryStage.show();
//    }
}
