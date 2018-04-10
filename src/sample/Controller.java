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
    private TextArea visiZodynoZodziaiTextArea;
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
//    private Map<String, String> vocaburalyMap = new TreeMap<>();

    //todo
    @Override
    // ANDRIAUS KOMENTARAS: "Cia nuskaitai faila ir sudeti i map. Sitas metodas leidziamas ant star upo"
    public void initialize(URL location, ResourceBundle resources) {

        // nustatomas failas iš kurio bus skaitomas žodynas
        RadioButton pazymetasZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        String failoVardas = pazymetasZodynas.getId();

        // nuskaito zodyną į zodynasTreemap + siunčiamas failo vardas failoVardas, (laikinai išjungiau su "z1")
        ReadWriteData.readFile(zodynasTreeMap, "z1"); // failoVardas
    }

    public void action(ActionEvent event) {
        // ANDRIAUS KOMENTARAS: "cia gali naudoti savo map jis uzpildomas viena karta kai uzkraunama aplikacija"
// ANDRIAUS: vocaburalyMap.get("teksts"); // darai ka nori su juo
    }

    //todo
    public void versk(javafx.event.ActionEvent event) {
        RadioButton pazymetasZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        String failoVardas = pazymetasZodynas.getId();
        laikinasTextField.setText("Pasirinktas žodynas: " + failoVardas +"="+ pazymetasZodynas.getText());
        String zodis = zodisTextField.getText(); // nuskaito verčiama žodį
        String vertimas = zodynasTreeMap.get(zodis); // suranda žodžio vertimą
        if (vertimas != null) {
            vertimasLabel.setText(vertimas);
//            laikinasTextField.setText("Zodyno dydis: " + zodynasTreeMap.size());
        } else {
            vertimasLabel.setText("žodyne tokio žodžio nėra");
        }
        variant7.getId();

        // paima žodyno Key reikšmes ir sukelia į List dinaminį masyvą
//        List<String> wordsKey = new ArrayList<>(zodynasTreeMap.keySet());
//        StringBuilder s = new StringBuilder(); // naujas string tipas, reikia išbandyti
        for (String w : zodynasTreeMap.keySet()) {
            visiZodynoZodziaiListView.getItems().addAll(w);
//            s.append(w).append(" \n"); // s.append() prijungia prie s viską kas skliausteliuose
        }
//        visiZodynoZodziaiTextArea.setText(s.toString());

//        laikinasTextField.setText(pask);
//        s.append("..." + gautiAtitikmenuVariantus(zodynasTreeMap.keySet(), zodis).spliterator());
    }
    // TODO veikia sąrašo įkėlimas į visiZodynoZodziaiTextArea

    // TODO pabaigti analizuoti šitą koda, jis veikia ant zodynas1 console
    // TODO variantų paieškos varikliukas
    public TreeSet gautiAtitikmenuVariantus(Set<String> zodynoKey, String zodis) {// pakaitalioti list-set
//        žodis yra/nėra
        String yraNera;
        List<String> f = new ArrayList<>();
//        System.out.print("Veskite žodį: ");
//        Scanner sc = new Scanner(System.in);
//        String zodis = sc.nextLine(); // įvedamas key, arba pradžios fragmentas
        Map<String, String> tm = new TreeMap<>();
        f.addAll(tm.keySet()); // mapo key reikšmų priskyrimas listui
        System.out.println(f);
        TreeSet<String> galimiVariantai = new TreeSet<>();
        for (int i = 0; i < f.size(); i++) {
            yraNera = f.get(i);
//            System.out.println(yraNera.contains(zodis));
            if (yraNera.toLowerCase().startsWith(zodis)) {
                System.out.println("žodis " + zodis + " rastas, indeksas: " + i + " key: " + yraNera);
                galimiVariantai.add(yraNera);
            }
        }
        System.out.println("viso žodyne žodžių: " + f.size());
        if (galimiVariantai.size() > 0) {
            System.out.println("Rasta " + galimiVariantai.size() + " žodžių su pradžios fragmentu: " + zodis);
            System.out.println(galimiVariantai);
        } else {
            System.out.println("Žodyne žodžių su {" + zodis + "} fragmentu nerasta");
        }
        return galimiVariantai;
    }

    // veikiantis metodas atspausdinti duomenis į ListView
    public void printListView() {
        TreeSet<String> ts = new TreeSet<>(Arrays.asList("pirmas", "antras", "trečias", "ketvirtas", "penktas", "šeštas", "septintas"));
        for (String item : ts) {
            visiZodynoZodziaiListView.getItems().addAll(item);
        }
    }

    // visi zodziai
//    public void irasytiZodziusIzoodyna() {
////        List<String> ss = new ArrayList<>();
////        ReadWriteData.writeDataIntoFile(ss, "z4");
//    }
    //        pastabos.setText(zodis + " vertimas: " + text1 + " žodyno dydis: " + zodynasTreeMap.size());//zodisTextField.getText());
//            zodisTextField.setText("");
//        if (zodis.equals(text1)) {
//            vertimasLabel.setText(text1);
//        } else {
//            vertimasLabel.setText("Žodyne tokio žodžio nėra!");
//            pastabos.setText(text1+", vertimas: "+text2);//zodisTextField.getText());
//        }

//    public void clearTextFieldOnEsc(KeyEvent event) { // kažkodėl nveikia
////        if (event.getCode() == KeyCode.ESCAPE) {
//            zodisTextField.setText("");
////        }
//    }

    public void kitasZodynas(ActionEvent event) {
        RadioButton pazymetasZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        String tugelis = pazymetasZodynas.getText();
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setContentText("Pasirinktas žodynas:\n   " + tugelis + "\nlaikinai veiks tik z1: " + z1.getText());
//        alert.show();
        zodisTextField.setPromptText(tugelis);
        laikinasTextField.setText("Pasirinktas žodynas: " + tugelis);
    }

    public void exitButon() {
        Platform.exit();
    }
}
