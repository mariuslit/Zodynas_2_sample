package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertsClass {
    // boolean reikalingas tik dėl vieno CONFIRMATION alerto, kur naudojamas if sąlygoje
    public boolean alerts(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        boolean b = false;
        if (alertType == Alert.AlertType.CONFIRMATION) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                b = true;
            }
        } else {
            alert.show();
        }
        return b;
    }
}
