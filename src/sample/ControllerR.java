package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import sample.ReadWriteFile.ReadWriteData;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class ControllerR {

    @FXML
    private Label antrasteLabelR;
    @FXML
    private Label zodynoDydisApaciojeLabelR;
    @FXML
    private TextField zodisTextFieldR;
    @FXML
    private TextArea vertimasTextAreaR;
    @FXML
    private ListView<String> visiListViewR;

    private Map<String, String> zodynasTreeMapR = new TreeMap<>();
    private Map<String, String> settingsLinkedMapR = new TreeMap<>();
    private String pazymetasZodisZodyneR;

    public void initialize() { // ištryniau (URL location, ResourceBundle resources) ir suveikė
        zodisTextFieldR.setText(Controller.info.getFragmentas()); // informacijos nuskaitymas iš Info klasės
        settingsLinkedMapR = ReadWriteData.readFile("settings");
        String zodynoDefaultId = settingsLinkedMapR.get("default");
        zodynasTreeMapR = ReadWriteData.readFile(zodynoDefaultId);
        String zodynoPavadinimas = settingsLinkedMapR.get(zodynoDefaultId);
        antrasteLabelR.setText("Žodyno \"" + zodynoPavadinimas + "\" redagavimas");
        vertimasTextAreaR.setText(Controller.info.getVertimas()); // informacijos nuskaitymas iš Info klasės
        printVisiListViewR();
    }

    // žodyno pavadinimo keitimas
    public void keistiZodynoPavadinimaR() {
        TextInputDialog dialog = new TextInputDialog(settingsLinkedMapR.get(settingsLinkedMapR.get("default")));
        dialog.setTitle("Žodyno pavadinomo keitimas");
        dialog.setHeaderText("Senasis žodynas: " + settingsLinkedMapR.get(settingsLinkedMapR.get("default")));
        dialog.setContentText("Įveskite naują žodyno avadinimą:");
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            antrasteLabelR.setText("Žodyno \"" + result.get() + "\" redagavimas");
            settingsLinkedMapR.put(settingsLinkedMapR.get("default"), result.get());
            ReadWriteData.writeFile(settingsLinkedMapR, "settings");
            System.out.println("result: " + result.get());
        }
    }

    // naujas žodis
    public void addWordR() {
        String a = zodisTextFieldR.getText();
        String b = vertimasTextAreaR.getText();

        // neteisingo žodyno pildymo filtrai
        if (zodynasTreeMapR.containsKey(a) && zodynasTreeMapR.get(a).equals(b)) { // vienodų žodžių filtras
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Toks žodis su tokiu pačiu vertimu šiame žodyne jau yra");
            alert.show();
        } else {
            if (a.length() == 0 || b.length() == 0) { // tuščių ir bereikšmių žodžių filtras
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Klaidelė");
                alert.setHeaderText(null);
                alert.setContentText("Neteisingai užpildyti laukai");
                alert.show();
            } else {
                zodynasTreeMapR.put(a, b);
                String aktyvusZodynas = settingsLinkedMapR.get("default");
                ReadWriteData.writeFile(zodynasTreeMapR, aktyvusZodynas);
                System.out.println("addWord: įdėtas naujas žodis į žodyną: " + aktyvusZodynas + ", įrašomas į failą 'Zx.txt'");
                visiListViewR.getItems().clear(); // duomenų trynimas iš ListView
                printVisiListViewR(); // žodyno spausdinimas toliau
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Žodis išsaugotas");
                alert.show();
            }
        }
    }

    // button [EDIT]
    public void editWordR() {
        if (pazymetasZodisZodyneR != null) {
            uzpildytiLaukus();
        }
    }

    // button [DELETE]
    public void deleteListViewItem() {
        if (pazymetasZodisZodyneR != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Įspėjimas !!!");
            alert.setHeaderText("Ar tikrai norite ištrinti žodį iš žodyno?");
            alert.setContentText(pazymetasZodisZodyneR);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
//            System.out.println("žodis " + pazymetasZodisZodyneR + " trinamas");
                zodynasTreeMapR.remove(pazymetasZodisZodyneR);
                visiListViewR.getItems().clear(); // duomenų trynimas iš ListView
                printVisiListViewR(); // žodyno spausdinimas toliau
                ReadWriteData.writeFile(zodynasTreeMapR, settingsLinkedMapR.get("default"));
                isvalytiViskaR();
            }
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
        zodisTextFieldR.setText(a);
        vertimasTextAreaR.setText(zodynasTreeMapR.get(a));
    }

    public void isvalytiViskaR() {
        zodisTextFieldR.clear();
        vertimasTextAreaR.clear();
//        zodisTextFieldR.setCursor(); // TODO: padėti kursorių į input langelį
    }

    // veikiantis metodas atspausdinti duomenis į ListView
    public void printVisiListViewR() {
        for (String item : zodynasTreeMapR.keySet()) { // public Map'as
            visiListViewR.getItems().addAll(item);
        }
        zodynoDydisApaciojeLabelR.setText(zodynasTreeMapR.size() + "");
    }

    // exitas iš sampleR
    public void exitButonR() {
        Controller x = new Controller();
        x.closeRstage();
    }
}


























