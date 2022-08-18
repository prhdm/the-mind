package controllers;

import com.google.gson.Gson;
import models.Game;
import server.models.Request;
import server.models.Response;

public class CreateGame implements RequestPerformer {

    @Override
    public Response performRequest(Request request) {
        Game gameRequest = new Gson().fromJson(request.body, Game.class);
        Game game = new Game(gameRequest.getGameText(),gameRequest.getMaxPlayers(),gameRequest.getMaxLevels(),gameRequest.isPrivate());
        game.addPlayer(serverRepository.getPlayer(request.authToken));
        String gameID = gameId();
        serverRepository.addGame(gameID, game);
        game.setHost(serverRepository.getPlayer(request.authToken).getPlayerName());
        return new Response(1, gameID);
    }

    private String gameId() {
        StringBuilder gameID = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            gameID.append((char) (Math.random() * 26 + 'a'));
        }
        return gameID.toString();
    }

}
