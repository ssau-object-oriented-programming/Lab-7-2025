package threads;

public class SimpleSemaphore {
    private boolean writerPermit = true;
    private boolean readerPermit = false;

    public SimpleSemaphore() {}

    public synchronized void acquireWriter() throws InterruptedException {
        while (!writerPermit) wait();
        writerPermit = false;
    }

    public synchronized void releaseWriter() {
        readerPermit = true;
        notifyAll();
    }

    public synchronized void acquireReader() throws InterruptedException {
        while (!readerPermit) wait();
        readerPermit = false;
    }

    public synchronized void releaseReader() {
        writerPermit = true;
        notifyAll();
    }
}
