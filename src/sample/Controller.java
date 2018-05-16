package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.ReadWriteData.ReadWriteData;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    public TextField fragmentTextField;
    @FXML
    private Label firstEquivalentLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label translationLabel;
    @FXML
    private Label countityOfVariantsBelowListViewLabel;
    @FXML
    private Label sizeOfDictionaryBelowListViewLabel;
    @FXML
    private ListView<String> allWordsListView; // visada užpildomas iš dictionaryTreeMap Key reikšmėmis
    @FXML
    private ListView<String> variantsListView;
    @FXML
    public ToggleGroup dictionarysToggleGroup;
    // RadioButton Id turi būti tokie patys kaip ir failų pavadinimai
    @FXML
    private RadioButton d1;
    @FXML
    private RadioButton d2;
    @FXML
    private RadioButton d3;
    @FXML
    private RadioButton d4;
    @FXML
    private RadioButton d5;
    @FXML
    private RadioButton d6;

    // ststic kintamieji skirti paruoti duomenis į kitą Controller
    public static Stage stageR;
    public static String wordR;
    public static String translationR;
    public static TreeMap<String, String> dictionaryTreeMap; // pagrindinis Map'as - žodynas
    public static TreeMap<String, String> settingsTreeMap;

    private Boolean isFirst = false; // šokinėjimo tarp [fragment] <-> [variant] laukų valdymui

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settingsTreeMap = ReadWriteData.readFile("settings"); // nuskaitomas settings.txt failas
        doOnSelectRadioButton(settingsTreeMap.get("default")); // keičiamas zodynas į default stratup metu
        translate(fragmentTextField.getText());
        variantsListView.setStyle("-fx-font-size: 16px;");
        variantsListView.setFixedCellSize(28);
        allWordsListView.setStyle("-fx-font-size: 14px;");
        allWordsListView.setFixedCellSize(26);

    }

    // čia šio Controller kodas iškviečia ControllerR valdomą langą sampleR.fxml
    private void openNewStageR() throws Exception {
        FXMLLoader load = new FXMLLoader(getClass().getResource("sampleR.fxml"));
        Parent root = load.load();
        stageR = new Stage();
        stageR.setTitle("Žodynas Ltit");
        stageR.setScene(new Scene(root));

        stageR.initModality(Modality.APPLICATION_MODAL);
        pane.setDisable(true);
        stageR.showAndWait();
        pane.setDisable(false);

        // refresh
        RadioButton selectedDict = (RadioButton) dictionarysToggleGroup.getSelectedToggle();
        doOnSelectRadioButton(selectedDict.getId());
        translate(fragmentTextField.getText());
    }

    // aktyvaus žodyno keitimas
    public void nextDictionary(ActionEvent event) { // kreipiasi visi 6 radioButton į šį metodą onAction
        RadioButton selectedDict = (RadioButton) dictionarysToggleGroup.getSelectedToggle();
        doOnSelectRadioButton(selectedDict.getId());
        ReadWriteData.writeFile(settingsTreeMap, "settings"); // įrašo settings į failą
        translate(fragmentTextField.getText());
        allWordsListView.getSelectionModel().select(0); // padeda kursorių į pirmą celę
    }

    // žodyno pakeitimas, toggel ID = failo vardas
    public void doOnSelectRadioButton(String toggelID_fileName) {
        switch (toggelID_fileName) {
            case "d1":
                dictionarysToggleGroup.selectToggle(d1);
                break;
            case "d2":
                dictionarysToggleGroup.selectToggle(d2);
                break;
            case "d3":
                dictionarysToggleGroup.selectToggle(d3);
                break;
            case "d4":
                dictionarysToggleGroup.selectToggle(d4);
                break;
            case "d5":
                dictionarysToggleGroup.selectToggle(d5);
                break;
            case "d6":
                dictionarysToggleGroup.selectToggle(d6);
                break;
        }

        // išsaugomas default žodynas "setings" faile
        settingsTreeMap.put("default", toggelID_fileName);

        // žodynų pavadinimų surašymas iš settings failo
        titleLabel.setText("Žodžių paieška žodyne \"" + settingsTreeMap.get(toggelID_fileName) + "\"");
        d1.setText(settingsTreeMap.get("d1"));
        d2.setText(settingsTreeMap.get("d2"));
        d3.setText(settingsTreeMap.get("d3"));
        d4.setText(settingsTreeMap.get("d4"));
        d5.setText(settingsTreeMap.get("d5"));
        d6.setText(settingsTreeMap.get("d6"));

        // nuskaito naujo žodyno duomenis išfailo ir užkrauna žodyną į Map'ą
        dictionaryTreeMap = ReadWriteData.readFile(toggelID_fileName);

        // išvedamas zodyno turinys į ListView
        allWordsListView.getItems().clear();// duomenų trynimas iš ListView

        // observableList naudojimas spausdinimui į ListView, vietoj įrašymo po vieną item per ciklus
        allWordsListView.setItems(FXCollections.observableList(new ArrayList<>(dictionaryTreeMap.keySet()))); // optimizuota

        // pažymi pirmą celę
        allWordsListView.getSelectionModel().selectFirst();

        sizeOfDictionaryBelowListViewLabel.setText(dictionaryTreeMap.size() + "");
    } // end of doOnSelectRadioButton

    // vertimas
    private void translate(String fragment) {
        if (!fragment.equals("")) {
            TreeSet<String> variant = getEquivalentVariants(fragment.toLowerCase());
            variantsListView.setItems(FXCollections.observableList(new ArrayList<>(variant))); // optimizuota
            countityOfVariantsBelowListViewLabel.setText(variant.size() + "");
            if (!variant.isEmpty()) { // ne lygu nuliui
                String firstEquiv = variant.first();
                firstEquivalentLabel.setText(firstEquiv);
                translationLabel.setText(dictionaryTreeMap.get(firstEquiv));
            } else {
                clearFields();
            }
        } else {
            clearFields();
        }
        variantsListView.getSelectionModel().selectFirst(); // padeda kursorių į pirmą celę
        fillColorsToFields();
    }

    // tikrina ir pataiso fono spalvas
    private void fillColorsToFields() {
        if (!fragmentTextField.getText().toLowerCase().equals(firstEquivalentLabel.getText().toLowerCase())) {
            // melsva
            firstEquivalentLabel.setStyle("-fx-background-color: #b0e5ea");
            translationLabel.setStyle("-fx-background-color: #b0e5ea");
        } else {
            // žalsva
            firstEquivalentLabel.setStyle("-fx-background-color: #80ea9e");
            translationLabel.setStyle("-fx-background-color: #80ea9e");
        }
    }

    // variantų paieškos varikliukas veikia puikiai
    private TreeSet<String> getEquivalentVariants(String fragment) {
        TreeSet<String> variant = new TreeSet<>(); // čia talpinamas atsakymas
        for (String item : dictionaryTreeMap.keySet()) {
            if (item.toLowerCase().startsWith(fragment)) { // true jei rado atitikmenį
                variant.add(item);
            }
        }
        return variant;
    } // end of <Back-End>

    public void onNewButtonPress() throws Exception {
        wordR = fragmentTextField.getText();
        translationR = "";
        openNewStageR();
    }

    public void onEditButtonPress() throws Exception {
        wordR = firstEquivalentLabel.getText();
        translationR = translationLabel.getText();
        openNewStageR();
    }

    // reakcija į pelės paspaudimą ant ListView_1 lauko
    public void doByClickingOnListView1(MouseEvent event) {
        String selectedItem = "";
        if (variantsListView.getSelectionModel().getSelectedItem() != null) {
            selectedItem = variantsListView.getSelectionModel().getSelectedItem();
            firstEquivalentLabel.setText(selectedItem);
            translationLabel.setText(dictionaryTreeMap.get(selectedItem));
            fillColorsToFields();
        }
        if (event.getClickCount() == 2 && !selectedItem.equals("")) {
            onDoubleClickListView12(selectedItem);
        }
    }

    // reakcija į pelės paspaudimą ant ListView_2 lauko
    public void doByClickingOnListView2(MouseEvent event) {
        String selectedItem = "";
        if (allWordsListView.getSelectionModel().getSelectedItem() != null) {
            selectedItem = allWordsListView.getSelectionModel().getSelectedItem();
            firstEquivalentLabel.setText(selectedItem);
            translationLabel.setText(dictionaryTreeMap.get(selectedItem));
            fillColorsToFields();
        }
        if (event.getClickCount() == 2 && !selectedItem.equals("")) {
            onDoubleClickListView12(selectedItem);
        }
    }

    // aptarnauja tik doByClickingOnListView1 + _2
    private void onDoubleClickListView12(String selectedItem) {
        fragmentTextField.setText(selectedItem);
        translate(selectedItem);
        fillColorsToFields();
    }

    // reakcija į klavišo paspaudimą žodžio fragmento įvedimo langelyje (interaktyvi paieška)
    public void doOnKeyPressFragmentField(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DOWN) && !variantsListView.getSelectionModel().isEmpty()) {
            variantsListView.requestFocus();
            variantsListView.getSelectionModel().selectFirst();
            variantsListView.scrollTo(0);
            isFirst = true;
        } else {
            translate(fragmentTextField.getText());
            fragmentTextField.requestFocus(); // padeda kursorių į input langelį
        }
    }

    // reakcija į klavišo paspaudimą ant ListView_1
    public void doOnKeyPressListView1(KeyEvent event) {
        if ((event.getCode().equals(KeyCode.UP) && variantsListView.getSelectionModel().isSelected(0))) {
            if (isFirst) {
                fragmentTextField.requestFocus(); // padeda kursorių į input langelį
                fragmentTextField.positionCaret(fragmentTextField.getLength()); // padeda kursorių į teksto galą
                isFirst = false;
            } else {
                isFirst = true;
            }
        }
        if (variantsListView.getSelectionModel().isSelected(1)) {
            isFirst = false;
        }
        if (variantsListView.getItems().size() > 0) { // jei ListView ne tuščias
            String a = variantsListView.getSelectionModel().getSelectedItem();
            firstEquivalentLabel.setText(a);
            translationLabel.setText(dictionaryTreeMap.get(a));
        }
        fillColorsToFields();
    }

    // reakcija į klavišo paspaudimą ant ListView_2
    public void doOnKeyPressListView2(KeyEvent event) {
        if (allWordsListView.getFocusModel().getFocusedIndex() != -1) {
            String a = allWordsListView.getSelectionModel().getSelectedItem();
            firstEquivalentLabel.setText(a);
            translationLabel.setText(dictionaryTreeMap.get(a));
        }
        fillColorsToFields();
    }

    public void doOnEscapePresPane(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            fragmentTextField.clear();
            clearFields();
            translate("");
            fragmentTextField.requestFocus(); // padeda kursorių į input langelį
        }
        if (event.getCode().equals(KeyCode.LEFT)) {
        }
    }

    public void clearAllFields() {
        fragmentTextField.clear();
        clearFields();
        translate("");
        fragmentTextField.requestFocus(); // padeda kursorių į input langelį
    }

    private void clearFields() {
        allWordsListView.scrollTo(0);
        allWordsListView.getSelectionModel().selectFirst();
        variantsListView.getItems().clear(); // duomenų trynimas iš ListView
        firstEquivalentLabel.setText("");
        translationLabel.setText("");
    }

    public void onExitButtonPress() {
        Platform.exit();
    }
}
