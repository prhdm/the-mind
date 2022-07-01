package models;

public class Game {
    private final boolean isPrivate;
    private final int maxLevels;
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

    public Game(String gameText, boolean isPrivate , int maxPlayers , int maxLevels) {
        this.gameText = gameText;
        this.isPrivate = isPrivate;
        this.maxPlayers = maxPlayers;
        this.maxLevels = maxLevels;
    }
}
