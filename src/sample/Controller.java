package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.ReadWriteFile.ReadWriteData;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    public static Informacija infoR_1;

    @FXML
    private TextField zodisTextField;
    @FXML
    private Label laikinasLabel;
    @FXML
    private Label pirmasAtitikmuoLabel;
    @FXML
    private Label vertimasLabel;
    @FXML
    private Label variantuKiekisApaciojeLabel;
    @FXML
    private Label zodynoDydisApaciojeLabel;
    @FXML
    private ListView<String> visiListView;
    @FXML
    private ListView<String> variantListView;
    @FXML
    private Button plusButton;
    @FXML
    private Button editButton;
    @FXML
    private ToggleGroup zodynuGrupe; // z1-z6
    @FXML
    private RadioButton z1;
    @FXML
    private RadioButton z2;
    @FXML
    private RadioButton z3;
    @FXML
    private RadioButton z4;
    @FXML
    private RadioButton z5;
    @FXML
    private RadioButton z6;

    private Map<String, String> zodynasTreeMap = new TreeMap<>();
    private Map<String, String> settingsLinkedMap = new TreeMap<>();

// TODO naujo lango iškvietimo tyrinėjimai
//    public void onPlus() {
//        editButton.setVisible(editButton.isVisible() == true ? false : true);
//        newWindow.show();
//    }

    // TODO neveikia SampleR lango iškvietimas ir valdymas per Controller, vis dar kreipiasi į Controller2
    {
    }

    // čia šio Controller kodas iškviečia Controller2 valdymo langą sample2.fxml
    @FXML
    public void openNewView() throws Exception {
//        Platform.isImplicitExit(); // TODO reika kad atidarius langą, tėvinis langa būtų užrkaintas
        Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Žodynas Ltit (žodyno redagavimo režimas)");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
//        plusButton.setDisable(true); // veikia, bet atjungtas kol nesugalvosiu kur panaudoti
    }

    public void onButtonPress_plus() throws Exception {
        infoR_1 = new Informacija(zodynasTreeMap, zodisTextField.getText());
        System.out.println("Controller: " + zodisTextField.getText() + " -> " + infoR_1.getFragmentas());
        openNewView();
    }

    public void onButtonPress_edit() throws Exception {
        infoR_1 = new Informacija(zodynasTreeMap, pirmasAtitikmuoLabel.getText(), vertimasLabel.getText()); // jei reikės papildomo parametro, settingsLinkedMap.get("default"));
        System.out.println("Controller: " + zodisTextField.getText() + " -> " + infoR_1.getFragmentas());
        openNewView();
    }

    // ANDRIAUS KOMENTARAS: "Cia nuskaitai faila ir sudeti i map. Sitas metodas leidziamas ant star upo"
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        students = FXCollections.observableList(createDummyStudents());
        settingsLinkedMap = ReadWriteData.readFile("settings"); // nuskaitomas settings.txt
        zodynasSelect(settingsLinkedMap.get("default")); //keičiamas zodynas į default stratup metu
        versk(zodisTextField.getText());
