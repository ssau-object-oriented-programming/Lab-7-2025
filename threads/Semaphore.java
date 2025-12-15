package threads;

public class Semaphore {
    private boolean canWrite = true; // сначала можно писать
    private boolean canRead = false; // читать нельзя, пока ничего не записано
    public synchronized void beginWrite() throws InterruptedException {
        while (!canWrite) {
            wait();
        }
    }
    public synchronized void endWrite() {
        // После записи блокируем запись и разрешаем чтение
        canWrite = false;
        canRead = true;
        notifyAll();
    }
    public synchronized void beginRead() throws InterruptedException {
        while (!canRead) {
            wait();
        }
    }
    public synchronized void endRead() {
        // После чтения снова разрешаем запись и запрещаем чтение
        canRead = false;
        canWrite = true;
        notifyAll();
    }
}