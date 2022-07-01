package client;

import controllers.MainController;

public class SyncBlock {
    private static SyncBlock syncBlock;
    public static  SyncBlock getInstance() {
        if (syncBlock == null)
            syncBlock = new SyncBlock();
        return syncBlock;
    }

    public void waitBlock() {
        try {
            synchronized (MainController.getInstance()) {
                MainController.getInstance().wait();
            }
        } catch (Exception ignored) {}
    }

    public void notifyBlock() {
        synchronized (MainController.getInstance()) {
            MainController.getInstance().notify();
        }
    }
}
