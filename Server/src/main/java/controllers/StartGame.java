package controllers;

import models.Game;
import server.models.Request;
import server.models.Response;

public class StartGame implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        Game game = serverRepository.getGame(request.body);
        serverRepository.getPlayer(request.authToken).setGameId(request.body);
        game.startGame();
        game.startBot();
        return new Response(1, "");
    }
}
