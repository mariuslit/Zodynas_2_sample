package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.ReadWriteData.ReadWriteData;

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

    private TreeMap<String, String> dictionaryTreeMapR = Controller.dictionaryTreeMap;
    private TreeMap<String, String> settingsTreeMapR = Controller.settingsTreeMap;
    private String selectedWord;
    private Utils alertR = new Utils();

    public void initialize() { // ištryniau (URL location, ResourceBundle resources) ir suveikė
        word_TextFieldR.setText(Controller.info.getFragment()); // informacijos nuskaitymas iš Info klasės
        String defaultDictionaryId = settingsTreeMapR.get("default");
        title_LabelR.setText("\"" + settingsTreeMapR.get(defaultDictionaryId) + "\" žodyno redagavimas");
        translation_TextAreaR.setText(Controller.info.getTranslation()); // informacijos nuskaitymas iš Info klasės
        fillListViewR();
        allWords_ListViewR.setStyle("-fx-font-size: 14px;");
        allWords_ListViewR.setFixedCellSize(24);
        allWords_ListViewR.getSelectionModel().selectFirst();
        allWords_ListViewR.getSelectionModel().select(Controller.info.getFragment());
        selectedWord = null;
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
            String newName = result.get().trim();
            if (!newName.replaceAll(" ", "").equals("")) {
                title_LabelR.setText("\"" + newName + "\" žodyno redagavimas");
                settingsTreeMapR.put(settingsTreeMapR.get("default"), newName);
                ReadWriteData.writeFile(settingsTreeMapR, "settings");
                title_LabelR.setTextFill(Color.RED);
            }
        }
    }

    // nujas alert metodas
    // naujas žodis
    public void addWordR() {
        String word = word_TextFieldR.getText();
        String transl = translation_TextAreaR.getText();
        // neteisingo žodyno pildymo filtrai TODO sukurti įspėjimus: [verčiamas žodis nepakeistas senas vertimas -> naujas vertimas], [du vienoodi vertimai]
        if (word.length() == 0 || transl.length() == 0) { // tuščių ir bereikšmių žodžių filtras
            alertR.alertai(Alert.AlertType.ERROR, "Klaidelė", null, "Ne visi laukai užpildyti");
        } else {
            if (dictionaryTreeMapR.containsKey(word) && dictionaryTreeMapR.get(word).equals(transl)) { // vienodų žodžių filtras
                alertR.alertai(Alert.AlertType.ERROR, "Klaidelė:", null, "Toks žodis su tokiu pačiu vertimu šiame žodyne jau yra");
            } else {
                dictionaryTreeMapR.put(word, transl);
                String activeDict = settingsTreeMapR.get("default");
                ReadWriteData.writeFile(dictionaryTreeMapR, activeDict);
                System.out.println("addWord: įdėtas naujas žodis į žodyną: " + activeDict + ", įrašomas į failą 'Zx.txt'"); // TODO TRINTI
                fillListViewR(); // žodyno spausdinimas toliau
                alertR.alertai(Alert.AlertType.WARNING, null, null, "Žodis išsaugotas");
                title_LabelR.setTextFill(Color.RED);
            }
        }
    }

    // button [EDIT]
    public void editWordR() {
        if (selectedWord != null) {
            fillFieldsR();
        }
    }

    // button [DELETE]
    public void deleteWordFromDictionaryR() {
        if (selectedWord != null) {
            if (alertR.alertai(Alert.AlertType.CONFIRMATION, "Įspėjimas !!!", "Ar tikrai norite ištrinti žodį iš žodyno?", selectedWord)) {
                dictionaryTreeMapR.remove(selectedWord);
                fillListViewR();
                clearFieldsR();
                ReadWriteData.writeFile(dictionaryTreeMapR, settingsTreeMapR.get("default"));
                title_LabelR.setTextFill(Color.RED);
            }
            selectedWord = null;
        }
    }

    // reakcija į pelės paspaudimą ListViewR
    public void editWordOnDoubleClickMouseSelectionR(MouseEvent event) {
        String selectedItem = allWords_ListViewR.getSelectionModel().getSelectedItem(); // padaryta pagal Andriaus kodą
        selectedWord = selectedItem;
        if (event.getClickCount() == 2) {
            fillFieldsR();
        }
    }

    private void fillFieldsR() {
        String word = selectedWord;
        word_TextFieldR.setText(word);
        translation_TextAreaR.setText(dictionaryTreeMapR.get(word));
    }

    public void clearFieldsR() {
        word_TextFieldR.clear();
        translation_TextAreaR.clear();
        word_TextFieldR.requestFocus(); // padeda kursorių į input langelį
    }

    // veikiantis metodas atspausdinti duomenis į ListView
    private void fillListViewR() {
        allWords_ListViewR.getItems().clear(); // duomenų trynimas iš ListView
        for (String item : dictionaryTreeMapR.keySet()) { // public Map'as
            allWords_ListViewR.getItems().addAll(item);
        }
        sizeOfDictionaryBelowListView_LabelR.setText(dictionaryTreeMapR.size() + "");
    }

    // on press [UŽDARYTI] button, do this method
    public void exitButonR() {
//        Controller close = new Controller();
//        close.closeStageR();
        Controller.closeStageR();

        System.out.println("ConrtollerR: exitButonR()");
    }

    // Atliekami veiksmai prieš nutraukiant programos veikimą
    public void onCloseEventR() {
        System.out.println("atliktas veiksmas EXIT iš ControllerR");
    }

}
