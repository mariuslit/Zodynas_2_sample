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
import javafx.stage.Stage;
import sample.ReadWriteData.ReadWriteData;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

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
    private ToggleGroup dictionarys_ToggleGroup;
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

    public static int y = ReadWriteData.x; // skaitliukas man, TODO vėliau ištrinti

    public static Info info;

    public static TreeMap<String, String> dictionaryTreeMap = new TreeMap<>();
    public static ObservableList<String> obsList; // naudojamas tik tam kad įsisavinčiau darbą su observableList, aplamai nelabai naudingas šiame projekte

    public static TreeMap<String, String> settingsTreeMap = new TreeMap<>();

    private Boolean first = false; // šokinėjimo tarp fragment <-> variant laukų valdymui


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settingsTreeMap = ReadWriteData.readFile("settings"); // nuskaitomas settings.txt
        // TODO: 2018-05-04 reikia panaikinti Info klasę, nes galima imti duomenis tiesiogiai iš Controler'ių klasių
        System.out.println(++ReadWriteData.x + " " + ReadWriteData.getObsList("settings") + " " + ++ReadWriteData.x);
        doOnSelectRadioButton(settingsTreeMap.get("default")); // keičiamas zodynas į default stratup metu
        translate(fragment_TextField.getText());

        variants_ListView.setStyle("-fx-font-size: 16px;");
        variants_ListView.setFixedCellSize(28);
        allWords_ListView.setStyle("-fx-font-size: 14px;");
        allWords_ListView.setFixedCellSize(24);
    }

    // čia šio Controller kodas iškviečia ControllerR valdomą langą sampleR.fxml
