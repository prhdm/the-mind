package repositories;

import models.Game;
import models.Player;

import java.util.concurrent.ConcurrentHashMap;

public class ServerRepository {

    private static ServerRepository serverRepository;
    private PlayerRepository playerRepository;
    private GameRepository gameRepository;

    public static ServerRepository getInstance() {
        if (serverRepository == null) {
            serverRepository = new ServerRepository();
        }
        return serverRepository;
    }

    private ServerRepository() {
        playerRepository = PlayerRepository.getInstance();
        gameRepository = GameRepository.getInstance();
    }

    public Player getPlayer(String authToken) {
        return playerRepository.getPlayer(authToken);
    }

    public void addGame(String gameID, Game game) {
        gameRepository.addGame(gameID, game);
    }

    public void addPlayer(String token, Player player) {
        playerRepository.addPlayer(token, player);
    }

    public Game getGame(String gameID) {
        return gameRepository.getGame(gameID);
    }

    public void removePlayer(String authToken) {
        playerRepository.removePlayer(authToken);
    }

    public void removeGame(String body) {
        gameRepository.removeGame(body);
    }

    public ConcurrentHashMap<String,Game> getGames() {
        return gameRepository.getGames();
    }
}
