package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class Controller2 {

    @FXML
    private Button keistiZodynoPavadinimaButtonR;
    @FXML
    private Label antrasteLabelR;
    @FXML
    private TextField zodynoPavadinimasTextFieldR;
    @FXML
    private TextField zodisTextFieldR;
    @FXML
    private TextField vertimasTextFieldR;
    @FXML
    private ListView visiListViewR;

//    public void initialize(URL location, ResourceBundle resources) {
//
//    }

    public void addWordR() { // (ActionEvent event) teko išimti nes netinka šiai užduočiai
        String a = zodisTextFieldR.getText(); // žodis
        String b = vertimasTextFieldR.getText(); // vertimas
        String c = "";
        if (a.length() != 0 || a.length() != 0) {
            c = (a.replace("-", " ") + "-" + b.replace("-", " "));
            visiListViewR.getItems().addAll(c);
        }
    }

    public void isvalytiViskaR() {
        zodisTextFieldR.clear();
        vertimasTextFieldR.clear();
//        zodisTextField.setCursor(); // TODO: padėti kursorių į input langelį
    }

    public void keistiZodynoPavadinimaR() {
        if (!zodynoPavadinimasTextFieldR.isEditable()) {
            zodynoPavadinimasTextFieldR.setEditable(true);
            zodynoPavadinimasTextFieldR.setDisable(false);
            zodynoPavadinimasTextFieldR.selectAll();
            keistiZodynoPavadinimaButtonR.setText("Išsaugoti žodyno pavadinimą");
            keistiZodynoPavadinimaButtonR.setTextFill(Color.RED);
        } else {
            zodynoPavadinimasTextFieldR.setEditable(false);
            zodynoPavadinimasTextFieldR.setDisable(true);
            String pav = zodynoPavadinimasTextFieldR.getText();
            antrasteLabelR.setText("Žodyno \"" + pav + "\" redagavimas");
            keistiZodynoPavadinimaButtonR.setText("Keisti žodyno pavadinimą");
            keistiZodynoPavadinimaButtonR.setTextFill(Color.BLACK);
        }
    }

    public void exitButonR() {
        Platform.exit();
    }
}
