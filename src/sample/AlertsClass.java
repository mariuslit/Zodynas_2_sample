package sample;

import javafx.scene.control.Alert;

public class AlertsClass {

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
