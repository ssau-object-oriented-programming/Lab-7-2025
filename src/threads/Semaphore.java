package threads;

public class Semaphore {
    private int readers = 0;//Счётчик активных читателей (сколько потоков сейчас читает)
    private int writers = 0;//Счётчик активных писателей (0 или 1)
    private int writeRequests = 0;//счётчик запросов на запись (сколько писателей ждут)

    // Генератор начинает запись
    public synchronized void beginWrite() throws InterruptedException {
        writeRequests++;
        while (readers > 0 || writers > 0) {
            wait();
        }
        writeRequests--;
        writers++;
    }

    // Генератор заканчивает запись
    public synchronized void endWrite() {
        writers--;
        notifyAll(); // уведомляем интегратор
    }

    // Интегратор начинает чтение
    public synchronized void beginRead() throws InterruptedException {
        while (writers > 0 || writeRequests > 0) {
            wait();
        }
        readers++;
    }

    // Интегратор заканчивает чтение
    public synchronized void endRead() {
        readers--;
        notifyAll(); // уведомляем генератор
    }
}
