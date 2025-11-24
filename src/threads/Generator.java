package threads;

import functions.basic.Log;
import functions.Function;
import java.util.Random;

public class Generator extends Thread {
    private final Task task;
    private final SimpleSemaphore sem;
    private final Random rnd = new Random();

    public Generator(Task task, SimpleSemaphore sem) {
        this.task = task;
        this.sem = sem;
    }

    @Override
    public void run() {
        int count = task.getCount();
        for (int i = 0; i < count && !isInterrupted(); i++) {
            double base = 1.0 + rnd.nextDouble() * 9.0;
            Function f = new Log(base);
            double left = rnd.nextDouble() * 100.0;
            double right = 100.0 + rnd.nextDouble() * 100.0;
            double step = rnd.nextDouble();
            try {
                sem.acquireWriter();
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
            task.putNoSync(f, left, right, step);
            System.out.printf("Source %.6f %.6f %.6f\n", left, right, step);
            sem.releaseWriter();
        }
    }
}
