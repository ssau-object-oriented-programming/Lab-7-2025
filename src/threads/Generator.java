package threads;

import functions.basic.Log;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Generator (Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    public void run() {
        int taskCount = task.getTaskCount();

        for (int i = 0; i < taskCount; i++) {

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Generator прерван");
                return;
            }

            double base = 1 + Math.random() * 9;
            double left = Math.random() * 100;
            double right = 100 + Math.random() * 100;
            double step = Math.random();

            Log logFunction = new Log(base);

            try {
                semaphore.acquire();
                try {
                    task.setFunction(logFunction);
                    task.setLeftBorder(left);
                    task.setRightBorder(right);
                    task.setDiscretStep(step);
                } finally {
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                System.out.println("Generator прерван при ожидании семафора");
                Thread.currentThread().interrupt();
                return;
            }
            System.out.printf("Generator [%d]: Source %.4f %.4f %.4f%n",
                    i+1, left, right, step);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Generator прерван во время сна");
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.println("Generator завершен");
    }
}
