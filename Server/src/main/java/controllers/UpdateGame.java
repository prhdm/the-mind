package controllers;

import com.google.gson.Gson;
import models.Game;
import models.Player;
import server.models.Request;
import server.models.Response;

public class UpdateGame implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        String auth = request.authToken;
        Player player = serverRepository.getPlayer(auth);
        Game game = serverRepository.getGame(player.getGameID());
        if (player == null || game.isGameFinished() )
            return new Response(0, "");
        return new Response(1, player.getCards().toString()+"-"+new Gson().toJson(serverRepository.getGame(request.body)));
    }
}
