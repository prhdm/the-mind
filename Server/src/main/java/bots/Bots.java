package bots;

import models.Game;
import models.Player;

import java.util.HashMap;
public class Bots {
    static final HashMap<Player,long[]> delay = new HashMap<>();
    static Player player;
    static Game game;
    public Bots(Player p, Game game) {
        player = p;
        Bots.game = game;
    }

    public void action() {
        int max =0;
        for (int i : game.getDroppedCards()) {
            if (i>max)
                max = i;
        }
        int min = 101;
        for (int i : player.getCards()) {
            if (i<min)
                min=i;
        }
        if (delay.get(player)==null) {
            delay.put(player, new long[]{(System.currentTimeMillis() / 1000L), min - max});
            return;
        }
        Long time = delay.get(player)[0]+delay.get(player)[1];
        if ((delay.get(player)[0]+ min-max)>time) {
            delay.put(player, new long[]{(System.currentTimeMillis() / 1000L), min - max});
        } else if (System.currentTimeMillis() / 1000L > time) {
            game.play(player,min);
            delay.remove(player);
        }
    }
}
