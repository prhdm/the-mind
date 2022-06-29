package controllers;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MenuController {
    public TextField nameTextField;

    public void onExitButtonClicked() {
        System.exit(0);
    }

    public void OnStartGameButtonClicked() {
        // TODO: Load a new pane for entering game settings and waiting for ppl to connect
        String name = nameTextField.getText();
        if (name.equals(""))
            System.out.println("ridi");
        else
            MainController.getInstance().setAnchorPane("/views/joinGame-view.fxml");

    }

    public void OnJoinGameButtonClicked() {
        //TODO: Show list of available games
        // TODO: Load a new page and show the list of available game.
        String name = nameTextField.getText();
        if (name.equals(""))
            System.out.println("ridi");
        else
            MainController.getInstance().setAnchorPane("/views/joinGame-view.fxml");
    }
}
