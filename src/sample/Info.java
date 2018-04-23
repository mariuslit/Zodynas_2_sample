package sample;

import javafx.stage.Stage;

import java.util.Map;
import java.util.TreeMap;

public class Info {

    private Map<String, String> zodynasR = new TreeMap<>();
    private String fragmentasR = new String();
    private String vertimasR = new String();
    private Stage stage2 = new Stage();

    // konstruktoriaus Overloading'as
    public Info(Map<String, String> zodynas, String fragmentas) {
        this.zodynasR = zodynas;
        this.fragmentasR = fragmentas;
    }

    // konstruktoriaus Overloading'as
    public Info(Map<String, String> zodynas, String fragmentas, String vertimas/*, String zodynoID*/) {
        this.zodynasR = zodynas;
        this.fragmentasR = fragmentas;
        this.vertimasR = vertimas;
//        this.zodynoIdR = zodynoID;
    }

    // konstruktoriaus Overloading'as
    public Info(Stage stage) {
        this.stage2 = stage;
    }

    public Stage getStage() {
        return stage2;
    }

    public String getFragmentas() {
        return fragmentasR;
    }

    public String getVertimas() {
        return vertimasR;
    }
}
