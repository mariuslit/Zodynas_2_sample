package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller2 {

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
        String x = zodisTextFieldR.getText();
        String y = vertimasTextFieldR.getText();
        visiListViewR.getItems().addAll(x.replace("-", " ") + "-" + y.replace("-", " "));

    }

    public void isvalytiViska() {
        zodisTextFieldR.clear();
        vertimasTextFieldR.clear();
//        zodisTextField.setCursor(); // TODO: padėti kursorių į langelį
    }

    public void keistiZodynoPavadinima() {
        zodynoPavadinimas.selectAll();
        vertimasTextFieldR.clear();
    }
}
