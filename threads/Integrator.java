package threads;

import functions.Function;
import functions.Functions;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                semaphore.startRead();

                if (isInterrupted()) {
                    System.out.println("Интегратор прерван");
                    semaphore.endRead(); // освободить семафор
                    return;
                }

                Function func = task.getFunction();
                double left = task.getLeftBorder();
                double right = task.getRightBorder();
                double step = task.getDiscretizationStep();

                try {
                    double result = Functions.integrate(func, left, right, step);
                    // Выводим
                    System.out.println("Result: " + left + " " + right + " " + step + " = " + result);
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }

                semaphore.endRead();
            }

            System.out.println("Интегратор завершил");

        } catch (InterruptedException e) {
            System.out.println("Интегратор прерван");
        }
    }
}