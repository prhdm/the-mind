package server.request;

import server.models.SyncBlock;

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue implements Runnable{

    public final Queue<Thread> requestQueue = new LinkedList<>();

    public void addToQueue(Thread socket) {
        requestQueue.add(socket);
    }

    @Override
    public void run() {
        while (true) {
            if (!requestQueue.isEmpty())
                requestQueue.poll().start();
            SyncBlock.getInstance().waitBlock();
        }
    }
}
