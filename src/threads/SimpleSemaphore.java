package threads;

public class SimpleSemaphore {
    private boolean canWrite = true;

    public synchronized void beginWrite() throws InterruptedException {
        while (!canWrite) {
            wait();
        }
    }

    public synchronized void endWrite() {
        canWrite = false;
        notifyAll();
    }

    public synchronized void beginRead() throws InterruptedException {
        while (canWrite) {
            wait();
        }
    }

    public synchronized void endRead() {
        canWrite = true;
        notifyAll();
    }
}
