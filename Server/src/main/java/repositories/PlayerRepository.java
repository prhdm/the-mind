package repositories;

import models.Player;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerRepository {
    private static final ConcurrentHashMap<String, Player> users = new ConcurrentHashMap<>();

    public static PlayerRepository getInstance() {
        return new PlayerRepository();
    }

    private PlayerRepository() {}

    public Player getPlayer(String authToken) {
        return users.get(authToken);
    }

    public void addPlayer(String token, Player player) {
        users.put(token, player);
    }

    public void removePlayer(String authToken) {
        users.remove(authToken);
    }
}
