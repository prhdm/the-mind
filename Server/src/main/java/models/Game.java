package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private transient final List<Integer> cards = new ArrayList<>();
    private String host;
    private final String gameText;
    private int heartsLeft;

    private boolean isGameFinished;

    private final int maxLevels;

    private int maxPlayers;

    private transient final List<Player> playerList = new ArrayList<>(maxPlayers);

    private transient final List<Player> bots = new ArrayList<>();

    private int level;
    private boolean isGameStarted;
    private final boolean isPrivate;
    private int ninjaCount;
    
    public void addGameLog(String log) {
        gameLog.add(log);
    }

    private final ArrayList<String> gameLog = new ArrayList<>();
    
    private final List<Integer> droppedCards = new ArrayList<>();

    public int getMaxLevels() {
        return maxLevels;
    }


    private transient Thread thread;

    public Game(String gameText, int maxPlayers, int maxLevels, boolean isPrivate) {
        level = 1;
        heartsLeft = maxPlayers;
        this.gameText = gameText;
        this.maxPlayers = maxPlayers;
        this.maxLevels = maxLevels;
        this.isPrivate = isPrivate;
        isGameStarted = false;
        for (int i = 0; i < maxPlayers; i++) {
            bots.add(new Player("Bot "+i));
        }
    }
    
    public void startBot() {
        thread = new Thread(() -> {
            while (!isGameFinished && playerList.size() != 0) {
                try {
                    int[] min = {Integer.MAX_VALUE};
                    Player[] playerWithMin = {null};
                    for (Player bot : bots) {
                        bot.getCards().forEach(integer -> {
                            min[0] = Math.min(min[0], integer);
                            playerWithMin[0] = bot;
                        });
                    }
                    int[] maxDropped = {0};
                    droppedCards.forEach(integer -> maxDropped[0] = Math.max(maxDropped[0], integer));
                    if (droppedCards.size()-1 == level* maxPlayers) {
                        play(playerWithMin[0], min[0]);
                    } else if (min[0] - maxDropped[0] <= 1) {
                        Thread.sleep(2000L);
                        play(playerWithMin[0], min[0]);;
                    } else {
                        Thread.sleep((min[0] - maxDropped[0]) * 1000L);
                        play(playerWithMin[0], min[0]);
                    }
                    if (isLevelFinished()) {
                        levelUp();
                        startGame();
                    }
                } catch (InterruptedException ignored) {
                }
            }
        });
        thread.start();
    }

    public List<Player> getPlayers() {
        return playerList;
    }

    public void addPlayer(Player player) {
        player.setCards(bots.get(0).getCards());
        playerList.add(player);
        bots.remove(0);
        gameLog.add("Bot " + 0 + " left the game");

    }

    public void setHost(String player) {
        host = player;
    }

    public boolean isGameFull() {
        return maxPlayers == playerList.size();
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public String getHost() {
        return host;
    }

    public void removePlayer(Player player) {
        playerList.remove(player);
        player.setName("Bot: "+bots.size());
        gameLog.add("Bot "+bots.size()+" joined the game");
        bots.add(player);
    }

    public void startGame() {
        isGameStarted = true;
        droppedCards.clear();
        cards.clear();
        for (int i = 0; i < 100; i++) {
            cards.add(i+1);
        }
        setCard(playerList);
        setCard(bots);
    }

    private void setCard(List<Player> playerList) {
        for (Player p : playerList) {
            List<Integer> temp = new ArrayList<>();
            for (int i = 0; i < level; i++) {
                temp.add(cards.remove(new Random().nextInt(0,cards.size())));
            }
            p.setCards(temp);
        }
    }

    public boolean play(Player player, int card) {
        for (int i : droppedCards) {
            if (i> card) {
                return false;
            }
        }
        for (Player p: playerList) {
            List<Integer> cards = p.getCards();
            for (int i: cards) {
                if (i<card) {
                    return false;
                }
            }
        }
        for (Player p: bots) {
            List<Integer> cards = p.getCards();
            for (int i: cards) {
                if (i<card) {
                    return false;
                }
            }
        }
        player.dropCard(card);
        droppedCards.add(card);
        gameLog.add(player.getPlayerName() + " dropped " + card);
        return true;
    }

    public void removeHeart() {
        gameLog.add("Heart lost");
        heartsLeft--;
        if (heartsLeft == -1) {
            isGameFinished = true;
        }
    }

    public boolean isLevelFinished() {
        return droppedCards.size() == level*maxPlayers;
    }

    public void levelUp() {
        gameLog.add("Level up");
        level++;
        if (level > maxLevels) {
            isGameFinished = true;
            gameLog.add("Game finished");
        }
    }

    public boolean isGamePrivate() {
        return isPrivate;
    }

    public String getGameText() {
        return gameText;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public boolean isPrivate() {
        return isPrivate;
    }
    
    public void ninja() {
        if (ninjaCount != 2) {
            gameLog.add("Ninja!");
            ninjaCount++;
            dropCard(playerList);
            dropCard(bots);
            if (isLevelFinished()) {
                levelUp();
                startGame();
            }
        }
    }

    private void dropCard(List<Player> playerList) {
        for (Player p: playerList) {
            int[] min = {Integer.MAX_VALUE};
            List<Integer> cards = p.getCards();
            if (cards.size() != 0) {
                cards.forEach(integer -> {
                    min[0] = Math.min(min[0], integer);
                });
                p.dropCard(min[0]);
                droppedCards.add(min[0]);
                gameLog.add(p.getPlayerName() + " dropped " + min[0]);
            }
        }
    }

    public boolean isGameFinished() {
        return isGameFinished;
    }

    public void interruptBots() {
        thread.interrupt();
    }
}
