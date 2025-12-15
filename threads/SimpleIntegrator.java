package threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;
    private int iteration = 0;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            iteration++;

            synchronized (task) {
                // Ждем пока генератор сгенерирует данные
                while (task.isDataProcessed()) {
                    try {
                        task.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                // Читаем и обрабатываем
                Function func = task.getFunction();
                double left = task.getLeftBorder();
                double right = task.getRightBorder();
                double step = task.getDiscretizationStep();

                if (func != null) {
                    try {
                        double result = Functions.integrate(func, left, right, step);
                        System.out.println("Result " + left + " " + right + " " + step + " " + result);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }

                task.setDataProcessed(true); // Данные обработаны
                task.notify(); // Будим генератор
            }
        }
    }
}