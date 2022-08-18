package controllers;

import models.Game;
import server.models.Request;
import server.models.Response;

public class PlayCard implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        String gameID = serverRepository.getPlayer(request.authToken).getGameID();
        Game game = serverRepository.getGame(gameID);
        int card = Integer.parseInt(request.body);
        boolean ok = game.play(serverRepository.getPlayer(request.authToken),card);
        if (!ok) {
            game.removeHeart();
            if (game.isGameFinished())
                return new Response(0, "FINISHED");
            else
                game.startGame();
        } else if (game.isLevelFinished()) {
            game.levelUp();
            game.startGame();
        }
        game.interruptBots();
        return new Response(1, "");
    }
}

