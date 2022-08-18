package server.request;

import com.google.gson.Gson;
import repositories.ServerRepository;
import server.models.Request;
import server.models.RequestType;
import server.models.Response;
import server.models.Wiring;
import utils.Colors;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RequestHandler implements Runnable {
    private static final ServerRepository serverRepository = ServerRepository.getInstance();
    private final Socket socket;
    private final PrintWriter out;
    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream());
    }

    private void printMessage(Request request) {
        System.out.println(Colors.BLUE+"Upcoming Request from: " + socket.getInetAddress().getHostAddress()+Colors.RESET);
        System.out.println();
        System.out.println(Colors.GREEN+"Request Type: " + request.url + Colors.RESET);
        if (request.authToken != null)
            System.out.println(Colors.GREEN+"Requested by: " + serverRepository.getPlayer(request.authToken).getPlayerName() + Colors.RESET);
        System.out.println(Colors.GREEN+"Request Body: ");
        System.out.println(request.body.replace("{","").replace("}","").replace(",","\n")+Colors.RESET);
    }

    private void sendMessage(Response response) {
        String res = new Gson().toJson(response);
        out.println(res);
        out.flush();
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            String messageFromClient = in.nextLine();
            Request request = new Gson().fromJson(messageFromClient, Request.class);
            if (request.url != RequestType.UPDATE_GAME
                    && request.url != RequestType.UPDATE_PLAYER_LIST
                    && request.url != RequestType.GAME_LIST
                    && request.url != RequestType.GET_PLAYERS) {
                printMessage(request);
            }
            sendMessage(new Wiring().performRequest(request));
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}