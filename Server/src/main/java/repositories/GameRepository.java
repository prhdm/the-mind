package repositories;

import models.Game;

import java.util.concurrent.ConcurrentHashMap;

public class GameRepository {
    private static final ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();

    public static GameRepository getInstance() {
        return new GameRepository();
    }

    private GameRepository() {}

    public void addGame(String gameID, Game game) {
        games.put(gameID, game);
    }

    public Game getGame(String gameID) {
        return games.get(gameID);
    }

    public void removeGame(String body) {
        games.remove(body);
    }

    public ConcurrentHashMap<String, Game> getGames() {
        return games;
    }
}
