package server;

import server.request.RequestHandler;
import server.request.RequestQueue;
import server.models.SyncBlock;
import utils.Colors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    RequestQueue requestQueue;

    public Server() {
        requestQueue = new RequestQueue();
        new Thread(requestQueue).start();
    }

    public void init() {
        System.out.println(Colors.RED_BACKGROUND+"=============="+Colors.RESET);
        System.out.println(Colors.GREEN +"Starting Server" + Colors.RESET);
        System.out.println(Colors.RED_BACKGROUND+"=============="+Colors.RESET);
        System.out.println();
        try {
            Config config = new Config();
            ServerSocket serverSocket = new ServerSocket(config.port);
            System.out.println(Colors.GREEN+"Server Started on port: "+ config.port+"." + Colors.RESET);
            System.out.println();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println(Colors.BLUE+"Upcoming Request from: " + socket.getInetAddress().getHostAddress()+Colors.RESET);
                addRequestToQueue(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addRequestToQueue(Socket socket) throws IOException {
        RequestHandler requestHandler = new RequestHandler(socket);
        requestQueue.addToQueue(new Thread(requestHandler));
        SyncBlock.getInstance().notifyBlock();
    }
}