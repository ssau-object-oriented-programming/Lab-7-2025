package threads;

import functions.basic.Log;
import functions.Function;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;
    private final Random rnd = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        int count = task.getCount();
        for (int i = 0; i < count; i++) {
            double base = 1.0 + rnd.nextDouble() * 9.0;
            Function f = new Log(base);
            double left = rnd.nextDouble() * 100.0;
            double right = 100.0 + rnd.nextDouble() * 100.0;
            double step = rnd.nextDouble();
            try {
                task.putBlocking(f, left, right, step);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            System.out.printf("Source %.6f %.6f %.6f\n", left, right, step);
        }
    }
}
