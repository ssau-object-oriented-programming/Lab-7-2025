package threads;

import functions.Function;
import functions.basic.Log;

import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;
    private Random random = new Random();
    private int iteration = 0;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            iteration++;

            synchronized (task) {
                // Ждем пока интегратор обработает предыдущие данные
                while (!task.isDataProcessed()) {
                    try {
                        task.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                // Генерируем новые данные
                double base;
                do {
                    base = 1 + random.nextDouble() * 9; // [1, 10]
                } while (Math.abs(base - 1.0) < 1e-10);

                Function logFunction = new Log(base);
                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();

                task.setFunction(logFunction);
                task.setLeftBorder(left);
                task.setRightBorder(right);
                task.setDiscretizationStep(step);
                task.setDataProcessed(false); // Данные готовы для обработки

                System.out.println("\nИтерация " + iteration);
                System.out.println("Source " + left + " " + right + " " + step);

                task.notify(); // Будим интегратор
            }
        }
    }
}
