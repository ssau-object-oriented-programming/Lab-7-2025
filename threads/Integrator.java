package threads;

import functions.Functions;
import java.util.concurrent.Semaphore;

public class Integrator extends Thread {
    private Task task;
    private Semaphore generatorSemaphore;
    private Semaphore integratorSemaphore;

    public Integrator(Task task, Semaphore generatorSemaphore, Semaphore integratorSemaphore) {
        this.task = task;
        this.generatorSemaphore = generatorSemaphore;
        this.integratorSemaphore = integratorSemaphore;
    }

    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            try {
                if (isInterrupted()) {
                    return;
                }

                // ждем разрешения на интегрирование
                integratorSemaphore.acquire();

                // читаем данные
                double left = task.getLeftBorder();
                double right = task.getRightBorder();
                double step = task.getStep();

                // вычисляем интеграл
                double result = Functions.integrate(task.getFunction(),
                        left, right, step);

                System.out.printf("Result %.4f %.4f %.4f %.4f\n",
                        left, right, step, result);

                // разрешаем генератору работать над следующим заданием
                generatorSemaphore.release();

            } catch (InterruptedException e) {
                return;
            }
        }
    }
}