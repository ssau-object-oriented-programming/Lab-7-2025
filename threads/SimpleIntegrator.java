package threads;

import functions.Functions;
import functions.Function;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            double left;
            double right;
            double step;
            Function function;
            synchronized (task) {
                // Ждём, пока генератор хотя бы один раз заполнит задачу
                while (task.getFunction() == null) {
                    try {
                        // Ждём недолго, чтобы не блокироваться навсегда
                        task.wait(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                function = task.getFunction();
                left = task.getLeft();
                right = task.getRight();
                step = task.getStep();
            }
            try {
                double result = Functions.integration(function, left, right, step);
                // Сообщение Result левая правая шаг результат
                System.out.printf("Result %.6f %.6f %.6f %.6f%n",
                        left, right, step, result);
            } catch (IllegalArgumentException e) {
                // Если интегрирование вышло за область определения
                System.out.printf("Result %.6f %.6f %.6f ERROR: %s%n",
                        left, right, step, e.getMessage());}
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}