//        Platform.isImplicitExit(); // TODO reika kad atidarius langą, tėvinis langa liktų užrkaintas
    @FXML
    private void openNewStageR() throws Exception {

        FXMLLoader load = new FXMLLoader(getClass().getResource("sampleR.fxml")); // perkopijuota iš Main
        Parent root = load.load(); // perkopijuota iš Main
//        Parent root = FXMLLoader.load(getClass().getResource("sampleR.fxml"));
        Stage stageR = new Stage();
        stageR.setTitle("Žodynas Ltit");
//        Controller controllerR = load.getController(); // perkopijuota iš Main
//        exitButton.setOnAction(e -> windowClose());
        stageR.setScene(new Scene(root));
        stageR.show();
        info = new Info(stageR);
    }

    @FXML
    public void closeStageR() {
//        System.out.println("kreipinys į Controler iš antro view");
        Stage stage = info.getStage();
        stage.close();
        doSomething();
    }

    // TODO padaryti kad atliktų kokius nors veiksmus po sampleR uždarymo
    // TODO Andriaus komentaras: "Na taip neiskviesi metodo. ir state turi pasiimti controleri ir is jo kviesti PUBLIC metoda!!!! #7"
    private void doSomething() {
        System.out.println("Uždarius children langą atlieka ne visus veiksmus");
//        doOnSelectRadioButton(settingsTreeMap.get("default"));
//        translate(fragment_TextField.getText());
    }

    // aktyvaus žodyno keitimas
    public void nextDictionary(ActionEvent event) { // kreipiasi visi 6 radioButton į šį metodą onAction
//        settingsTreeMap = ReadWriteData.readFile("settings"); // nuskaitomas settings.txt
        RadioButton selectedDict = (RadioButton) dictionarys_ToggleGroup.getSelectedToggle();
        doOnSelectRadioButton(selectedDict.getId());
        ReadWriteData.writeFile(settingsTreeMap, "settings"); // įrašo settings į failą
        translate(fragment_TextField.getText());
        allWords_ListView.getSelectionModel().select(0); // padeda kursorių į pirmą celę
    }

    // žodyno pakeitimas, toggel ID = failo vardas
    private void doOnSelectRadioButton(String toggelID_fileName) {
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

        // nuskaito duomenis išfailo ir  užkrauna žodyną į Map'ą
        dictionaryTreeMap = ReadWriteData.readFile(toggelID_fileName);

        // išvedamas zodyno turinys į ListView
        allWords_ListView.getItems().clear();// duomenų trynimas iš ListView

        // TODO observableList naudojimas spausdinimui į ListView, vietoj įrašymo po vieną item
        List<String> str = new ArrayList<>(); // List'o sukūrimas kad galėčiau įrašyti į observableList
        for (String item : dictionaryTreeMap.keySet()) {
//            allWords_ListView.getItems().addAll(item); // spausdina po vieną eilutę į ListView -> pakeičiau su observableList
            str.add(item); // List'o pildymas kad galėčiau įrašyti į observableList
        }
        obsList = FXCollections.observableList(str); // sukuriamas observableList
        allWords_ListView.setItems(obsList); // atspausdina visą observableList sąrašą į ListView be ciklų

        allWords_ListView.getSelectionModel().selectFirst(); // padeda kursorių į pirmą celę
        sizeOfDictionaryBelowListView_Label.setText(dictionaryTreeMap.size() + "");
    } // end of doOnSelectRadioButton

    // vertimas
    private void translate(String fragment) {
        if ((!fragment.equals(""))) {
            TreeSet<String> variant = getEquivalentVariants(fragment.toLowerCase()); // Andriaus komentaras: "naudoti interface SET! #8" [kol kas neveikia]
            // spausdinti variantue į ListView
            variants_ListView.getItems().clear();// duomenų trynimas iš ListView
            for (String item : variant) {
                variants_ListView.getItems().addAll(item);
            }
            countityOfVariantsBelowListView_Label.setText(variant.size() + "");
            if (variant.size() != 0) { // ne lygu nuliui
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
        info = new Info(dictionaryTreeMap, fragment_TextField.getText());
//        System.out.println("Controller: " + fragment_TextField.getText() + " -> " + info.getFragmentas());
        fillColorsToFields();
    }

    // tikrina ir pataiso spalvas
    private void fillColorsToFields() {
        // fono spalvos
        if (!fragment_TextField.getText().toLowerCase().equals(firstEquivalent_Label.getText().toLowerCase())) {
            firstEquivalent_Label.setStyle("-fx-background-color: #b0e5ea");
            translation_Label.setStyle("-fx-background-color: #b0e5ea");
        } else {
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
    } // todo ////////////////////////////////////////////////////// end of <Back-End>

    public void onNewButtonPress() throws Exception {
        info = new Info(dictionaryTreeMap, fragment_TextField.getText());
        openNewStageR();
    }

    public void onEditButtonPress() throws Exception {
        info = new Info(dictionaryTreeMap, firstEquivalent_Label.getText(), translation_Label.getText()); // jei reikės papildomo parametro, settingsTreeMap.get("default"));
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
        if (event.getCode().isArrowKey()/*equals(KeyCode.RIGHT)*/) {
            System.out.println(++y + " r"+event.getCode().getDeclaringClass());// TODO: 2018-05-04 neveikia kai spaudžiamas dešinėn
        }
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
    }

    // reakcija į klavišo paspaudimą ant ListView_2
    public void doOnKeyPressListView_2(KeyEvent event) {
        if (allWords_ListView.getFocusModel().getFocusedIndex() != -1) {
            String a = allWords_ListView.getSelectionModel().getSelectedItem();
            firstEquivalent_Label.setText(a);
            translation_Label.setText(dictionaryTreeMap.get(a));
        }
    }

    public void doOnEscapePresPane(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            fragment_TextField.clear();
            clearFields();
            translate("");
            fragment_TextField.requestFocus(); // padeda kursorių į input langelį
        }
        if (event.getCode().equals(KeyCode.LEFT)) {// TODO: 2018-05-04
            System.out.println(++y + " kairėn");// TODO: 2018-05-04
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

//    // Atliekami veiksmai prieš nutraukiant programos veikimą
//    public void onCloseEvent() {
//        System.out.println("Atliekami veiksmai prieš nutraukiant programos veikimą");
//    }
}
