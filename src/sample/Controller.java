package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.ReadWriteFile.ReadWriteData;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

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

    public static Informacija info;
    private Map<String, String> zodynasTreeMap = new TreeMap<>();
    private Map<String, String> settingsLinkedMap = new TreeMap<>();

    // čia šio Controller kodas iškviečia Controller2 valdymo langą sample2.fxml
    @FXML
    public void openNewView() throws Exception {
//        Platform.isImplicitExit(); // TODO reika kad atidarius langą, tėvinis langa būtų užrkaintas
        FXMLLoader load = new FXMLLoader(getClass().getResource("sample2.fxml")); // perkopijuota iš Main
        Parent root = load.load(); // perkopijuota iš Main
//        Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Žodynas Ltit");
//        Controller controller2 = load2.getController(); // perkopijuota iš Main
//        exitButton.setOnAction(e -> windowClose());
        secondaryStage.setScene(new Scene(root));
        secondaryStage.show();
    }

    public void onNewButtonPress() throws Exception {
        info = new Informacija(zodynasTreeMap, zodisTextField.getText());
        openNewView();
    }

    public void onEditButtonPress() throws Exception {
        info = new Informacija(zodynasTreeMap, pirmasAtitikmuoLabel.getText(), vertimasLabel.getText()); // jei reikės papildomo parametro, settingsLinkedMap.get("default"));
        openNewView();
    }

    // ANDRIAUS KOMENTARAS: "Cia nuskaitai faila ir sudeti i map. Sitas metodas leidziamas ant star upo"
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        students = FXCollections.observableList(createDummyStudents()); // TODO reikia padaryti viską per observableList
        settingsLinkedMap = ReadWriteData.readFile("settings"); // nuskaitomas settings.txt
        zodynasSelect(settingsLinkedMap.get("default")); //keičiamas zodynas į default stratup metu
        versk(zodisTextField.getText());
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

        // išsaugomas default žodynas "setings" faile
        settingsLinkedMap.put("default", failoVardas);
        ReadWriteData.writeFile(settingsLinkedMap, "settings"); // įrašo settings į failą

        // žodynų pavadinimų surašymas
        z1.setText(settingsLinkedMap.get("z1"));
        z2.setText(settingsLinkedMap.get("z2"));
        z3.setText(settingsLinkedMap.get("z3"));
        z4.setText(settingsLinkedMap.get("z4"));
        z5.setText(settingsLinkedMap.get("z5"));
        z6.setText(settingsLinkedMap.get("z6"));

        // nuskaito duomenis "Žodynas"
        zodynasTreeMap = ReadWriteData.readFile(failoVardas); // užkrauna žodyną

        // išvedamas zodyno turinys į ListView
        visiListView.getItems().clear();// duomenų trynimas iš ListView
        for (String item : zodynasTreeMap.keySet()) { // public Map'as
            visiListView.getItems().addAll(item);
        }
        zodynoDydisApaciojeLabel.setText("(" + zodynasTreeMap.size() + ")");
    } // end of zodynasSelect

    // aktyvaus žodyno keitimas
    public void kitasZodynas(ActionEvent event) {
        settingsLinkedMap = ReadWriteData.readFile("settings"); // nuskaitomas settings.txt
        RadioButton pazymetasZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        zodynasSelect(pazymetasZodynas.getId());
        versk(zodisTextField.getText());
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setContentText("Pasirinktas žodynas:\n   " + pazymetasZodynas.getId() + "\nlaikinai veiks tik z1: " + z1.getText());
//        alert.show();
    }

    // reakcija į pelės paspaudimą ant ListView lauko, vis tik payko su vienu metodu
    public void clickList(MouseEvent event) {
        String selectedItem;
        if (event.getSource().toString().equals("ListView[id=variantListView, styleClass=list-view]") &&
                variantListView.getSelectionModel().getSelectedItem() != null) {
            selectedItem = variantListView.getSelectionModel().getSelectedItem();
            pirmasAtitikmuoLabel.setText(selectedItem);
            vertimasLabel.setText(zodynasTreeMap.get(selectedItem));
            laikinasLabel.setText(event.getSource().toString());
        }
        if (event.getSource().toString().equals("ListView[id=visiListView, styleClass=list-view]") &&
                visiListView.getSelectionModel().getSelectedItem() != null) {
            selectedItem = visiListView.getSelectionModel().getSelectedItem();
            pirmasAtitikmuoLabel.setText(selectedItem);
            vertimasLabel.setText(zodynasTreeMap.get(selectedItem));
            laikinasLabel.setText(event.getSource().toString());
        }
    }

    // reakcija į klavišo paspaudimą žodžio fragmento įvedimo langelyje (interaktyvi paieška)
    public void paspaudimas(KeyEvent event) {
        versk(zodisTextField.getText());
    }

    public void versk(String fragmentas) {
        if ((!fragmentas.equals(""))) {
            TreeSet<String> variantai = gautiAtitikmenuVariantus(fragmentas.toLowerCase());
            // spausdinti variantue į ListView
            variantListView.getItems().clear();// duomenų trynimas iš ListView
            for (String item : variantai) {
                variantListView.getItems().addAll(item);
            }
            variantuKiekisApaciojeLabel.setText("(" + variantai.size() + ")");
            if (variantai.size() != 0) { // ne lygu nuliui
                String pirmasAtitikmuo = variantai.first();
                pirmasAtitikmuoLabel.setText(pirmasAtitikmuo);
                vertimasLabel.setText(zodynasTreeMap.get(pirmasAtitikmuo));
            } else {
                isvalyti();
            }
        } else {
            isvalyti();
        }
        info = new Informacija(zodynasTreeMap, zodisTextField.getText());
//        System.out.println("Controller: " + zodisTextField.getText() + " -> " + info.getFragmentas());
        // fono spalvos
        if (!fragmentas.toLowerCase().equals(pirmasAtitikmuoLabel.getText().toLowerCase())) {
            pirmasAtitikmuoLabel.setStyle("-fx-background-color: #b0e5ea");
            vertimasLabel.setStyle("-fx-background-color: #b0e5ea");
        } else {
            pirmasAtitikmuoLabel.setStyle("-fx-background-color: #80ea9e");
            vertimasLabel.setStyle("-fx-background-color: #80ea9e");
        }
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

    public void isvalytiViska() {
        zodisTextField.clear();
//        zodisTextField.setCursor(); // TODO: padėti kursorių į input langelį
        isvalyti();
        versk("");
    }

    public void isvalyti() {
        variantListView.getItems().clear();// duomenų trynimas iš ListView
        pirmasAtitikmuoLabel.setText("");
        vertimasLabel.setText("");
    }

    public void onExitButtonPress() {
        Platform.exit();
    }

//    // Atliekami veiksmai prieš nutraukiant programos veikimą
//    public void onCloseEvent() {
//        System.out.println("Atliekami veiksmai prieš nutraukiant programos veikimą");
//    }
}
