package threads;

import functions.basic.*;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;
    Random random = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            synchronized (task) {
                // Ждем, пока предыдущие данные будут обработаны
                while (task.getFunction() != null) {
                    try {
                        task.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                // Создаем логарифмическую функцию со случайным основанием от 1 до 10
                double base = 1 + random.nextDouble() * 9;

                // Генерируем параметры
                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                task.setFunction(new Log(base));
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setStep(step);

                // Выводим сообщение
                System.out.printf("Source %.6f %.6f %.6f%n", left, right, step);

                // Уведомляем интегратор
                task.notifyAll();
            }
        }
    }
}