package threads;

import functions.basic.Log;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {
    private Task task;
    private Semaphore generatorSemaphore;
    private Semaphore integratorSemaphore;

    public Generator(Task task, Semaphore generatorSemaphore, Semaphore integratorSemaphore) {
        this.task = task;
        this.generatorSemaphore = generatorSemaphore;
        this.integratorSemaphore = integratorSemaphore;
    }

    public void run() {
        Random random = new Random();

        for (int i = 0; i < task.getTasksCount(); i++) {
            try {
                if (isInterrupted()) {
                    return;
                }

                // ждем разрешения на генерацию
                generatorSemaphore.acquire();

                // генерируем значения
                double base = 1 + random.nextDouble() * 9;
                Log logFunction = new Log(base);
                double leftBorder = random.nextDouble() * 100;

                // гарантируем, что rightBorder > leftBorder
                double rightBorder;
                do {
                    rightBorder = 100 + random.nextDouble() * 100;
                } while (rightBorder <= leftBorder);

                double step = random.nextDouble();

                // записываем в задание
                task.setFunction(logFunction);
                task.setLeftBorder(leftBorder);
                task.setRightBorder(rightBorder);
                task.setStep(step);

                System.out.printf("Source %.4f %.4f %.4f\n",
                        leftBorder, rightBorder, step);

                // разрешаем интегратору работать
                integratorSemaphore.release();

            } catch (InterruptedException e) {
                return;
            }
        }
    }
}