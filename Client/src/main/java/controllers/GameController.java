package controllers;

import client.RequestHandler;
import client.request.RequestType;
import client.response.Response;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import models.Game;

public class GameController {
    private static GameController gameController;
    public HBox cardsPlace;

    public static GameController getInstance() {
        if (gameController == null)
            gameController = new GameController();
        return gameController;
    }

    public void initialize() {
        gameController = this;
        Response response = RequestHandler.getResponse();
    }

    public void onExitGameClicked() {
        MainController.getInstance().setAnchorPane("/views/menu-view.fxml");
        new RequestHandler(RequestType.EXIT_GAME,RequestHandler.getGameId());
    }

    public void update(String body) {
        String[] query = body.replace("},[","} [").split(" ");
        String[] cards = query[1].replace("[","").replace("]","").replace("{","").replace("}","").split(",");
        cardsPlace.getChildren().clear();
        for (int i = 0; i < cards.length; i++) {
            Button button = new Button(cards[i]);
            button.setOnAction(event -> {
                new RequestHandler(RequestType.PLAY_CARD,RequestHandler.getGameId()+","+button.getText());
            });
            cardsPlace.getChildren().add(button);
        }
    }
}
