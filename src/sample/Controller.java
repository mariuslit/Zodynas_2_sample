package sample;

import com.sun.prism.shader.FillCircle_RadialGradient_PAD_AlphaTest_Loader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.ReadWriteFile.ReadWriteData;

import java.io.FileReader;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    public Stage newWindow = new Stage();

    @FXML
    public void langas() throws Exception {
        Platform.isImplicitExit(); // TODO reika kad atidarius langą, tėvinis langa būtų užrkaintas
        Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Žodynas Ltit (žodyno redagavimo režimas)");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void onPlus() {
        editButton.setVisible(editButton.isVisible() == true ? false : true);
        newWindow.show();
//        langas();
//        newWindow.show();

    }

    @FXML
    private TextField zodisTextField;
    @FXML
    private Label laikinasLabel;
    @FXML
    private Label pirmasAtitikmuoLabel;
    @FXML
    private Label vertimasLabel;
    @FXML
    private Label variantuAprasymasLabel;
    @FXML
    private Label zodynoAprasymasLabel;
    @FXML
    private ListView visiListView;
    @FXML
    private ListView variantListView;
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

    @Override
    //1 ANDRIAUS KOMENTARAS: "Cia nuskaitai faila ir sudeti i map. Sitas metodas leidziamas ant star upo"
    public void initialize(URL location, ResourceBundle resources) {
        settingsLinkedMap = ReadWriteData.readFile("settings"); // nuskaitomas settings.txt
        zodynasSelect(settingsLinkedMap.get("default")); //keičiamas zodynas į default stratup metu
        versk(zodisTextField.getText());
        zodisTextField.setText("");
    }
    //2 aktyvaus žodyno keitimas

    public void kitasZodynas(ActionEvent event) {
        RadioButton pazymetasZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
        zodynasSelect(pazymetasZodynas.getId());
        versk(zodisTextField.getText());
//        Alert alert = new Alert(Alert.AlertType.WARNING);
//        alert.setContentText("Pasirinktas žodynas:\n   " + pazymetasZodynas.getId() + "\nlaikinai veiks tik z1: " + z1.getText());
//        alert.show();
    }
    //3 žodyno pakeitima

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

        // nuskaito ir išsaugo duomenis "Žodynas"
        zodynasTreeMap = ReadWriteData.readFile(failoVardas); // užkrauna žodyną
        ReadWriteData.writeFile(zodynasTreeMap, "laikinas"); // TODO įrašymo testavimas

        // išvedamas zodyno turinys į ListView
        TreeSet x = new TreeSet<>();
        printVisiListView(x, "visi");
        RadioButton aktyvusZodynas = (RadioButton) zodynuGrupe.getSelectedToggle();
    }
    //4 reakcija į klavišo paspaudimą žodžio fragmento įvedimo langelyje

    public void click(MouseEvent event) {
        String s = event.getPickResult().toString(); // TODO užklausiau Andriaus per fb
        laikinasLabel.setText(s); // TODO kai viskas veiks laikinasLabel. pakeisti į vertimasLabel.
    }
    //5 reakcija į klavišo paspaudimą žodžio fragmento įvedimo langelyje

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
                zodynoAprasymasLabel.setText("(" + zodynasTreeMap.size() + ")");
                break;
            case "variantai":
                variantListView.getItems().clear();// duomenų trynimas iš ListView
                for (String item : variantai) {
                    variantListView.getItems().addAll(item);
//                    Alert alert = new Alert(Alert.AlertType.WARNING);
//                    alert.setContentText("item: (" + item + ")");
//                    alert.show();
                }
                variantuAprasymasLabel.setText("(" + variantai.size() + ")");
                break;
        }
    }

    public void isvalytiViska() {
        zodisTextField.clear();
        isvalyti();
//        zodisTextField.setCursor(); // TODO: padėti kursorių į input langelį
    }

    public void isvalyti() {
        variantListView.getItems().clear();// duomenų trynimas iš ListView
        pirmasAtitikmuoLabel.setText("-");
        vertimasLabel.setText("-");
    }

    public void exitButon() {
        Platform.exit();
    }
}
