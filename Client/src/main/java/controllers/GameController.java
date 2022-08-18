package controllers;

import client.RequestHandler;
import client.request.RequestType;
import client.response.Response;
import com.google.gson.Gson;
import config.ConfigPath;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import models.Game;

import java.util.Arrays;

public class GameController {
    private static GameController gameController;
    public HBox cardsPlace;
    public Label numberOfLives;
    public Label level;
    public ListView<String> actionResults;

    Thread thread;

    public static GameController getInstance() {
        if (gameController == null)
            gameController = new GameController();
        return gameController;
    }

    public void initialize() {
        gameController = this;
        thread = new Thread(() -> {
            while (true) {
                try {
                    RequestHandler req = new RequestHandler(RequestType.UPDATE_GAME, RequestHandler.getGameId());
                    req.sendRequest();
                    if (req.getResponse().getStatus() == 0)
                        break;
                    Platform.runLater(() -> {
                        Response response = req.getResponse();
                        update(response.getBody());
                    });
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        thread.start();
    }

    public void onExitGameClicked() {
        new RequestHandler(RequestType.EXIT_GAME,RequestHandler.getGameId()).sendRequest();
        thread.interrupt();
        MainController.getInstance().setAnchorPane(ConfigPath.Menu);
    }

    public void update(String body) {
        cardsPlace.getChildren().clear();
        String[] query = body.split("-");
        String[] cards = query[0].replace("[", "").replace("]", "").split(", ");
        Game game = new Gson().fromJson(query[1], Game.class);
        numberOfLives.setText(game.getHeartsLeft() + "");
        level.setText(game.getLevel() + "");
        actionResults.getItems().clear();
        actionResults.getItems().addAll(game.getGameLog());
        for (String card : cards) {
            if (card.equals(""))
                continue;
            Button button = new Button(card);
            button.setOnAction(event -> {
                RequestHandler requestHandler =new RequestHandler(RequestType.PLAY_CARD,button.getText());
                requestHandler.sendRequest();
            });
            cardsPlace.getChildren().add(button);
        }
    }

    public void onEmojiButtonClicked(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        RequestHandler requestHandler =new RequestHandler(RequestType.EMOJI,button.getText());
        requestHandler.sendRequest();
    }

    public void onNinjaButtonClicked() {
        RequestHandler requestHandler =new RequestHandler(RequestType.PLAY_NINJA,"");
        requestHandler.sendRequest();
    }
}
