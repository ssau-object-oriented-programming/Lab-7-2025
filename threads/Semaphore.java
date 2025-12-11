package threads;

public class Semaphore {
    // Если true - очередь Генератора писать. Если false - очередь Интегратора читать.
    private boolean canWrite = true;

    // Генератор вызывает это перед записью
    public synchronized void beginWrite() throws InterruptedException {
        while (!canWrite) {
            wait(); // Ждем, пока Интегратор не прочитает данные
        }
    }

    // Генератор вызывает это после записи
    public synchronized void endWrite() {
        canWrite = false;
        notifyAll();
    }

    // Интегратор вызывает это перед чтением
    public synchronized void beginRead() throws InterruptedException {
        while (canWrite) {
            wait(); // Ждем, пока Генератор не запишет данные
        }
    }

    // Интегратор вызывает это после чтения
    public synchronized void endRead() {
        canWrite = true;
        notifyAll();
    }
}