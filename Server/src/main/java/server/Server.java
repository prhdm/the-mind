package server;

import server.request.RequestHandler;
import utils.Colors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    ExecutorService executorService;

    public Server() {
        int cores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(cores);
    }

    public void init() {
        System.out.println(Colors.RED_BACKGROUND+"=============="+Colors.RESET);
        System.out.println(Colors.GREEN +"Starting Server" + Colors.RESET);
        System.out.println(Colors.RED_BACKGROUND+"=============="+Colors.RESET);
        System.out.println();
        Config config = new Config();
        try (ServerSocket serverSocket = new ServerSocket(config.getPort())) {
            System.out.println(Colors.GREEN+"Server Started on port: "+ config.getPort()+"." + Colors.RESET);
            System.out.println();
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new RequestHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}