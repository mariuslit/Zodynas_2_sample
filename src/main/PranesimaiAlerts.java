package main;

import javafx.scene.control.Alert;

public class PranesimaiAlerts {

    public void alert(Alert.AlertType alertType, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle("PRANEŠIMAS");
        alert.setHeaderText(null);
        alert.setContentText(content);
//        alert.setHeaderText(content);
//        alert.setContentText(null);
        alert.show();
    }

}
