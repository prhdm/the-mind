import java.net.ServerSocket;
public class Server {
    public Server() {
        try {
            Config config = new Config();
            ServerSocket socket = new ServerSocket(config.port);
            socket.accept();
            System.out.println("accepted socket");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
