package controllers;

import client.RequestHandler;
import client.request.RequestType;
import client.response.Response;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.Player;

import java.util.Arrays;

public class LobbyController {

    private static LobbyController lobbyController;

    @FXML
    public Label gameIdLabel;

    @FXML
    public ListView<String> playerList;

    @FXML
    public Button startGameButton;

    public static LobbyController getInstance() {
        return lobbyController;
    }

    Thread thread;
    public void initialize() {
        lobbyController = this;
        gameIdLabel.setText(RequestHandler.getGameId());
        thread = new Thread(() -> {
            while (true) {
                try {
                    RequestHandler req = new RequestHandler(RequestType.GET_PLAYERS, RequestHandler.getGameId());
                    req.sendRequest();
                    Response response = req.getResponse();
                    if (response.getBody().equals("start")) {
                        Platform.runLater(() -> {
                            MainController.getInstance().setAnchorPane("/views/game-view.fxml");
                        });
                        break;
                    }
                    else {
                        update(response.getBody());
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        thread.start();
    }

    @FXML
    public void onBackToMainMenuButtonClicked() {
        thread.interrupt();
        RequestHandler req = new RequestHandler(RequestType.EXIT_GAME,RequestHandler.getGameId());
        MainController.getInstance().setAnchorPane("/views/menu-view.fxml");
    }


    @FXML
    public void onStartGameButtonClicked() {
        RequestHandler req = new RequestHandler(RequestType.START_GAME,RequestHandler.getGameId());
        req.sendRequest();
        Response response = req.getResponse();
        if (response.getStatus() == 1) {
            MainController.getInstance().setAnchorPane("/views/game-view.fxml");
        } else {
            MainController.getInstance().showAlert("Game is Full","You Can't join this game");
        }
    }

    public void update(String body) {
        Platform.runLater(() -> {
            Player[] players = new Gson().fromJson(body, Player[].class);
            playerList.getItems().clear();
            Arrays.stream(players).forEach(s -> playerList.getItems().add(s.getPlayerName()));
            startGameButton.setDisable(!RequestHandler.isHost());
            startGameButton.setVisible(RequestHandler.isHost());
        });
    }
}
