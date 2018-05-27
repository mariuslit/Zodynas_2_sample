package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import sample.ReadWriteData.ReadWriteData;

import java.io.EOFException;
import java.util.*;

public class ControllerR {

    @FXML
    private Label titleLabelR;
    @FXML
    private Label sizeOfDictionaryBelowListViewLabelR;
    @FXML
    private TextField wordTextFieldR;
    @FXML
    private TextArea translationTextAreaR;
    @FXML
    private ListView<String> allWordsListViewR;

    private TreeMap<String, String> dictionaryTreeMapR = Controller.dictionaryTreeMap;
    private TreeMap<String, String> settingsTreeMapR = Controller.settingsTreeMap;
    private String selectedWord; // kintamasis skirtas patikrinti ar koks nors žodis yra baksteltas ant ListView
    private AlertsClass alert = new AlertsClass();

    public void initialize() {
        wordTextFieldR.setText(Controller.wordR);
        String defaultDictionaryId = settingsTreeMapR.get("default");
        titleLabelR.setText("\"" + settingsTreeMapR.get(defaultDictionaryId) + "\" žodyno redagavimas");
        translationTextAreaR.setText(Controller.translationR); // informacijos nuskaitymas iš Info klasės
        fillListViewR();
        allWordsListViewR.setStyle("-fx-font-size: 14px;");
        allWordsListViewR.setFixedCellSize(26);
        allWordsListViewR.getSelectionModel().selectFirst();
        allWordsListViewR.getSelectionModel().select(Controller.wordR);
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

            // patikrina ar vartotojas neįrašė tuščio pavadinimo - vieno ar kelių tuščių tarpų.
            if (!newName.replaceAll(" ", "").equals("")) {
                titleLabelR.setText("\"" + newName + "\" žodyno redagavimas");
                settingsTreeMapR.put(settingsTreeMapR.get("default"), newName);
                ReadWriteData.writeFile(settingsTreeMapR, "settings");
                titleLabelR.setTextFill(Color.RED);
            }
        }
    }


    // button [IŠSAUGOTI ŽODYNE]
    public void addWordR() {
        String newWord = wordTextFieldR.getText();
        boolean isNewWord = !dictionaryTreeMapR.containsKey(newWord);
        String oldTranslation = dictionaryTreeMapR.get(newWord);
        String newTranslation = translationTextAreaR.getText();

        // pranešimas jeigu laukai tušti (neteisingo pildymo filtras)
        if (newWord.replace(" ", "").equals("") || newTranslation.replace(" ", "").equals("")) {

            alert.alert(Alert.AlertType.ERROR, "KLAIDA\n\n Prašome užpildyti vsus laukus!");
            return;
        }

        //  jeigu žodis ir reikšmė kartojasi - nieko nedaryti (niekas nepakeista)
        if (!isNewWord && oldTranslation.equals(newTranslation)) {
            alert.alert(Alert.AlertType.INFORMATION, "Nėra jokių pakeitimų!");
            return;
        }

        // pakeitimai įrašomi į žodyną
        dictionaryTreeMapR.put(newWord, newTranslation);

        // alertų teksto formavimas
        String content = "";
        String contentPlusDuplicates = "";

        // dublikatų paieška tarp žodyno reikšmių
        ArrayList<String> duplicatesList = getDuplicatesList(newWord, newTranslation); // dublikatų sąrašas
        int duplicatesCountity = duplicatesList.size(); // dublikatų kiekis
        boolean isDuplicated = duplicatesCountity > 1; // ar yra dublikatų

        // papildomas įrašas alerte jeigu dublikatų yra
        if (isDuplicated) {
            contentPlusDuplicates = "\n" +
                    "\n----------------------" +
                    "\nDĖMESIO !" +
                    "\n" +
                    "\nŽodžio aprašymas:" +
                    "\n" + newTranslation +
                    "\n" +
                    "\nŽODYNE KARTOJASI " + duplicatesCountity + " KART." +
                    "\n" +
                    "\nJį turi šie žodžiai:" +
                    duplicatesList.toString().replaceAll("\\[|\\]", "").replaceAll(", ", ""); // pašalina simbolius: [],
        }

        //  jeigu žodis kartojasi o reikšmė nesikartoja - tai tik rodžio paredagavimas
        if (!isNewWord && !oldTranslation.equals(newTranslation)) {
            content = "ŽODŽIO" +
                    "\n" + newWord +
                    "\n" +
                    "\nAPRAŠYMAS PAKEISTAS." +
                    "\n" +
                    "\nSenas aprašymas:" +
                    "\n" + oldTranslation +
                    "\n" +
                    "\nNaujas aprašymas:" +
                    "\n" + newTranslation +
                    contentPlusDuplicates;
            alert.alert(Alert.AlertType.INFORMATION, content);
            saveAndRefresh();
            return;
        }

        //  NAUJAS ŽODIS
        if (isNewWord) {
            content = "NAUJAS ŽODIS IŠSAUGOTAS." +
                    "\n" +
                    "\nŽodis: " +
                    "\n" + newWord + "" +
                    "\n" +
                    "\nAprašymas: " +
                    "\n" + newTranslation +
                    contentPlusDuplicates;
            alert.alert(Alert.AlertType.INFORMATION, content);
            saveAndRefresh();
        }
    }


    private void saveAndRefresh() {
        // išsaugo žodyną faile
        ReadWriteData.writeFile(dictionaryTreeMapR, settingsTreeMapR.get("default"));

        // refresh ListView
        fillListViewR();
        titleLabelR.setTextFill(Color.RED);
    }

    // dublikatų paieška tarp žodyno reikšmių
    private ArrayList<String> getDuplicatesList(String key, String transl) {
        ArrayList<String> list = new ArrayList<>();
        TreeMap<String, String> treeMap = dictionaryTreeMapR;
//        treeMap.put(key, transl);
        int x = 0;
        for (String item : treeMap.keySet()) {
            if (treeMap.get(item).equals(transl)) {
                list.add("\n" + ++x + ".   " + item);
            }
        }
        return list;
    }


    // button [REDAGUOTI]
    public void editWordR() {// neveiks, jeigu joks žodis nepažymėtas
        if (selectedWord != null) {
            fillFieldsR();
        }
    }

    // button [TRINTI]
    public void deleteWordFromDictionaryR() {

        // nutraukia veiksmus jeigu joks žodis nepažymėtas
        if (selectedWord == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("PRANEŠIMAS");
        alert.setHeaderText(null);
        String content = "Žodis ir jo aprašymas bus ištrintas iš žodyno." +
                "\n" +
                "\nAR TIKRAI NORITE TRINTI ?" +
                "\n" +
                "\nŽodis:" +
                "\n" + selectedWord + "" +
                "\n" +
                "\nAprašymas:" +
                "\n" + dictionaryTreeMapR.get(selectedWord);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dictionaryTreeMapR.remove(selectedWord);
            fillListViewR();
            clearFieldsR();
            ReadWriteData.writeFile(dictionaryTreeMapR, settingsTreeMapR.get("default"));
            titleLabelR.setTextFill(Color.RED);
        }

        selectedWord = null;

    }

    // reakcija į pelės paspaudimą ListViewR
    public void editWordOnDoubleClickMouseSelectionR(MouseEvent event) {
        try {
            String selectedItem = allWordsListViewR.getSelectionModel().getSelectedItem();
            selectedWord = selectedItem;
            if (event.getClickCount() == 2) {
                fillFieldsR();
            }
        } catch (Exception e) {
            System.out.println("klaida");
        }
    }

    private void fillFieldsR() {
        String word = selectedWord;
        wordTextFieldR.setText(word);
        translationTextAreaR.setText(dictionaryTreeMapR.get(word));
    }

    public void clearFieldsR() {
        wordTextFieldR.clear();
        translationTextAreaR.clear();
        wordTextFieldR.requestFocus(); // padeda kursorių į input langelį
    }

    // veikiantis metodas atspausdinti duomenis į ListView
    private void fillListViewR() {
        TreeSet<String> variant = new TreeSet<>(dictionaryTreeMapR.keySet());
        allWordsListViewR.setItems(FXCollections.observableList(new ArrayList<>(variant))); // optimizuota
        sizeOfDictionaryBelowListViewLabelR.setText(dictionaryTreeMapR.size() + "");
    }

    // on press [UŽDARYTI] button, do this method
    public void exitButonR() {
        Controller.stageR.close();
    }
}
