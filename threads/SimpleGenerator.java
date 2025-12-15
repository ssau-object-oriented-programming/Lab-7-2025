package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        Random random = new Random();

        for (int i = 0; i < task.getTasksCount(); i++) {
            // Основание логарифма: равномерно на [1, 10)
            double base = 1.0 + random.nextDouble() * 9.0;
            // Левая граница: [0, 100)
            double left = random.nextDouble() * 100.0;
            // Правая граница: [100, 200)
            double right = 100.0 + random.nextDouble() * 100.0;
            // Шаг дискретизации: [0, 1)
            double step = random.nextDouble();

            // Атомарная запись всех параметров задания
            synchronized (task) {
                task.setFunction(new Log(base));
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
            }

            // Сообщение Source левая правая шаг"
            System.out.printf("Source %.6f %.6f %.6f%n", left, right, step);

            // Небольшая задержка для наглядности работы потоков
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // Корректно выходим при прерывании
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}