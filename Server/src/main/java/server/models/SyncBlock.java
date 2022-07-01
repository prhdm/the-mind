package server.models;

public class SyncBlock {
    private static SyncBlock syncBlock;
    public static  SyncBlock getInstance() {
        if (syncBlock == null)
            syncBlock = new SyncBlock();
        return syncBlock;
    }

    public void waitBlock() {
        try {
            synchronized (syncBlock) {
                syncBlock.wait();
            }
        } catch (Exception ignored) {}
    }

    public void notifyBlock() {
        synchronized (syncBlock) {
            syncBlock.notify();
        }
    }
}
