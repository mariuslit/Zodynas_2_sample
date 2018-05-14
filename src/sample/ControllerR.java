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
    private String selectedWord; // kintamasis skirtas patikrinti ar koks nors žodis yra baksteltas ant ListView
    private AlertsClass alertR = new AlertsClass();

    public void initialize() {
        word_TextFieldR.setText(Controller.wordR);
        String defaultDictionaryId = settingsTreeMapR.get("default");
        title_LabelR.setText("\"" + settingsTreeMapR.get(defaultDictionaryId) + "\" žodyno redagavimas");
        translation_TextAreaR.setText(Controller.translationR); // informacijos nuskaitymas iš Info klasės
        fillListViewR();
        allWords_ListViewR.setStyle("-fx-font-size: 14px;");
        allWords_ListViewR.setFixedCellSize(24);
        allWords_ListViewR.getSelectionModel().selectFirst();
        allWords_ListViewR.getSelectionModel().select(Controller.wordR);
        System.out.println(Controller.wordR);
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
                    alertR.alerts(Alert.AlertType.INFORMATION, "Pranešimas", "Žodžio '" + word + "' aprašymas pakeistas.", "Senas aprašymas:\n" + dictionaryTreeMapR.get(word_TextFieldR.getText()) + "\n\nNaujas aprašymas:\n" + transl);
                    dictionaryTreeMapR.put(word, transl);
                } else {
                    // Aleret3 naujas žodis + senas (pasikartojantis) aprašymas:
                    dictionaryTreeMapR.put(word, transl);
                    // TreeMap value dublikatų paieška
                    int x = 0;
                    StringBuilder s = new StringBuilder();
                    for (String item : dictionaryTreeMapR.keySet()) {
                        if (dictionaryTreeMapR.get(item).equals(transl)) {
                            x++;
                            s.append("\n").append(x).append(".   ").append(item);
                        }
                    }
                    if (x > 1) {
                        // Aleret4 naujas žodis + naujas aprašymas
                        alertR.alerts(Alert.AlertType.WARNING, "Pranešimas", "Naujas žodis išsaugotas", "DĖMESIO !\n\nŽodžio aprašymas\n" + transl + "\n\nvisame žodyne kartojasi " + x + " kart.\n\nJį turi šie žodžiai:" + s);
                    } else {
                        alertR.alerts(Alert.AlertType.INFORMATION, "Pranešimas", "Naujas žodis išsaugotas", "Žodis: \n" + word + "\n\nAprašymas: \n" + transl);
                    }
                }
            }
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
            if (alertR.alerts(Alert.AlertType.CONFIRMATION, "Įspėjimas !!!","Žodis ir jo aprašymas bus ištrintas iš žodyno.\nAr tikrai norite trinti?","Žodis:\n"+ selectedWord+"\n\nVertimas:\n"+dictionaryTreeMapR.get(selectedWord))) {
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
        String selectedItem = allWords_ListViewR.getSelectionModel().getSelectedItem();
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
        Controller.stageR.close();
    }
}
