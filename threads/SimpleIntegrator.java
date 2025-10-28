package threads;

import functions.basic.*;
import functions.*;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            synchronized (task) {
                // Ждем, пока данные появятся
                while (task.getFunction() == null) {
                    try {
                        task.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                double left, right, step;

                left = task.getLeftBorder();
                right = task.getRightBorder();
                step = task.getStep();

                // Вычисляем интеграл
                double result = Functions.integrate(task.getFunction(), left, right, step);

                // Выводим результат
                System.out.printf("Result %.6f %.6f %.6f %.6f%n", left, right, step, result);

                // Сбрасываем функцию
                task.setFunction(null);

                // Уведомляем генератор
                task.notifyAll();
            }
        }
    }
}