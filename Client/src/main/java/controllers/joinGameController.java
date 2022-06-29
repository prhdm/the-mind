package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import models.Game;
import models.GameRequest;

import java.util.Arrays;

public class joinGameController {

    public ListView<Game> availableGamesList;

    public void initialize() {
        // TODO: tell server to get list of games that are public
        // TODO: then we have set a for loop to send a request to server to get game props
        //GameRequest gameRequest = new GameRequest();
        //ObservableList<Game> gameObservableList = FXCollections.observableArrayList();
        //gameObservableList.addAll(Arrays.asList(gameRequest.getGames()));

    }

    public void onGameClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            System.out.println(((Game)mouseEvent.getSource()).getGameText());
        }
    }

    public void onEnterGameButtonClicked(ActionEvent actionEvent) {
        //TODO: get textField text
        String gameId="";
        //TODO: sendRequest to find out isGameFull or isGameAvailable
        boolean check = true;
        if (check) {
            //TODO: send a request to join the player to game;
        } else {
            //TODO: send an alert to tell player that game is not available
        }
    }
}
