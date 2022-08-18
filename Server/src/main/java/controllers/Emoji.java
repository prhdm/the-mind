package controllers;

import models.Game;
import server.models.Request;
import server.models.Response;

public class Emoji implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        String gameID = serverRepository.getPlayer(request.authToken).getGameID();
        Game game = serverRepository.getGame(gameID);
        game.addGameLog(serverRepository.getPlayer(request.authToken).getPlayerName() + " " + request.body);
        return new Response(1, "");
    }
}
