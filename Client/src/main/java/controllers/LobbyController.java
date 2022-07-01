package controllers;

import client.RequestHandler;
import client.request.RequestType;
import client.response.Response;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.Arrays;

public class LobbyController {

    public Label gameIdLabel;

    private static LobbyController lobbyController;
    public ListView<String> playerList;
    public Button startGameButton;

    public static LobbyController getInstance() {
        return lobbyController;
    }

    public void initialize() {
        lobbyController = this;
        gameIdLabel.setText(RequestHandler.getGameId());
        new RequestHandler(RequestType.GET_PLAYERS,RequestHandler.getGameId());

    }
    public void onBackToMainMenuButtonClicked() {
        MainController.getInstance().setAnchorPane("/views/menu-view.fxml");
    }

    public void onStartGameButtonClicked() {

    }

    public void update(String body) {
        String players[] = body.replace("[","").replace("]","").replace(",{"," {").split(" ");
        Arrays.stream(players).forEach(s -> playerList.getItems().add(s));
        if (RequestHandler.isHost()) {
        }
    }
}
