package bots;

import models.Game;
import models.Player;

import java.lang.reflect.InvocationTargetException;

public class BotActions implements Runnable {
    private Game game;
    public BotActions(Game game) {
        this.game = game;
    }

    private void botPlay(Game game) {
        try {
            for (Player p : game.getBots()) {
                Bots bot = new Bots(p,game);
                bot.getClass().getMethod("action").invoke(bot);
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            botPlay();
        }
    }
}
