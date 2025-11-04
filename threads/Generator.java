package threads;
import java.util.concurrent.Semaphore;
import functions.basic.*;

public class Generator extends Thread {
    private Task task;
    private Semaphore writeSemaphore;
    private Semaphore readSemaphore;

    public Generator(Task task, Semaphore writeSemaphore, Semaphore readSemaphore) {
        this.task = task;
        this.writeSemaphore = writeSemaphore;
        this.readSemaphore = readSemaphore;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); ++i) {
            writeSemaphore.acquireUninterruptibly();
            task.setFunction(new Log(Math.random() * 10 + 1));
            task.setLeftX(Math.random() * 101);
            task.setRightX(Math.random() * 101 + 100);
            task.setStep(Math.random());
            System.out.printf("Source левая граница: %f правая граница: %f шаг дискретизации: %f\n", task.getLeftX(), task.getRightX(), task.getStep());
            readSemaphore.release();
        }
    }
}