package threads;

import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        Random random = new Random();
        for (int i = 0; i < task.taskCount; i++) {
            // Если кто-то попросил поток остановиться (interrupt), выходим
            if (isInterrupted()) {
                System.out.println("Generator interrupted");
                break;
            }

            try {
                //  Запрашиваем разрешение на запись
                semaphore.beginWrite();

                // Пишем данные
                double base = 2 + random.nextDouble() * 9;
                task.function = new Log(base);
                task.left = 0.1 + random.nextDouble() * 100;
                task.right = 100 + random.nextDouble() * 100;
                task.step = Math.max(0.0001, random.nextDouble());

                System.out.printf("Source: left=%.2f right=%.2f step=%.4f%n",
                        task.left, task.right, task.step);

                // Сообщаем, что запись окончена
                semaphore.endWrite();

            } catch (InterruptedException e) {
                System.out.println("Generator interrupted during wait");
                interrupt(); // Восстанавливаем флаг прерывания
                break;
            }
        }
    }
}