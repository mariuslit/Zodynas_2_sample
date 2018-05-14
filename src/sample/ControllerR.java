package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.ReadWriteData.ReadWriteData;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;

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

    // naujas žodis
    public void addWordR() {
        String word = word_TextFieldR.getText();
        String transl = translation_TextAreaR.getText();
        // neteisingo žodyno pildymo filtrai
        if (word.length() == 0 || transl.length() == 0) { // tuščių ir bereikšmių žodžių filtras
            alertR.alerts(Alert.AlertType.ERROR, "Klaidos pranešimas", null, "Ne visi laukai užpildyti");
        } else { // 4 Alertai:
            // Aleret1 senas žodis + senas aprašymas:
            if (dictionaryTreeMapR.containsKey(word) && dictionaryTreeMapR.get(word).equals(transl)) {
                alertR.alerts(Alert.AlertType.ERROR, "Klaidos pranešimas", null, "Toks žodis su tokiu pačiu aprašymu šiame žodyne jau yra");
            } else {
                // Aleret2 senas žodis + naujas aprašymas:
                if (dictionaryTreeMapR.containsKey(word) && !dictionaryTreeMapR.get(word).equals(transl)) {
                    alertR.alerts(Alert.AlertType.WARNING, "Pranešimas", "Žodžio '" + word + "' aprašymas pakeistas:", " Senas aprašymas: '" + dictionaryTreeMapR.get(word_TextFieldR.getText()) + "'" + "\nNaujas aprašymas: '" + transl + "'");
                    dictionaryTreeMapR.put(word, transl);
                } else {
                    // Aleret3 naujas žodis + senas (pasikartojantis) aprašymas:
                    dictionaryTreeMapR.put(word, transl);
                    int x = 0;
                    StringBuilder s = new StringBuilder("\n");
                    for (String item : dictionaryTreeMapR.keySet()) {
                        if (dictionaryTreeMapR.get(item).equals(transl)) {
                            x++;
                            s.append("\n").append(x).append(". ").append(item);
                        }
                    }
                    System.out.println(x);
                    if (x > 1) {
//                        System.out.println(transl + " (value) kartojasi " + x + " kartų");
                        alertR.alerts(Alert.AlertType.WARNING, "Pranešimas", "Naujas žodis išsaugotas", "DĖMESIO !\n\nReikšmė '" + transl + "' žodyne kartojasi " + x + " kart." + s);
                    } else {
                        // Aleret4 naujas žodis + naujas aprašymas
                        alertR.alerts(Alert.AlertType.WARNING, "Pranešimas", "Naujas žodis išsaugotas", "Žodis: '" + word + "'" + "\n\nAprašymas: '" + transl + "'");
                    }
                }
            }
//            dictionaryTreeMapR.put(word, transl);
            String activeDict = settingsTreeMapR.get("default");
            ReadWriteData.writeFile(dictionaryTreeMapR, activeDict);
            fillListViewR(); // žodyno spausdinimas toliau
            title_LabelR.setTextFill(Color.RED);
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
            if (alertR.alerts(Alert.AlertType.CONFIRMATION, "Įspėjimas !!!", "Ar tikrai norite ištrinti žodį iš žodyno?", selectedWord)) {
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
        TreeSet<String> variant = new TreeSet<>(dictionaryTreeMapR.keySet());
        allWords_ListViewR.setItems(FXCollections.observableList(new ArrayList<>(variant))); // optimizuota
        sizeOfDictionaryBelowListView_LabelR.setText(dictionaryTreeMapR.size() + "");
    }

    // on press [UŽDARYTI] button, do this method
    public void exitButonR() {
        Controller.closeStageR();
    }
}
