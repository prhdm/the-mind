package models;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private String gameID;
    private List<Integer> cards = new ArrayList<>();

    public String getPlayerName() {
        return playerName;
    }

    public Player(String name) {
        this.playerName = name;
    }

    public void setGameId(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(List<Integer> cards) {
        this.cards = cards;
    }

    public void setName(String s) {
        this.playerName = s;
    }

    @Override
    public String toString() {
        return "{" +
                "playerName='" + playerName + '\'' +
                ", gameID='" + gameID + '\'' +
                ", cards=" + cards +
                '}';
    }

}
