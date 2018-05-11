package sample;

import javafx.stage.Stage;

import java.util.Map;
import java.util.TreeMap;

public class Info {

    private Map<String, String> dictionary = new TreeMap<>();
    private String fragment;
    private String translation;
    private Stage stageR;// = new Stage();

    // konstruktoriaus Overloading'as
    Info(Map<String, String> dictionary, String fragment) {
        this.dictionary = dictionary;
        this.fragment = fragment;
    }

    // konstruktoriaus Overloading'as
    Info(Map<String, String> dictionary, String fragment, String translation) {
        this.dictionary = dictionary;
        this.fragment = fragment;
        this.translation = translation;
    }

    // konstruktoriaus Overloading'as
    public Info(Stage stage) {
        this.stageR = stage;
    }

    public void setStageR(Stage stageR) {
        this.stageR = stageR;
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
