package models;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<Integer> cards = new ArrayList<>();
    private String host;
    private final String gameText;
    private int heartsLeft;

    private final int maxLevels;

    private int maxPlayers;

    private int level;
    private boolean isGameStarted;
    private final boolean isPrivate;

    public void addToDroppedCards(int i) {
        droppedCards.add(i);
    }

    private final List<Integer> droppedCards = new ArrayList<>();

    public ArrayList<String> getGameLog() {
        return gameLog;
    }

    private final ArrayList<String> gameLog = new ArrayList<>();

    public Game(String gameText, boolean isPrivate , int maxPlayers , int maxLevels) {
        this.gameText = gameText;
        this.isPrivate = isPrivate;
        this.maxPlayers = maxPlayers;
        this.maxLevels = maxLevels;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public String getHost() {
        return host;
    }

    public String getGameText() {
        return gameText;
    }

    public int getHeartsLeft() {
        return heartsLeft;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getLevel() {
        return level;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public List<Integer> getDroppedCards() {
        return droppedCards;
    }
}
