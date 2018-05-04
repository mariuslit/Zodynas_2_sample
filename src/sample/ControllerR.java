package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.ReadWriteData.ReadWriteData;
import sun.reflect.generics.tree.Tree;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class ControllerR {

    @FXML
    private Label title_LabelR;
    @FXML
    private Label sizeOfDictionaryBelowListView_LabelR;
    @FXML
    private TextField word_TextFieldR;
    @FXML
    private TextArea translation_TextAreaR;
    @FXML
    private ListView<String> allWords_ListViewR;

    private TreeMap<String, String> dictionaryTreeMapR = Controller.dictionaryTreeMap;//new TreeMap<>();
    private TreeMap<String, String> settingsTreeMapR = Controller.settingsTreeMap;//new TreeMap<>();
    private String selectedWord;

    public void initialize() { // ištryniau (URL location, ResourceBundle resources) ir suveikė
        word_TextFieldR.setText(Controller.info.getFragmentas()); // informacijos nuskaitymas iš Info klasės
//        settingsTreeMapR = Controller.settingsTreeMap; // ReadWriteData.readFile("settings");
        String defaultDictionaryId = settingsTreeMapR.get("default");
//        dictionaryTreeMapR = Controller.dictionaryTreeMap; // ReadWriteData.readFile(defaultDictionaryId);
        title_LabelR.setText("\"" + settingsTreeMapR.get(defaultDictionaryId) + "\" žodyno redagavimas");
        translation_TextAreaR.setText(Controller.info.getVertimas()); // informacijos nuskaitymas iš Info klasės
        fillInTheListViewR();
        allWords_ListViewR.setStyle("-fx-font-size: 14px;");
        allWords_ListViewR.setFixedCellSize(24);
    }

    // žodyno pavadinimo keitimas
    public void changeDictionaryNameR() {
        TextInputDialog dialog = new TextInputDialog(settingsTreeMapR.get(settingsTreeMapR.get("default")));
        dialog.setTitle("Žodyno pavadinomo keitimas");
        dialog.setHeaderText("Senasis žodyno pavadinimas: \"" + settingsTreeMapR.get(settingsTreeMapR.get("default")) + "\"");
        dialog.setContentText("Įveskite naują žodyno avadinimą:");
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newName = result.get().trim().replace("  ", " ");
            if (!newName.replaceAll(" ", "").equals("")) {
                title_LabelR.setText("\"" + newName + "\" žodyno redagavimas");
                settingsTreeMapR.put(settingsTreeMapR.get("default"), newName);
                ReadWriteData.writeFile(settingsTreeMapR, "settings");
                title_LabelR.setTextFill(Color.RED);
            }
        }
    }

    // naujas žodis
    public void addWordR() {
        String word = word_TextFieldR.getText();
        String transl = translation_TextAreaR.getText();

        // neteisingo žodyno pildymo filtrai
        if (word.length() == 0 || transl.length() == 0) { // tuščių ir bereikšmių žodžių filtras
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Klaidelė:");
            alert.setHeaderText(null);
            alert.setContentText("Ne visi laukai užpildyti");
            alert.show();
        } else {
            if (dictionaryTreeMapR.containsKey(word) && dictionaryTreeMapR.get(word).equals(transl)) { // vienodų žodžių filtras
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Klaidelė:");
                alert.setHeaderText(null);
                alert.setContentText("Toks žodis su tokiu pačiu vertimu šiame žodyne jau yra");
                alert.show();
            } else {
                dictionaryTreeMapR.put(word, transl);
                String activeDict = settingsTreeMapR.get("default");
                ReadWriteData.writeFile(dictionaryTreeMapR, activeDict);
                System.out.println("addWord: įdėtas naujas žodis į žodyną: " + activeDict + ", įrašomas į failą 'Zx.txt'");
                allWords_ListViewR.getItems().clear(); // duomenų trynimas iš ListView
                fillInTheListViewR(); // žodyno spausdinimas toliau
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Žodis išsaugotas");
                alert.show();
                title_LabelR.setTextFill(Color.RED);
            }
        }
    }

    // button [EDIT]
    public void editWordR() {
        if (selectedWord != null) {
            fillInTheFieldsR();
        }
    }

    // button [DELETE]
    public void deleteWordFromDictionaryR() {
        if (selectedWord != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Įspėjimas !!!");
            alert.setHeaderText("Ar tikrai norite ištrinti žodį iš žodyno?");
            alert.setContentText(selectedWord);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
//            System.out.println("žodis " + selectedWord + " trinamas");
                dictionaryTreeMapR.remove(selectedWord);
                allWords_ListViewR.getItems().clear(); // duomenų trynimas iš ListView
                fillInTheListViewR(); // žodyno spausdinimas toliau
                ReadWriteData.writeFile(dictionaryTreeMapR, settingsTreeMapR.get("default"));
                clearFieldsR();
                title_LabelR.setTextFill(Color.RED);
            }
        }
    }

    // reakcija į pelės paspaudimą ListViewR
    public void editWordOnDoubleClickMouseSelectionR(MouseEvent event) {
        String selectedItem = allWords_ListViewR.getSelectionModel().getSelectedItem(); // padaryta pagal Andriaus kodą
        selectedWord = selectedItem;
        if (event.getClickCount() == 2) {
            fillInTheFieldsR();
        }
    }

    private void fillInTheFieldsR() {
        String a = selectedWord;
        word_TextFieldR.setText(a);
        translation_TextAreaR.setText(dictionaryTreeMapR.get(a));
    }

    public void clearFieldsR() {
        word_TextFieldR.clear();
        translation_TextAreaR.clear();
        word_TextFieldR.requestFocus(); // padeda kursorių į input langelį
    }

    // veikiantis metodas atspausdinti duomenis į ListView
    private void fillInTheListViewR() {
        for (String item : dictionaryTreeMapR.keySet()) { // public Map'as
            allWords_ListViewR.getItems().addAll(item);
        }
        sizeOfDictionaryBelowListView_LabelR.setText(dictionaryTreeMapR.size() + "");
    }

    // exitas iš sampleR
    public void exitButonR() {
        Controller x = new Controller();
        x.closeStageR();
    }
}


























