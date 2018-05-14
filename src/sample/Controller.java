package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TextField fragment_TextField;
    @FXML
    private Label firstEquivalent_Label;
    @FXML
    private Label title_Label;
    @FXML
    private Label translation_Label;
    @FXML
    private Label countityOfVariantsBelowListView_Label;
    @FXML
    private Label sizeOfDictionaryBelowListView_Label;
    @FXML
    private ListView<String> allWords_ListView; // visada užpildomas iš dictionaryTreeMap Key reikšmėmis
    @FXML
    private ListView<String> variants_ListView;
    @FXML
    public ToggleGroup dictionarys_ToggleGroup;
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

    public static int y = 0; // skaitliukas man, TODO TRINTI
    public static Info info; // tarpinė klasė - informacijos pernešimui tarp Controller'ių

    public static TreeMap<String, String> dictionaryTreeMap = new TreeMap<>(); // pagrindinis Map - žodynas
    public static TreeMap<String, String> settingsTreeMap = new TreeMap<>();

    private Boolean first = false; // šokinėjimo tarp fragment <-> variant laukų valdymui

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settingsTreeMap = ReadWriteData.readFile("settings"); // nuskaitomas settings.txt failas
        // TODO: reikia panaikinti Info klasę, nes galima imti duomenis tiesiogiai iš Controller'ių klasių
        // TODO: tik Stage kol kas niekuo negaliu pakeisti
        doOnSelectRadioButton(settingsTreeMap.get("default")); // keičiamas zodynas į default stratup metu
        translate(fragment_TextField.getText());

        variants_ListView.setStyle("-fx-font-size: 16px;");
        variants_ListView.setFixedCellSize(28);
        allWords_ListView.setStyle("-fx-font-size: 14px;");
        allWords_ListView.setFixedCellSize(26);
    }

    // čia šio Controller kodas iškviečia ControllerR valdomą langą sampleR.fxml
    private void openNewStageR() throws Exception {

        // todo čia kažko per daug, reikia pratrinti
        FXMLLoader load = new FXMLLoader(getClass().getResource("sampleR.fxml"));
        Parent root = load.load();
        Stage stageR = new Stage();
        info = new Info(stageR);
        stageR.setTitle("Žodynas Ltit");
        stageR.setScene(new Scene(root));
        // todo

        stageR.initModality(Modality.APPLICATION_MODAL);
        pane.setDisable(true);
        stageR.showAndWait();
        pane.setDisable(false);

        RadioButton selectedDict = (RadioButton) dictionarys_ToggleGroup.getSelectedToggle();
        doOnSelectRadioButton(selectedDict.getId());
    }

    // inicijuojamas išControllerR
    public static void closeStageR() {
        Stage stage = info.getStageR();
        stage.close();
    }

    // aktyvaus žodyno keitimas
    public void nextDictionary(ActionEvent event) { // kreipiasi visi 6 radioButton į šį metodą onAction
        RadioButton selectedDict = (RadioButton) dictionarys_ToggleGroup.getSelectedToggle();
        doOnSelectRadioButton(selectedDict.getId());
        ReadWriteData.writeFile(settingsTreeMap, "settings"); // įrašo settings į failą
        translate(fragment_TextField.getText());
        allWords_ListView.getSelectionModel().select(0); // padeda kursorių į pirmą celę
    }

    // žodyno pakeitimas, toggel ID = failo vardas
    public void doOnSelectRadioButton(String toggelID_fileName) {
        switch (toggelID_fileName) {
            case "d1":
                dictionarys_ToggleGroup.selectToggle(d1);
                break;
            case "d2":
                dictionarys_ToggleGroup.selectToggle(d2);
                break;
            case "d3":
                dictionarys_ToggleGroup.selectToggle(d3);
                break;
            case "d4":
                dictionarys_ToggleGroup.selectToggle(d4);
                break;
            case "d5":
                dictionarys_ToggleGroup.selectToggle(d5);
                break;
            case "d6":
                dictionarys_ToggleGroup.selectToggle(d6);
                break;
        }

        // išsaugomas default žodynas "setings" faile
        settingsTreeMap.put("default", toggelID_fileName);

        // žodynų pavadinimų surašymas iš settings failo
        title_Label.setText("Žodžių paieška žodyne \"" + settingsTreeMap.get(toggelID_fileName) + "\"");
        d1.setText(settingsTreeMap.get("d1"));
        d2.setText(settingsTreeMap.get("d2"));
        d3.setText(settingsTreeMap.get("d3"));
        d4.setText(settingsTreeMap.get("d4"));
        d5.setText(settingsTreeMap.get("d5"));
        d6.setText(settingsTreeMap.get("d6"));

        // nuskaito naujo žodyno duomenis išfailo ir užkrauna žodyną į Map'ą
        dictionaryTreeMap = ReadWriteData.readFile(toggelID_fileName);

        // išvedamas zodyno turinys į ListView
        allWords_ListView.getItems().clear();// duomenų trynimas iš ListView

        // observableList naudojimas spausdinimui į ListView, vietoj įrašymo po vieną item per ciklus
        allWords_ListView.setItems(FXCollections.observableList(new ArrayList<>(dictionaryTreeMap.keySet()))); // optimizuota

        // pažymi pirmą celę
        allWords_ListView.getSelectionModel().selectFirst();

        sizeOfDictionaryBelowListView_Label.setText(dictionaryTreeMap.size() + "");
    } // end of doOnSelectRadioButton

    // vertimas
    private void translate(String fragment) {
        if (!fragment.equals("")) {
            TreeSet<String> variant = getEquivalentVariants(fragment.toLowerCase());
            variants_ListView.setItems(FXCollections.observableList(new ArrayList<>(variant))); // optimizuota
            countityOfVariantsBelowListView_Label.setText(variant.size() + "");
            if (!variant.isEmpty()) { // ne lygu nuliui
                String firstEquiv = variant.first();
                firstEquivalent_Label.setText(firstEquiv);
                translation_Label.setText(dictionaryTreeMap.get(firstEquiv));
            } else {
                clearFields();
            }
        } else {
            clearFields();
        }
        variants_ListView.getSelectionModel().selectFirst(); // padeda kursorių į pirmą celę
        info = new Info(dictionaryTreeMap, fragment_TextField.getText(),firstEquivalent_Label.getText());
        fillColorsToFields();
    }

    // tikrina ir pataiso fono spalvas
    private void fillColorsToFields() {
        if (!fragment_TextField.getText().toLowerCase().equals(firstEquivalent_Label.getText().toLowerCase())) {
            // melsva
            firstEquivalent_Label.setStyle("-fx-background-color: #b0e5ea");
            translation_Label.setStyle("-fx-background-color: #b0e5ea");
        } else {
            // žalsva
            firstEquivalent_Label.setStyle("-fx-background-color: #80ea9e");
            translation_Label.setStyle("-fx-background-color: #80ea9e");
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
    } /////////// end of <Back-End>

    public void onNewButtonPress() throws Exception {
        info = new Info(dictionaryTreeMap, fragment_TextField.getText(),"");
        openNewStageR();
    }

    public void onEditButtonPress() throws Exception {
        info = new Info(dictionaryTreeMap, firstEquivalent_Label.getText(), translation_Label.getText());
        openNewStageR();
    }

    // reakcija į pelės paspaudimą ant ListView_1 lauko
    public void doByClickingOnListView_1(MouseEvent event) {
        String selectedItem = "";
        if (variants_ListView.getSelectionModel().getSelectedItem() != null) {
            selectedItem = variants_ListView.getSelectionModel().getSelectedItem();
            firstEquivalent_Label.setText(selectedItem);
            translation_Label.setText(dictionaryTreeMap.get(selectedItem));
            fillColorsToFields();
        }
        if (event.getClickCount() == 2 && !selectedItem.equals("")) {
            onDoubleClickListView_1_2(selectedItem);
        }
    }

    // reakcija į pelės paspaudimą ant ListView_2 lauko
    public void doByClickingOnListView_2(MouseEvent event) {
        String selectedItem = "";
        if (allWords_ListView.getSelectionModel().getSelectedItem() != null) {
            selectedItem = allWords_ListView.getSelectionModel().getSelectedItem();
            firstEquivalent_Label.setText(selectedItem);
            translation_Label.setText(dictionaryTreeMap.get(selectedItem));
            fillColorsToFields();
        }
        if (event.getClickCount() == 2 && !selectedItem.equals("")) {
            onDoubleClickListView_1_2(selectedItem);
        }
    }

    // aptarnauja tik doByClickingOnListView_1 + _2
    private void onDoubleClickListView_1_2(String selectedItem) {
        fragment_TextField.setText(selectedItem);
        translate(selectedItem);
        fillColorsToFields();
    }

    // reakcija į klavišo paspaudimą žodžio fragmento įvedimo langelyje (interaktyvi paieška)
    public void doOnKeyPressFragmentField(KeyEvent event) {
        if (event.getCode().equals(KeyCode.DOWN) && !variants_ListView.getSelectionModel().isEmpty()) {
            variants_ListView.requestFocus();
            variants_ListView.getSelectionModel().selectFirst();
            variants_ListView.scrollTo(0);
            first = true;
        } else {
            translate(fragment_TextField.getText());
            fragment_TextField.requestFocus(); // padeda kursorių į input langelį
        }
    }

    // reakcija į klavišo paspaudimą ant ListView_1
    public void doOnKeyPressListView_1(KeyEvent event) {
        if ((event.getCode().equals(KeyCode.UP) && variants_ListView.getSelectionModel().isSelected(0))) {
            if (first) {
                fragment_TextField.requestFocus(); // padeda kursorių į input langelį
                fragment_TextField.positionCaret(fragment_TextField.getLength()); // padeda kursorių į teksto galą
                first = false;
            } else {
                first = true;
            }
        }
        if (variants_ListView.getSelectionModel().isSelected(1)) {
            first = false;
        }
        if (variants_ListView.getItems().size() > 0) { // jei ListView ne tuščias
            String a = variants_ListView.getSelectionModel().getSelectedItem();
            firstEquivalent_Label.setText(a);
            translation_Label.setText(dictionaryTreeMap.get(a));
        }
        fillColorsToFields();
    }

    // reakcija į klavišo paspaudimą ant ListView_2
    public void doOnKeyPressListView_2(KeyEvent event) {
        if (allWords_ListView.getFocusModel().getFocusedIndex() != -1) {
            String a = allWords_ListView.getSelectionModel().getSelectedItem();
            firstEquivalent_Label.setText(a);
            translation_Label.setText(dictionaryTreeMap.get(a));
        }
        fillColorsToFields();
    }

    public void doOnEscapePresPane(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            fragment_TextField.clear();
            clearFields();
            translate("");
            fragment_TextField.requestFocus(); // padeda kursorių į input langelį
        }
        if (event.getCode().equals(KeyCode.LEFT)) {
        }
    }

    public void clearAllFields() {
        fragment_TextField.clear();
        clearFields();
        translate("");
        fragment_TextField.requestFocus(); // padeda kursorių į input langelį
    }

    private void clearFields() {
        allWords_ListView.scrollTo(0);
        allWords_ListView.getSelectionModel().selectFirst();
        variants_ListView.getItems().clear(); // duomenų trynimas iš ListView
        firstEquivalent_Label.setText("");
        translation_Label.setText("");
    }

    public void onExitButtonPress() {
        Platform.exit();
    }
}
