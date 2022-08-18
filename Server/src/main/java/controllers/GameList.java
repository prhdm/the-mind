package controllers;

import com.google.gson.Gson;
import models.Game;
import server.models.Request;
import server.models.Response;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameList implements RequestPerformer {
    @Override
    public Response performRequest(Request request) {
        ConcurrentHashMap<String, Game> games = serverRepository.getGames();
        ArrayList<Game> gameList = new ArrayList<>();
        games.forEach((k,v) -> {
            if (!v.isGamePrivate() && !v.isGameFull())
                gameList.add(v);
        } );
        return new Response(1, games.keySet() +"-"+ new Gson().toJson(gameList));
    }
}
