package controllers;

import models.Game;
import server.models.Request;
import server.models.Response;

public class PlayNinja implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        String gameID = serverRepository.getPlayer(request.authToken).getGameID();
        Game game = serverRepository.getGame(gameID);
        game.ninja();
        return new Response(1,"");
    }
}
