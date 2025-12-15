package threads;

import functions.Functions;
import functions.Function;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            if (isInterrupted()) { // Проверяем прерывание перед началом обработки очередного задания
                System.out.println("Integrator прерван");
                break;
            }
            double left;
            double right;
            double step;
            Function function;
            // Чтение задания через семафор
            try {
                semaphore.beginRead();
                try {
                    function = task.getFunction();
                    left = task.getLeft();
                    right = task.getRight();
                    step = task.getStep();
                } finally {
                    semaphore.endRead();
                }
            } catch (InterruptedException e) {
                System.out.println("Integrator прерван при ожидании семафора");
                interrupt();
                break;
            }
            try {
                double result = Functions.integration(function, left, right, step);
                // Сообщение "Result левая правая шаг результат"
                System.out.printf("Result %.6f %.6f %.6f %.6f%n",
                        left, right, step, result);
            } catch (IllegalArgumentException e) { // Если границы интегрирования выходят за область определения функции
                System.out.printf("Result %.6f %.6f %.6f ERROR: %s%n",
                        left, right, step, e.getMessage());
            }
            try { // Небольшая пауза, чтобы лучше увидеть взаимодействие потоков
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Integrator прерван во время sleep");
                interrupt();
                break;
            }
        }
    }
}