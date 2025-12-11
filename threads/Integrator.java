package threads;

import functions.Functions;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        for (int i = 0; i < task.taskCount; i++) {
            if (isInterrupted()) {
                System.out.println("Integrator interrupted");
                break;
            }

            try {
                // Запрашиваем разрешение на чтение
                semaphore.beginRead();

                // Читаем и считаем
                double res = Functions.integrate(task.function, task.left, task.right, task.step);

                System.out.printf("Result: left=%.2f right=%.2f step=%.4f res=%.6f%n",
                        task.left, task.right, task.step, res);

                // Сообщаем, что чтение окончено
                semaphore.endRead();

            } catch (InterruptedException e) {
                System.out.println("Integrator interrupted during wait");
                interrupt();
                break;
            }
        }
    }
}