package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private String host;
    private final String gameText;
    private final int maxLevels;

    public Game(String gameText, int maxPlayers, int maxLevels, boolean isPrivate) {
        this.gameText = gameText;
        this.maxPlayers = maxPlayers;
        this.maxLevels = maxLevels;
        this.isPrivate = isPrivate;
        isGameStarted = false;
        isPrivate = false;
        for (int i = 0; i < maxPlayers; i++) {
            bots.add(new Player("Bot "+i));
            playerList.add(bots.get(i));
        }
    }

    private int maxPlayers;
    private final List<Player> playerList = new ArrayList<>(maxPlayers);
    private final List<Player> bots = new ArrayList<>();

    private int level;
    private final boolean isGameStarted;
    private final boolean isPrivate;
    private final List<Integer> cards = new ArrayList<>(100);

    private int getRandomNumber() {
        int random = new Random().nextInt(1,100);
        while (cards.get(random) == 1) {
            random = new Random().nextInt(1,100);
        }
        cards.set(random,1);
        return random;
    }

    @Override
    public String toString() {
        return "{" +
                '"'+"maxPlayers"+'"'+":" + '"'+maxPlayers +'"'+
                ","+ '"'+"isGameStarted"+'"'+":" + '"'+ isGameStarted + '"'+
                ","+'"'+"isPrivate"+'"'+":" + '"'+ isPrivate + '"'+
                "}";
    }

    public List<Player> getPlayers() {
        return playerList;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public void setHost(String player) {
        host = player;
    }

    public boolean isGameFull() {
        return maxPlayers == playerList.size();
    }

    public String isGameStarted() {
        return isGameStarted ? "1" : "0";
    }

    public String getHost() {
        return host;
    }

    public void removePlayer(Player player) {
        playerList.remove(player);
        Player newPlayer = new Player("Bot "+bots.size());
        newPlayer.setCards(player.getCards());
        bots.add(newPlayer);
        playerList.add(newPlayer);
    }
}
