package threads;

import functions.basic.Log;

import java.util.Random;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        Random random = new Random();
        int tasksCount = task.getTasksCount();
        for (int i = 0; i < tasksCount; i++) {
            try {
                if (isInterrupted()) {
                    System.out.println("Generator прерван");
                    break;
                }
                semaphore.beginWrite();

                double base = 1 + random.nextDouble() * 9;
                Log logFunction = new Log(base);
                task.setFunction(logFunction);
                double left = random.nextDouble() * 100;
                task.setLeft(left);
                double right = 100 + random.nextDouble() * 100;
                task.setRight(right);
                double step = random.nextDouble();
                task.setStep(step);
                System.out.printf("Source %.4f %.4f %.4f%n", left, right, step);
                semaphore.endWrite();
            } catch (InterruptedException e) {
                System.out.println("Generator прерван во время ожидания");
                break;
            }
        }
    }
}
