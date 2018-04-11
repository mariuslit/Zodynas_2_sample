package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sample.ReadWriteFile.ReadWriteData;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    private TextField zodisTextField;
    @FXML
    private TextField laikinasTextField;
    @FXML
    private Label vertimasLabel;
    @FXML
    private ListView visiZodynoZodziaiListView;
    @FXML
    private ListView variant7;
    @FXML
    private Button edit;
    @FXML
    private ToggleGroup zodynuGrupe; // z1-z6
    @FXML
    private RadioButton z1;
    @FXML
    private RadioButton z2;
    @FXML
    private RadioButton z3;
    @FXML
    private RadioButton z4;
    @FXML
    private RadioButton z5;
    @FXML
    private RadioButton z6;

    private Map<String, String> zodynasTreeMap = new TreeMap<>();
    private Map<String, String> setingsLinkedMap = new TreeMap<>();

    //todo
    @Override
    // ANDRIAUS KOMENTARAS: "Cia nuskaitai faila ir sudeti i map. Sitas metodas leidziamas ant star upo"
    public void initialize(URL location, ResourceBundle resources) {

        // nustatomas failas iš kurio bus skaitomas žodynas
        RadioButton pazymetasZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        String failoVardas = pazymetasZodynas.getId();

        // nuskaito zodyną į zodynasTreemap + siunčiamas failo vardas failoVardas, (laikinai išjungiau su "z1")
        ReadWriteData.readFile(zodynasTreeMap, "z1"); // failoVardas
        ReadWriteData.readFile(setingsLinkedMap, "setings"); // failoVardas
        ReadWriteData.writeFile(zodynasTreeMap, "z1"); // failoVardas

        // TODO sukurti ReadFileSetings setings.txt
    }

    public void action(ActionEvent event) {
        // ANDRIAUS KOMENTARAS: "cia gali naudoti savo map jis uzpildomas viena karta kai uzkraunama aplikacija"
// ANDRIAUS: vocaburalyMap.get("teksts"); // darai ka nori su juo
    }

    //todo
    public void versk(javafx.event.ActionEvent event) {
        // pasirinktas žodynas
        RadioButton aktyvusZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        laikinasTextField.setText("Pasirinktas žodynas: " + aktyvusZodynas.getId() + "=" + aktyvusZodynas.getText());

        String fragmentas = zodisTextField.getText(); // nuskaito verčiama žodį
        String vertimas = zodynasTreeMap.get(fragmentas); // suranda žodžio vertimą

        if (vertimas != null) {
            vertimasLabel.setText(vertimas);
        } else {
            vertimasLabel.setText("žodyne tokio žodžio nėra");
        }

        // parodo visus žodyno žodžius per listView
        //visiZodynoZodziaiListView.//todo čia reika įrašyti duomenų trynimo iš ListView metodą;
        for (String w : zodynasTreeMap.keySet()) {
            visiZodynoZodziaiListView.getItems().addAll(w);
//            s.append(w).append(" \n"); // s.append() prijungia prie s viską kas skliausteliuose
        }
        gautiAtitikmenuVariantus(zodynasTreeMap,fragmentas);
    }

    // variantų paieškos varikliukas
    public TreeSet gautiAtitikmenuVariantus(Map<String, String> zodynas, String fragmentas) {
        TreeSet<String> variantai = new TreeSet<>(); // čia talpinamas atsakymas
        for (String item :zodynas.keySet()) {
//            yraNera = zodynas.get(w);
//            System.out.println(yraNera.contains(zodis));
            if (item.toLowerCase().startsWith(fragmentas)) { // true jei rado atitikmenį
                variantai.add(item);
                variant7.getItems().addAll(item);
            }
        }
        return variantai;
    }

    // veikiantis metodas atspausdinti duomenis į ListView
    public void printListView() {
        TreeSet<String> ts = new TreeSet<>(Arrays.asList("pirmas", "antras", "trečias", "ketvirtas", "penktas", "šeštas", "septintas"));
        for (String item : ts) {
            visiZodynoZodziaiListView.getItems().addAll(item);
        }
    }

    // aktyvaus žodyno keitimas
    public void kitasZodynas(ActionEvent event) {
        RadioButton pazymetasZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        String tugelis = pazymetasZodynas.getText();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Pasirinktas žodynas:\n   " + tugelis + "\nlaikinai veiks tik z1: " + z1.getText());
        alert.show();
        zodisTextField.setPromptText(tugelis);
        laikinasTextField.setText("Pasirinktas žodynas: " + tugelis);
    }

    public void exitButon() {
        Platform.exit();
    }
}
