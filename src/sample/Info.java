package sample;

import javafx.stage.Stage;

import java.util.Map;
import java.util.TreeMap;

public class Info {

    private Map<String, String> dictionary = new TreeMap<>();
    private String fragment = new String();
    private String translation = new String();
    private Stage stageR = new Stage();

    // konstruktoriaus Overloading'as
    public Info(Map<String, String> dictionary, String fragment) {
        this.dictionary = dictionary;
        this.fragment = fragment;
    }

    // konstruktoriaus Overloading'as
    public Info(Map<String, String> dictionary, String fragment, String translation/*, String zodynoID*/) {
        this.dictionary = dictionary;
        this.fragment = fragment;
        this.translation = translation;
//        this.zodynoIdR = zodynoID;
    }

    // konstruktoriaus Overloading'as
    public Info(Stage stage) {
        this.stageR = stage;
    }

    public Stage getStage() {
        return stageR;
    }

    public String getFragmentas() {
        return fragment;
    }

    public String getVertimas() {
        return translation;
    }
}
