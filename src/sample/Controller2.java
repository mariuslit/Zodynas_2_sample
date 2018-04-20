package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import sample.ReadWriteFile.ReadWriteData;

import java.util.Map;
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

    private Map<String, String> zodynasTreeMapR = new TreeMap<>();
    private Map<String, String> settingsLinkedMapR = new TreeMap<>();

    public void initialize() { // ištryniau (URL location, ResourceBundle resources) ir suveikė
        zodisTextFieldR.setText(Controller.infoR_1.getFragmentas()); // informacijos nuskaitymas iš Informacija klasės
//        vertimasTextFieldR.setPromptText("naujas žodis");

        settingsLinkedMapR = ReadWriteData.readFile("settings");
        String zodynoPavadinimas =  settingsLinkedMapR.get(settingsLinkedMapR.get("default"));
        zodynoPavadinimasTextFieldR.setText(zodynoPavadinimas);
        antrasteLabelR.setText("Žodyno \"" + zodynoPavadinimas + "\" redagavimas");

        vertimasTextFieldR.setText(Controller.infoR_1.getVertimas()); // informacijos nuskaitymas iš Informacija klasės
        System.out.println("iš Controller2: " + Controller.infoR_1.getFragmentas().toString());
    }

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
        String pavadinimas = zodynoPavadinimasTextFieldR.getText();
        if (!zodynoPavadinimasTextFieldR.isEditable()) {
            // kai paspaudžiamas button keisti žodyno pavadinimą
            zodynoPavadinimasTextFieldR.setEditable(true);
            zodynoPavadinimasTextFieldR.setDisable(false);
            zodynoPavadinimasTextFieldR.selectAll(); // TODO neveikia nors anksčiau veikė
            keistiZodynoPavadinimaButtonR.setText("Išsaugoti žodyno pavadinimą");
            keistiZodynoPavadinimaButtonR.setTextFill(Color.RED);

            settingsLinkedMapR = ReadWriteData.readFile("settings");
            System.out.println(settingsLinkedMapR.get("default") + pavadinimas);
        } else {
            // kai pavadinimas pakeistas sutvarkom GUI ir išsaugom settings failą
            zodynoPavadinimasTextFieldR.setEditable(false);
            zodynoPavadinimasTextFieldR.setDisable(true);
            antrasteLabelR.setText("Žodyno \"" + pavadinimas + "\" redagavimas");
            keistiZodynoPavadinimaButtonR.setText("Keisti žodyno pavadinimą");
            keistiZodynoPavadinimaButtonR.setTextFill(Color.BLACK);

            settingsLinkedMapR.put(settingsLinkedMapR.get("default").toString(), pavadinimas);
            ReadWriteData.writeFile(settingsLinkedMapR, "settings");
        }
    }

    public void exitButonR() {
        Platform.exit();
    }
}
