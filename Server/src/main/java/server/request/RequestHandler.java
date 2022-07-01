package server.request;

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
                System.out.println(Colors.GREEN+"Requested by: " + users.get(request.authToken) + Colors.RESET);
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
            case "UPDATE_GAME":
                break;
            case "PLAY":
                break;
            case "GAME_LIST":
                String gameslist = games.values().toString();
                response = new Response(1, gameslist);
                gson = new Gson();
                res = gson.toJson(response);
                sendMessage(res);
                break;
            case "CONNECT_GAME":
                break;
            case "GET_PLAYERS":
                gameID = request.body;
                SyncBlock.getInstance().notifyBlock();
                int numbOfplayers=0;
                while (games.get(gameID).isGameStarted().equals("0")) {
                    if (numbOfplayers != games.get(gameID).getPlayers().size()) {
                        numbOfplayers = games.get(gameID).getPlayers().size();
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
                break;
            case "JOIN_GAME":
                gameID = request.body;
                game = games.get(gameID);
                if (!game.isGameFull()) {
                    users.get(request.authToken).setGameId(gameID);
                    game.addPlayer(users.get(request.authToken));
                    response = new Response(1,game.isGameStarted()+","+game.getHost());
                    gson = new Gson();
                    res = gson.toJson(response);
                    sendMessage(res);
                }
                break;
            case "EXIT_GAME":
                games.get(request.body).removePlayer(users.get(request.authToken));
                users.remove(request.authToken);
        }
    }

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