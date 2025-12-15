package threads;

import functions.Function;
import functions.basic.Log;

import java.util.Random;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private Random random = new Random();

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                semaphore.startWrite();

                if (isInterrupted()) {
                    System.out.println("Генератор прерван");
                    semaphore.endWrite(); // Важно освободить семафор!
                    return;
                }

                double base = 2 + random.nextDouble() * 8;
                Function logFunction = new Log(base);
                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                task.setFunction(logFunction);
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setDiscretizationStep(step);

                // Выводим
                System.out.println("\nИтерация " + (i + 1) + ":\nSource: " + left + " " + right + " " + step);

                semaphore.endWrite();
            }

        } catch (InterruptedException e) {
            System.out.println("Генератор прерван");
        }
    }
}