package threads;
import java.util.concurrent.Semaphore;
import functions.*;

public class Integrator extends Thread {
    private Task task;
    private Semaphore writeSemaphore;
    private Semaphore readSemaphore;

    public Integrator(Task task, Semaphore writeSemaphore, Semaphore readSemaphore) {
        this.task = task;
        this.writeSemaphore = writeSemaphore;
        this.readSemaphore = readSemaphore;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); ++i) {
            readSemaphore.acquireUninterruptibly();
            System.out.printf("Result левая граница: %f правая граница: %f шаг дискретизации: %f результат интегрирования: %f\n", task.getLeftX(), task.getRightX(), task.getStep(), Functions.integrate(task.getFunction(), task.getLeftX(), task.getRightX(), task.getStep()));
            writeSemaphore.release();
        }
    }
}