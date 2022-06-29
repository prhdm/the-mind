package client;

import javafx.application.Platform;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    private static Client client;
    public static Client getInstance() {
        if (client == null)
            client = new Client();
        return client;
    }
    static Thread clientThread;
    public Client() {
        clientThread = new Thread(this);
        clientThread.start();
    }
    public void runClient() throws IOException {
        Config config = new Config();
        Socket socket = new Socket(config.host, config.port);
        DataInputStream input = new DataInputStream(System.in);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.write(4);
    }

    @Override
    public void run() {
        while (true) {
            if (Platform.isImplicitExit())
                break;
            try {
                runClient();
                break;
            } catch (Exception e) {
                try {
                    System.out.println("failed to connect");
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public void startGame(String name) {
    }
}
