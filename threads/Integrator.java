package threads;

import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private Task task;
    private Semaphore dataReady; // Семафор для ожидания готовности данных
    private Semaphore dataProcessed; // Семафор для уведомления об обработке данных

    public Integrator(Task task, Semaphore dataReady, Semaphore dataProcessed) {
        this.task = task;
        this.dataReady = dataReady;
        this.dataProcessed = dataProcessed;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверяем прерывание
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }

                dataReady.acquire();
                try {
                    double left, right, step;
                    left = task.getLeftBorder();
                    right = task.getRightBorder();
                    step = task.getStep();

                    // Вычисляем интеграл
                    double result = Functions.integrate(task.getFunction(), left, right, step);
                    // Выводим результат
                    System.out.printf("Result %.6f %.6f %.6f %.6f%n", left, right, step, result);
                } finally {
                    // Сигнализируем, что данные обработаны
                    dataProcessed.release();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Integrator был прерван при ожидании семафора");
        }
    }
}