package controllers;

import server.models.Request;
import server.models.Response;

public class ExitGame implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        serverRepository.getGame(request.body).addGameLog(serverRepository.getPlayer(request.authToken).getPlayerName() + " left the game");
        serverRepository.getGame(request.body).removePlayer(serverRepository.getPlayer(request.authToken));
        serverRepository.removePlayer(request.authToken);
        serverRepository.getGame(request.body).interruptBots();
        if (serverRepository.getGame(request.body).getPlayers().size()==0) {
            serverRepository.removeGame(request.body);
        }
        return new Response(1,"");
    }

}
