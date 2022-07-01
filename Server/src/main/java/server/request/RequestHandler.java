package server.request;

import bots.BotActions;
import bots.Bots;
import com.google.gson.Gson;
import models.Game;
import models.GameRequest;
import models.Player;
import server.models.Request;
import server.models.Response;
import server.models.SyncBlock;
import utils.Colors;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.*;

public class RequestHandler implements Runnable {
    private static HashMap<String, Player> users = new HashMap<>();
    private static HashMap<String,Game> games = new HashMap();
    private final Socket socket;
    private final PrintWriter out;
    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream());
    }


    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            String messageFromClient = in.nextLine();
            System.out.println(messageFromClient);
            Gson gson = new Gson();
            Request request = gson.fromJson(messageFromClient, Request.class);
            System.out.println();
            System.out.println(Colors.GREEN+"Request Type: " + request.url + Colors.RESET);
            if (request.authToken != null)
                System.out.println(Colors.GREEN+"Requested by: " + users.get(request.authToken).getPlayerName() + Colors.RESET);
            System.out.println(Colors.GREEN+"Request Body: ");
            System.out.println(request.body.replace("{","").replace("}","").replace(",","\n")+Colors.RESET);
            performRequest(request);
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    boolean play = false;
    private void performRequest(Request request) {
        Gson gson = new Gson();
        Response response;

        switch (request.url) {
            case "AUTHENTICATE":
                if (request.authToken != null)
                    users.remove(request.authToken);
                String auth = makeAuthToken(request.body);
                response = new Response(1, auth);
                String res = gson.toJson(response);
                sendMessage(res);
                break;
            case "CREATE_GAME":
                GameRequest gameRequest = gson.fromJson(request.body, GameRequest.class);
                Game game = new Game(gameRequest.gameText, gameRequest.maxPlayers, gameRequest.maxLevels, gameRequest.isPrivate);
                game.addPlayer(users.get(request.authToken));
                String gameID = randomString(4);
                games.put(gameID, game);
                game.setHost(request.authToken);
                response = new Response(1, gameID);
                res = gson.toJson(response);
                sendMessage(res);
                break;
            case "GAME_LIST":
                String gameslist = games.values().toString();
                response = new Response(1, gameslist);
                gson = new Gson();
                res = gson.toJson(response);
                sendMessage(res);
                break;
            case "GET_PLAYERS":
                gameID = request.body;
                auth = request.authToken;
                SyncBlock.getInstance().notifyBlock();
                int numbPlayers=0;
                while (users.get(auth) != null && games.get(gameID) != null && games.get(gameID).isGameStarted().equals("0")) {
                    if (numbPlayers != games.get(gameID).getPlayers().size()) {
                        numbPlayers = games.get(gameID).getPlayers().size();
                        StringBuilder players = new StringBuilder();
                        for (Player p: games.get(gameID).getPlayers()) {
                            players.append(p.getPlayerName()).append(" ");
                        }
                        response = new Response(1, players.toString());
                        gson = new Gson();
                        res = gson.toJson(response);
                        sendMessage(res);
                    }
                }
                if (games.get(gameID).isGameStarted().equals("1")) {
                    response = new Response(1,"start");
                    gson = new Gson();
                    res = gson.toJson(response);
                    sendMessage(res);
                }
                break;
            case "JOIN_GAME":
                gameID = request.body;
                game = games.get(gameID);
                if (game != null && !game.isGameFull()) {
                    users.get(request.authToken).setGameId(gameID);
                    game.addPlayer(users.get(request.authToken));
                    response = new Response(1,game.isGameStarted()+","+game.getHost());
                    gson = new Gson();
                    res = gson.toJson(response);
                    sendMessage(res);
                } else if (game == null) {
                    response = new Response(0,"not-exist");
                    gson = new Gson();
                    res = gson.toJson(response);
                    sendMessage(res);
                } else {
                    response = new Response(0,"game-full");
                    gson = new Gson();
                    res = gson.toJson(response);
                    sendMessage(res);
                }
                break;
            case "EXIT_GAME":
                games.get(request.body).removePlayer(users.get(request.authToken));
                users.remove(request.authToken);
                if (games.get(request.body).getPlayers().size()==0) {
                    games.remove(request.body);
                }
                response = new Response(1,"");
                gson = new Gson();
                res = gson.toJson(response);
                sendMessage(res);
                break;
            case "START_GAME":
                game = games.get(request.body);
                game.startGame();
                auth = request.authToken;
                gameID = request.body;
                int numbDroppedCards=-1;
                SyncBlock.getInstance().notifyBlock();
                if (bots == null) {
                    bots = new Thread(new BotActions(game));
                    bots.start();
                }
                while (users.get(auth) != null && games.get(gameID) != null && game.getLevel() != (game.getMaxLevels()+1) && game.getHearts() != 0) {
                    if (numbDroppedCards != game.getDroppedCards().size()) {
                        numbDroppedCards = game.getDroppedCards().size();
                        response = new Response(1, games.get(gameID).toString() + "," + users.get(auth).getCards().toString());
                        gson = new Gson();
                        res = gson.toJson(response);
                        sendMessage(res);
                        System.out.println(games.get(request.body).isGameStarted());
                    }
                }
                if (users.get(auth) != null)
                    bots.interrupt();
                bots.interrupt();
                break;
            case "PLAY_CARD":
                String[] query = request.body.split(",");
                game = games.get(query[0]);
                int card = Integer.parseInt(query[1]);
                boolean ok = game.play(users.get(request.authToken),card);

                if (!ok) {
                    game.removeHeart();
                    game.startGame();
                } else if (game.isLevelFinished()) {
                    game.levelUp();
                    game.startGame();
                }
        }
    }
    static Thread bots;


    private String makeAuthToken(String name) {
        Player player = new Player(name);
        String token = randomString(10);
        users.put(token,player);
        return token;
    }

    private String randomString(int length) {
        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }
    
    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }
    
}