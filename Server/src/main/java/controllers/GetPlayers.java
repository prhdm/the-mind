package controllers;

import server.models.Request;
import server.models.Response;

public class GetPlayers implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        if (serverRepository.getGame(request.body).isGameStarted())
            return new Response(1, "start");
        return new Response(1, serverRepository.getGame(request.body).getPlayers().toString());
    }

}
