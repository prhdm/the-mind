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

public class RequestHandler {
    private static boolean isConnect;
    private final RequestType requestType;
    private static Response response;
    private final String body;
    private static String authToken;

    public static String getGameId() {
        return gameId;
    }

    private static String gameId;
    Gson gson;

    PrintWriter out;
    Scanner in;

    public RequestHandler(RequestType requestType,String body) {
        gson = new Gson();
        this.requestType = requestType;
        this.body = body;
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



    public void sendRequest() {
        Config config = Config.getInstance();
        try (Socket socket = new Socket(config.host, config.port)) {
            isConnect = true;
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            sendRequestToServer();
            getResponseFromServer();
            in.close();
            out.close();
        } catch (Exception e) {
            isConnect = false;
        }
    }

    private void sendRequestToServer() {
        Request request = new Request(authToken,requestType.toString(),body);
        String req = gson.toJson(request);
        out.println(req);
        out.flush();
    }

    private void getResponseFromServer() {
        String input = in.nextLine();
        response = gson.fromJson(input, Response.class);
    }

    public Response getResponse() {
        return response;
    }

    public void setAuthToken(String authToken) {
        RequestHandler.authToken = authToken;
    }
}