//        infoR_1 = new Informacija(zodynasTreeMap, zodisTextField.getText());
//        System.out.println(infoR_1.getFragmentas());
    }

    // aktyvaus žodyno keitimas
    public void kitasZodynas(ActionEvent event) {
        RadioButton pazymetasZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        zodynasSelect(pazymetasZodynas.getId());
        versk(zodisTextField.getText());
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setContentText("Pasirinktas žodynas:\n   " + pazymetasZodynas.getId() + "\nlaikinai veiks tik z1: " + z1.getText());
//        alert.show();
    }

    // žodyno pakeitima
    public void zodynasSelect(String toggelID) {
        String failoVardas = toggelID;
        switch (toggelID) {
            case "z1":
                zodynuGrupe.selectToggle(z1);
                break;
            case "z2":
                zodynuGrupe.selectToggle(z2);
                break;
            case "z3":
                zodynuGrupe.selectToggle(z3);
                break;
            case "z4":
                zodynuGrupe.selectToggle(z4);
                break;
            case "z5":
                zodynuGrupe.selectToggle(z5);
                break;
            case "z6":
                zodynuGrupe.selectToggle(z6);
                break;
        }

        // žodynų pavadinimai iš failo
        z1.setText(settingsLinkedMap.get("z1"));
        z2.setText(settingsLinkedMap.get("z2"));
        z3.setText(settingsLinkedMap.get("z3"));
        z4.setText(settingsLinkedMap.get("z4"));
        z5.setText(settingsLinkedMap.get("z5"));
        z6.setText(settingsLinkedMap.get("z6"));

        // išsaugomas default žodynas "setings" faile
        settingsLinkedMap.put("default", failoVardas);
        ReadWriteData.writeFile(settingsLinkedMap, "settings"); // įrašo settings į failą

        // nuskaito ir išsaugo duomenis "Žodynas"
        zodynasTreeMap = ReadWriteData.readFile(failoVardas); // užkrauna žodyną
        ReadWriteData.writeFile(zodynasTreeMap, "laikinas"); // paskutinis naudotas aktyvus žodynas // TODO įrašymo testavimas, naudoti kai veiks žodyno redagavimas

        // išvedamas zodyno turinys į ListView
        TreeSet x = new TreeSet<>();
        printVisiListView(x, "visi");
        RadioButton aktyvusZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
    } // end of zodynasSelect

    // reakcija į pelės paspaudimą ListView, vis tik payko su vienu metodu
    public void click1list(MouseEvent event) {
        String selectedItem = variantListView.getSelectionModel().getSelectedItem(); // padaryta pagal Andriaus kodą
        if (event.getSource().toString().equals("ListView[id=visiListView, styleClass=list-view]")) {
            selectedItem = visiListView.getSelectionModel().getSelectedItem();
        }
        pirmasAtitikmuoLabel.setText(selectedItem);
        vertimasLabel.setText(zodynasTreeMap.get(selectedItem));
        laikinasLabel.setText(event.getSource().toString());
    }

    // reakcija į klavišo paspaudimą žodžio fragmento įvedimo langelyje (interaktyvi paieška)
    public void paspaudimas(KeyEvent event) {
        versk(zodisTextField.getText());
    }

    public void versk(String fragmentas) {
        if ((!fragmentas.equals(""))) { // ne lygu nuliui
            fragmentas = fragmentas.toLowerCase(); // nuskaito verčiama žodį
            TreeSet<String> variantai = new TreeSet(gautiAtitikmenuVariantus(fragmentas));
            printVisiListView(variantai, "variantai");
            if (variantai.size() != 0) { // ne lygu nuliui
                String pirmasAtitikmuo = variantai.first().toString();
                pirmasAtitikmuoLabel.setText(pirmasAtitikmuo);
//                pirmasAtitikmuoLabel.setTextFill(Color.GREEN);
                vertimasLabel.setText(zodynasTreeMap.get(pirmasAtitikmuo));
            } else {
                isvalyti();
            }
        } else {
            isvalyti();
        }
        infoR_1 = new Informacija(zodynasTreeMap, zodisTextField.getText());
        System.out.println("Controller: " + zodisTextField.getText() + " -> " + infoR_1.getFragmentas());
        if (!fragmentas.toLowerCase().equals(pirmasAtitikmuoLabel.getText().toLowerCase())) {
            plusButton.setDisable(false);
            pirmasAtitikmuoLabel.setStyle("-fx-background-color: #b0e5ea");
//            vertimasLabel.setTextFill(Color.BLUE);
            vertimasLabel.setStyle("-fx-background-color: #b0e5ea");
        } else {
            plusButton.setDisable(true);
            pirmasAtitikmuoLabel.setStyle("-fx-background-color: #80ea9e");
//            vertimasLabel.setTextFill(Color.GREEN);
            vertimasLabel.setStyle("-fx-background-color: #80ea9e");
        }
//        edit mygtuko blokavimas jei reikia
//        if (pirmasAtitikmuoLabel.getText().equals("-") && vertimasLabel.getText().equals("-")) {
//            editButton.setDisable(true);
//        } else {
//            editButton.setDisable(false);
//        }
    }

    // variantų paieškos varikliukas veikia puikiai
    public TreeSet<String> gautiAtitikmenuVariantus(String fragmentas) {
        TreeSet<String> variantai = new TreeSet<>(); // čia talpinamas atsakymas
        for (String item : zodynasTreeMap.keySet()) {
            if (item.toLowerCase().startsWith(fragmentas)) { // true jei rado atitikmenį
                variantai.add(item);
            }
        }
        return variantai;
    }

    // veikiantis metodas atspausdinti duomenis į ListView
    public void printVisiListView(TreeSet<String> variantai, String kurisSarasas) {
        switch (kurisSarasas) {
            case "visi":
                visiListView.getItems().clear();// duomenų trynimas iš ListView
                for (String item : zodynasTreeMap.keySet()) { // public Map'as
                    visiListView.getItems().addAll(item);
                }
                zodynoDydisApaciojeLabel.setText("(" + zodynasTreeMap.size() + ")");
                break;
            case "variantai":
                variantListView.getItems().clear();// duomenų trynimas iš ListView
                for (String item : variantai) {
                    variantListView.getItems().addAll(item);
                }
                variantuKiekisApaciojeLabel.setText("(" + variantai.size() + ")");
                break;
        }
    }

    public void isvalytiViska() {
        zodisTextField.clear();
//        zodisTextField.setCursor(); // TODO: padėti kursorių į input langelį
        isvalyti();
    }

    public void isvalyti() {
        variantListView.getItems().clear();// duomenų trynimas iš ListView
        pirmasAtitikmuoLabel.setText("-");
        vertimasLabel.setText("-");
    }

    public void exitButon() {
        Platform.exit();
    }

    public void onCloseEvent() {
        System.out.println("Tis methos will be call on close event!!!!");
    }
}
