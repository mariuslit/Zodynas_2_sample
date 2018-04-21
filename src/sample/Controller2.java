package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.ReadWriteFile.ReadWriteData;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class Controller2 {

    @FXML
    private Button keistiZodynoPavadinimaButtonR;
    @FXML
    private Button editButtonR;
    @FXML
    private Button deleteButtonR;
    @FXML
    private Label antrasteLabelR;
    @FXML
    private Label zodynoDydisApaciojeLabelR;
    @FXML
    private TextField zodynoPavadinimasTextFieldR;
    @FXML
    private TextField zodisTextFieldR;
    @FXML
    private TextField vertimasTextFieldR;
    @FXML
    private ListView<String> visiListViewR;

    private Map<String, String> zodynasTreeMapR = new TreeMap<>();
    private Map<String, String> settingsLinkedMapR = new TreeMap<>();
    String senasPavadinimasR;
    String pazymetasZodisZodyneR;

    public void initialize() { // ištryniau (URL location, ResourceBundle resources) ir suveikė
        zodisTextFieldR.setText(Controller.infoR_1.getFragmentas()); // informacijos nuskaitymas iš Informacija klasės
//        vertimasTextFieldR.setPromptText("naujas žodis");

        settingsLinkedMapR = ReadWriteData.readFile("settings");
        String zodynoPavadinimas = settingsLinkedMapR.get(settingsLinkedMapR.get("default"));
        zodynoPavadinimasTextFieldR.setText(zodynoPavadinimas);
        antrasteLabelR.setText("Žodyno \"" + zodynoPavadinimas + "\" redagavimas");

        vertimasTextFieldR.setText(Controller.infoR_1.getVertimas()); // informacijos nuskaitymas iš Informacija klasės
        System.out.println("iš Controller2: " + Controller.infoR_1.getFragmentas().toString());
        senasPavadinimasR = zodynoPavadinimasTextFieldR.getText();
        zodynasTreeMapR = ReadWriteData.readFile(settingsLinkedMapR.get("default").toString());
        printVisiListViewR();
    }

    public void addWordR() { // (ActionEvent event) teko išimti nes netinka šiai užduočiai
        String a = zodisTextFieldR.getText(); // žodis
        String b = vertimasTextFieldR.getText(); // vertimas
        String c = "";
        System.out.println("a=(" + a + ")" + a.length() + " b=(" + b + ")" + b.length());

        // neteisingo žodyno pildymo filtrai
        if (zodynasTreeMapR.containsKey(a) && zodynasTreeMapR.get(a).equals(b)) { // vienodų žodžių filtras
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Toks žodis su tokiu pačiu vertimu šiame žodyne jau yra");
            alert.show();
        } else {
            if (a.length() == 0 || b.length() == 0 || a.equals("-") || b.equals("-")) { // tuščių ir bereikšmių žodžių filtras
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Klaidelė");
                alert.setHeaderText(null);
//                alert.setHeaderText("Neteisingai užpildyti laukai");
                alert.setContentText("Neteisingai užpildyti laukai");
//                alert.setContentText("žodis - '" + a + "'\nvertimas - '" + b + "'");
                alert.show();
            } else {
                c = (a.replace("-", " ") + "-" + b.replace("-", " "));
                visiListViewR.getItems().clear(); // duomenų trynimas iš ListView
                visiListViewR.getItems().addAll(c); // naujas žodis įrašomas pirmoje eilutėje į ListView
                printVisiListViewR(); // žodyno spausdinimas toliau
                zodynasTreeMapR.put(a, b);
                String z = settingsLinkedMapR.get("default");
                ReadWriteData.writeFile(zodynasTreeMapR, z); // paskutinis naudotas aktyvus žodynas // TODO įrašymo testavimas,
//            ReadWriteData.writeFile(zodynasTreeMapR, z); // TODO naudoti kai veiks žodyno redagavimas
                System.out.println("įdėtas naujas žodis į žodyną: " + z + ", įrašomas į failą 'Zx.txt'");
            }
        }
    }

    public void isvalytiViskaR() {
        zodisTextFieldR.clear();
        vertimasTextFieldR.clear();
//        zodisTextField.setCursor(); // TODO: padėti kursorių į input langelį
    }

    public void keistiZodynoPavadinimaR() {
//        String pavadinimas = zodynoPavadinimasTextFieldR.getText().replace("-", " ");

        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle("Žodyno pavadinomo keitimas");
        dialog.setHeaderText("Senasis žodynas: " + settingsLinkedMapR.get(settingsLinkedMapR.get("default")));
        dialog.setContentText("Įveskite naują žodyno avadinimą:");
// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Naujas žodyno pavadinimas: " + result.get());
        }


//        if (!zodynoPavadinimasTextFieldR.isVisible()) {
//            // kai paspaudžiamas button keisti žodyno pavadinimą
//            zodynoPavadinimasTextFieldR.setVisible(true);
//            zodynoPavadinimasTextFieldR.selectAll(); // TODO neveikia nors anksčiau veikė
//            keistiZodynoPavadinimaButtonR.setText("Išsaugoti žodyno pavadinimą");
//            keistiZodynoPavadinimaButtonR.setTextFill(Color.RED);

//            settingsLinkedMapR = ReadWriteData.readFile("settings");
//        } else {
        // kai pavadinimas pakeistas sutvarkom GUI ir išsaugom settings failą
//            zodynoPavadinimasTextFieldR.setVisible(false);
        antrasteLabelR.setText("Žodyno \"" + result + "\" redagavimas");
//            keistiZodynoPavadinimaButtonR.setText("Keisti žodyno pavadinimą");
//            keistiZodynoPavadinimaButtonR.setTextFill(Color.BLACK);

        settingsLinkedMapR.put(settingsLinkedMapR.get("default").toString(), result.toString());
        ReadWriteData.writeFile(settingsLinkedMapR, "settings");
        System.out.println("pavadinimo keitimas: " + senasPavadinimasR + " -> " + result);
        senasPavadinimasR = zodynoPavadinimasTextFieldR.getText();
//        }
    }

    // veikiantis metodas atspausdinti duomenis į ListView
    public void printVisiListViewR() {
        for (String item : zodynasTreeMapR.keySet()) { // public Map'as
            visiListViewR.getItems().addAll(item);
//            visiListViewR.getItems().addAll(item + " - " + zodynasTreeMapR.get(item));
        }
        zodynoDydisApaciojeLabelR.setText("(" + zodynasTreeMapR.size() + ")");
    }

    // button [< EDIT] programavimas
    public void editWordR() {
        System.out.println(pazymetasZodisZodyneR);
        uzpildytiLaukus();

    }

    // button [DELETE >] programavimas
    public void deleteListViewItem() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Įspėjimas !!!");
        alert.setHeaderText("Ar tikrai norite ištrinti žodį iš žodyno?");
        alert.setContentText(pazymetasZodisZodyneR);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            System.out.println("žodis " + pazymetasZodisZodyneR + " trinamas");
            zodynasTreeMapR.remove(pazymetasZodisZodyneR);
        } else {
            // ... user chose CANCEL or closed the dialog
            System.out.println("žodis " + pazymetasZodisZodyneR + " NEtrinamas");
        }
    }

    // reakcija į pelės paspaudimą ListViewR
    public void editWordOnDoubleClickMouseSelection(MouseEvent event) {
        String selectedItem = visiListViewR.getSelectionModel().getSelectedItem(); // padaryta pagal Andriaus kodą
        pazymetasZodisZodyneR = selectedItem;
        if (event.getClickCount() == 2) {
            uzpildytiLaukus();
        }
    }

    public void uzpildytiLaukus() {
        String a = pazymetasZodisZodyneR;
        String b = zodynasTreeMapR.get(a);
        zodisTextFieldR.setText(a);
        vertimasTextFieldR.setText(b);
    }

    // exitas iš View-2
    public void exitButonR() {
        Platform.exit();
    }
}


























