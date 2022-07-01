package client;

import client.request.Request;
import client.request.RequestType;
import client.response.Response;
import com.google.gson.Gson;
import controllers.MainController;
import javafx.application.Platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RequestHandler implements Runnable {
    private static boolean isConnect;
    private final RequestType requestType;
    private Response response;
    private final String body;
    private static String authToken;

    public static String getGameId() {
        return gameId;
    }

    private static String gameId;
    Gson gson;

    public RequestHandler(RequestType requestType,String body) {
        gson = new Gson();
        this.requestType = requestType;
        this.body = body;
        new Thread(this).start();
        SyncBlock.getInstance().waitBlock();
    }

    public static void setGameId(String body) {
        gameId = body;
    }

    public static boolean isConnected() {
        return isConnect;
    }

    private static String host;
    public static void setHost(String h) {
        host = h;
    }

    public static boolean isHost() {
        return host== null || host.equals(authToken);
    }

    private void init(RequestType requestType) {
        try {
            isConnect = true;
            if (requestType == RequestType.AUTHENTICATE) {
                authToken = null;
                sendRequest();
                authToken = response.getBody();
            } else if (requestType == RequestType.GET_PLAYERS) {
                SyncBlock.getInstance().notifyBlock();
                openStream(requestType);
            } else {
                sendRequest();
            }
        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();
        }
        SyncBlock.getInstance().notifyBlock();
    }

    private void openStream(RequestType requestType) throws IOException {
        Config config = Config.getInstance();
        Socket socket = new Socket(config.host, config.port);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        Scanner scanner = new Scanner(socket.getInputStream());
        Request request = new Request(authToken,requestType.toString(),body);
        String req = gson.toJson(request);
        printWriter.println(req);
        printWriter.flush();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            response = gson.fromJson(input, Response.class);
            Platform.runLater(() -> MainController.getInstance().realTimeupdate(requestType,response));
        }
        scanner.close();
        socket.close();
        printWriter.close();
    }

    private void sendRequest() throws IOException {
        Config config = Config.getInstance();
        Socket socket = new Socket(config.host, config.port);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        Scanner scanner = new Scanner(socket.getInputStream());
        Request request = new Request(authToken,requestType.toString(),body);
        String req = gson.toJson(request);
        printWriter.println(req);
        printWriter.flush();
        String input = scanner.nextLine();
        response = gson.fromJson(input,Response.class);
        scanner.close();
        socket.close();
        printWriter.close();
    }


    @Override
    public void run() {
        init(requestType);
    }

    public Response getResponse() {
        return response;
    }
}
