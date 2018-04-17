package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller2 {

    @FXML
    private Button keistiPavadinimaButton;
    @FXML
    private Label titleRaedagavimasLabel;
    @FXML
    private TextField zodynoPavadinimas;
    @FXML
    private TextField zodisTextFieldR;
    @FXML
    private TextField vertimasTextFieldR;
    @FXML
    private ListView visiListViewR;

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addWordX() { // (ActionEvent event) teko išimti nes netinka šiai užduočiai
        String a = zodisTextFieldR.getText(); // žodis
        String b = vertimasTextFieldR.getText(); // vertimas
        String c = "";
        if (a.length() != 0 || a.length() != 0) {
            c = (a.replace("-", " ") + "-" + b.replace("-", " "));
            visiListViewR.getItems().addAll(c);
        }
    }

    public void isvalytiViska() {
        zodisTextFieldR.clear();
        vertimasTextFieldR.clear();
//        zodisTextField.setCursor(); // TODO: padėti kursorių į input langelį
    }

    public void keistiZodynoPavadinima() {
        if (!zodynoPavadinimas.isEditable()) {
            zodynoPavadinimas.setEditable(true);
            zodynoPavadinimas.setDisable(false);
            zodynoPavadinimas.selectAll();
            keistiPavadinimaButton.setText("Išsaugoti žodyno pavadinimą");
            keistiPavadinimaButton.setTextFill(Color.RED);
        } else {
            zodynoPavadinimas.setEditable(false);
            zodynoPavadinimas.setDisable(true);
            String pav = zodynoPavadinimas.getText();
            titleRaedagavimasLabel.setText("Žodyno \"" + pav + "\" redagavimas");
            keistiPavadinimaButton.setText("Keisti žodyno pavadinimą");
            keistiPavadinimaButton.setTextFill(Color.BLACK);
        }

    }

    // TODO NEBAIGTA 4 reakcija į klavišo paspaudimą žodžio fragmento įvedimo langelyje
    public void click(MouseEvent event) {
        String selectedItem = variantListView.getSelectionModel().getSelectedItem(); // padaryta pagal Andriaus kodą
        pirmasAtitikmuoLabel.setText(selectedItem);
        vertimasLabel.setText(zodynasTreeMap.get(selectedItem));
//        String s = event.getPickResult().toString(); // TODO gauti List'o pavadinimą o,mouseClick
//        String idList = variantListView.getId(); // TODO gauti List'o pavadinimą o,mouseClick
//        laikinasLabel.setText(s); // TODO kai viskas veiks laikinasLabel. pakeisti į vertimasLabel.
    }


    public void exitButon() {
        Platform.exit();
    }
}
