package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*****************************************************************

 Developer:   Marius Litvinas 2018-03-31
 Customer:    Indrė Litvinienė
 Programm:    Dictionary "Ltit"

******************************************************************
 Description:

 paremta principu: ŽODIS -> VERTIMAS
 ŽODIS    - verčiamas žodis arba frazė iš kelių žodžių
 VERTIMAS - sakinys ar keli sakiniai, gali būti iš kelių eilučių.

 galima susikurti savo 6 žodynus, juos pildyti ir readaguoti

 *****************************************************************/
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader load = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = load.load();
        primaryStage.setResizable(false); // nekintantis rėmelis
        primaryStage.setTitle("Žodynas Ltit");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}






