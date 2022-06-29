package controllers;

import com.sun.tools.javac.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class MainController {
    private static MainController mainController;

    public static MainController getInstance() {
        if (mainController == null)
            mainController = new MainController();
        return mainController;
    }
    public AnchorPane mainPane;

    public void initialize() {
        mainController = this;
        setAnchorPane("/views/menu-view.fxml");
    }

    public void setAnchorPane(String fxml) {
        try {
            AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            mainPane.getChildren().setAll(pane);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
