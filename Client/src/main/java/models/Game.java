package models;

public class Game {
    private String gameText;
    private String gameId;

    public String getGameText() {
        return gameText;
    }

    public int getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    private int players;
    private int maxPlayers;
    private boolean isGameStarted;
}
