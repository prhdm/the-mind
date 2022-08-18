package controllers;

import client.RequestHandler;
import client.request.RequestType;
import client.response.Response;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import models.Game;

public class StartGameController {

    @FXML
    public CheckBox isPrivateCheckBox;

    @FXML
    public ChoiceBox<Integer> numberOfPlayersChoiceBox;

    @FXML
    public ChoiceBox<Integer> numberOfLevelsChoiceBox;

    @FXML
    public TextField gameTextTextField;

    public void initialize() {
        isPrivateCheckBox.setSelected(false);
        numberOfPlayersChoiceBox.setValue(4);
        numberOfLevelsChoiceBox.setValue(10);
        for (int i = 1; i <= 10 ; i++) {
            numberOfLevelsChoiceBox.getItems().add(i);
            numberOfPlayersChoiceBox.getItems().add(i);
        }
    }

    @FXML
    public void onBackToMainMenuButtonClicked() {
        MainController.getInstance().setAnchorPane("/views/menu-view.fxml");
    }

    public void onCreateGameButtonClicked() {
        Game game = new Game(gameTextTextField.getText(), isPrivateCheckBox.isSelected(),numberOfPlayersChoiceBox.getValue(),numberOfLevelsChoiceBox.getValue());
        Gson gson = new Gson();
        String body = gson.toJson(game);
        RequestHandler req = new RequestHandler(RequestType.CREATE_GAME,body);
        req.sendRequest();
        Response response = req.getResponse();
        if (response.getStatus() == 1) {
            RequestHandler.setGameId(response.getBody());
            MainController.getInstance().setAnchorPane("/views/lobby-view.fxml");
        }
    }
}
