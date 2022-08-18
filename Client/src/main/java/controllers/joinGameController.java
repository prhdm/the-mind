package controllers;

import client.RequestHandler;
import client.request.RequestType;
import client.response.Response;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Game;

import java.util.Arrays;

public class joinGameController {

    public ListView<VBox> availableGamesList;
    public TextField gamedIdTextField;

    Thread thread;
    public void initialize() {
        thread = new Thread(() -> {
            while (true) {
                try {
                    RequestHandler req = new RequestHandler(RequestType.GAME_LIST,"");
                    req.sendRequest();
                    Response response = req.getResponse();
                    update(response.getBody());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        thread.start();
    }

    public void onGameClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            int index = availableGamesList.getSelectionModel().getSelectedIndex();
            if (index != -1)
                gamedIdTextField.setText(gameIds[index]);
        }
    }

    public void onEnterGameButtonClicked() {
        String gameId= gamedIdTextField.getText();
        RequestHandler req = new RequestHandler(RequestType.JOIN_GAME,gameId);
        req.sendRequest();
        Response response = req.getResponse();
        if (response.getStatus() == 1) {
            RequestHandler.setGameId(gameId);
            if (response.getBody().split(",")[0].equals("1")) {
                new RequestHandler(RequestType.START_GAME,RequestHandler.getGameId());
                MainController.getInstance().setAnchorPane("/views/game-view.fxml");
                thread.interrupt();
            } else {
                RequestHandler.setHost(response.getBody().split(",")[1]);
                MainController.getInstance().setAnchorPane("/views/lobby-view.fxml");
            }
        } else {
            MainController.getInstance().showAlert("Game is Full","You Can't join this game");
        }
    }

    public void onBackToMainMenuButtonClicked() {
        MainController.getInstance().setAnchorPane("/views/menu-view.fxml");
    }

    String[] gameIds;

    public void update(String body) {
        Platform.runLater(() -> {
            String[] games = body.split("-");
            gameIds = games[0].replace("[", "").replace("]", "").split(", ");
            Game[] gameList = new Gson().fromJson(games[1], Game[].class);
            availableGamesList.getItems().clear();
for (int i = 0; i < gameIds.length; i++) {
                availableGamesList.getItems().add(setVbox(gameList[i], gameIds[i]));
            }
        });
    }

    public VBox setVbox(Game game, String gameId) {
        HBox hBoxUp = new HBox();
        hBoxUp.getChildren().add(new Label("Game Text :"+game.getGameText()));
        hBoxUp.getChildren().add(new Label("Game Id :"+gameId));
        HBox hBoxDown = new HBox();
        hBoxDown.getChildren().add(new Label("Max players :"+game.getMaxPlayers()));
        hBoxDown.getChildren().add(new Label("Started :"+game.isGameStarted()));
        VBox vBox = new VBox();
        vBox.getChildren().add(hBoxUp);
        vBox.getChildren().add(hBoxDown);
        return vBox;
    }
}
