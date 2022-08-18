package server.models;

import controllers.*;

public class Wiring {
    public Response performRequest(Request request) {
        return switch (request.url) {
            case AUTHENTICATE -> new Authenticate().performRequest(request);
            case CREATE_GAME -> new CreateGame().performRequest(request);
            case GAME_LIST -> new GameList().performRequest(request);
            case GET_PLAYERS -> new GetPlayers().performRequest(request);
            case JOIN_GAME -> new JoinGame().performRequest(request);
            case EXIT_GAME -> new ExitGame().performRequest(request);
            case START_GAME -> new StartGame().performRequest(request);
            case PLAY_CARD -> new PlayCard().performRequest(request);
            case UPDATE_GAME -> new UpdateGame().performRequest(request);
            case PLAY_NINJA -> new PlayNinja().performRequest(request);
            case EMOJI -> new Emoji().performRequest(request);
            default -> new Response(0, "Invalid Request");
        };
    }
}
