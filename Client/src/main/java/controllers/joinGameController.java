package controllers;

import client.RequestHandler;
import client.request.RequestType;
import client.response.Response;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.Game;

public class joinGameController {

    public ListView<Game> availableGamesList;
    public TextField gamdIdTextField;

    public void initialize() {
        // TODO: tell server to get list of games that are public
        // TODO: then we have set a for loop to send a request to server to get game props

        new RequestHandler(RequestType.GAME_LIST,"");
        //GameRequest gameRequest = new GameRequest();
        //ObservableList<Game> gameObservableList = FXCollections.observableArrayList();
        //gameObservableList.addAll(Arrays.asList(gameRequest.getGames()));
    }

    public void onGameClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
        }
    }

    public void onEnterGameButtonClicked(ActionEvent actionEvent) {
        String gameId=gamdIdTextField.getText();
        Response response = new RequestHandler(RequestType.JOIN_GAME,gameId).getResponse();
        if (response.getStatus() == 1) {
            RequestHandler.setGameId(gameId);
            if (response.getBody().split(",")[0].equals("1")) {
            } else {
                System.out.println(response.getBody());
                RequestHandler.setHost(response.getBody().split(",")[1]);
                MainController.getInstance().setAnchorPane("/views/lobby-view.fxml");
            }
        } else {
            MainController.getInstance().showAlert("Game is Full","You Can't join this game");
        }
    }

    public void onBackToMainMenuButtonClicked(ActionEvent actionEvent) {
        MainController.getInstance().setAnchorPane("/views/menu-view.fxml");
    }
}
