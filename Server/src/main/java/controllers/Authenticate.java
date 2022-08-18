package controllers;

import models.Player;
import server.models.Request;
import server.models.Response;

import java.security.SecureRandom;
import java.util.Base64;

public class Authenticate implements RequestPerformer {

    @Override
    public Response performRequest(Request request) {
        if (request.authToken != null) {
            String GameID = serverRepository.getPlayer(request.authToken).getGameID();
            if (GameID != null) {
                serverRepository.getGame(GameID).removePlayer(serverRepository.getPlayer(request.authToken));
            }
            serverRepository.removePlayer(request.authToken);
        }
        String auth = makeAuthToken(request.body);
        return new Response(1, auth);
    }

    private String makeAuthToken(String name) {
        Player player = new Player(name);
        String token = randomString();
        serverRepository.addPlayer(token, player);
        return token;
    }

    private String randomString() {
        SecureRandom random = new SecureRandom();
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        return encoder.encodeToString(bytes);
    }

}
