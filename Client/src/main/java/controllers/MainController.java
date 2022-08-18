package controllers;

import config.ConfigPath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class MainController {
    private static MainController mainController;

    @FXML
    public AnchorPane mainPane;

    public void initialize() {
        mainController = this;
        setAnchorPane(ConfigPath.Menu);
    }

    public static MainController getInstance() {
        if (mainController == null)
            mainController = new MainController();
        return mainController;
    }

    public void setAnchorPane(String fxml) {
        try {
            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            mainPane.getChildren().setAll(pane);
        }
        catch (Exception ignored) {}
    }

    void showAlert(String connection_refused, String please_check_your_internet) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(connection_refused);
        alert.setContentText(please_check_your_internet);
        alert.showAndWait();
    }

}
