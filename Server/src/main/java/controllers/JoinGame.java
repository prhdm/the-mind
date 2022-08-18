package controllers;

import models.Game;
import server.models.Request;
import server.models.Response;

public class JoinGame implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        Game game = serverRepository.getGame(request.body);
        if (game != null && !game.isGameFull()) {
            serverRepository.getPlayer(request.authToken).setGameId(request.body);
            game.addPlayer(serverRepository.getPlayer(request.authToken));
            game.addGameLog(serverRepository.getPlayer(request.authToken).getPlayerName() + " joined the game");
            game.interruptBots();
            return new Response(1,game.isGameStarted()+","+game.getHost());
        } else if (game == null) {
            return new Response(0,"not-exist");
        } else {
            return new Response(0,"game-full");
        }
    }

}

