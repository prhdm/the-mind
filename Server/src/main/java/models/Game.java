package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {
    private final List<Integer> cards = new ArrayList<>();
    private String host;
    private final String gameText;
    private int heartsLeft;

    public int getMaxLevels() {
        return maxLevels;
    }

    private final int maxLevels;

    public Game(String gameText, int maxPlayers, int maxLevels, boolean isPrivate) {
        level = 1;
        heartsLeft = maxPlayers;
        this.gameText = gameText;
        this.maxPlayers = maxPlayers;
        this.maxLevels = maxLevels;
        this.isPrivate = isPrivate;
        isGameStarted = false;
        isPrivate = false;
        for (int i = 0; i < maxPlayers; i++) {
            bots.add(new Player("Bot "+i));
        }
    }

    private int maxPlayers;
    private final List<Player> playerList = new ArrayList<>(maxPlayers);
    private final List<Player> bots = new ArrayList<>();

    private int level;
    private boolean isGameStarted;
    private final boolean isPrivate;

    public List<Integer> getDroppedCards() {
        return droppedCards;
    }

    public void addToDroppedCards(int i) {
        droppedCards.add(i);
    }

    private final List<Integer> droppedCards = new ArrayList<>();


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
        player.setCards(bots.get(0).getCards());
        playerList.add(player);
        bots.remove(0);

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
        player.setName("Bot: "+bots.size());
        bots.add(player);
    }

    public void startGame() {
        isGameStarted = true;
        for (int i = 0; i < 100; i++) {
            cards.add(i+1);
        }
        for (Player p :playerList) {
            List<Integer> temp = new ArrayList<>();
            for (int i = 0; i < level; i++) {
                temp.add(cards.remove(new Random().nextInt(0,cards.size())));
            }
            p.setCards(temp);
        }
        for (Player p: bots) {
            List<Integer> temp = new ArrayList<>();
            for (int i = 0; i < level; i++) {
                temp.add(cards.remove(new Random().nextInt(0,cards.size())));
            }
            p.setCards(temp);
        }
    }

    public int getLevel() {
        return level;
    }

    public int getHearts() {
        return heartsLeft;
    }

    public boolean play(Player player, int card) {
        for (int i : droppedCards) {
            if (i> card)
                return false;
        }
        for (Player p: playerList) {
            List<Integer> cards = p.getCards();
            for (int i: cards) {
                if (i>card)
                    return false;
            }
        }
        for (Player p: bots) {
            List<Integer> cards = p.getCards();
            for (int i: cards) {
                if (i>card)
                    return false;
            }
        }
        player.dropCard(card);
        droppedCards.add(card);
        return true;
    }

    public void removeHeart() {
        heartsLeft--;
    }

    public boolean isLevelFinished() {
        return droppedCards.size() == level*maxPlayers;
    }

    public void levelUp() {
        level++;
    }

    public List<Player> getBots() {
        return bots;
    }
}
