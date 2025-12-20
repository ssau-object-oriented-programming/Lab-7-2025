package threads;

import functions.Function;
import java.util.concurrent.Semaphore;
import static functions.Functions.integral;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        int taskCount = task.getTaskCount();
        int completedTasks = 0;

        while (completedTasks < taskCount) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Integrator прерван");
                return;
            }
            Function function = null;
            double left = 0;
            double right = 0;
            double step = 0;

            try {
                semaphore.acquire();
                try {
                    function = task.getFunction();
                    left = task.getLeftBorder();
                    right = task.getRightBorder();
                    step = task.getDiscretStep();
                    task.setFunction(null);
                } finally {
                    semaphore.release();
                }
            } catch (InterruptedException e) {
                System.out.println("Integrator: прерван при ожидании семафора");
                Thread.currentThread().interrupt();
                return;
            }

            if (function != null) {
                completedTasks++;
                try {
                    double result = integral(function, left, right, step);
                    System.out.printf("Integrator[%d]: Result %.4f %.4f %.4f %.7f%n",
                            completedTasks, left, right, step, result);
                } catch (Exception e) {
                    System.out.println("Integrator error: " + e.getMessage());
                }
            }
            else {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    System.out.println("Integrator прерван во время сна");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        System.out.println("Integrator завершен");
    }
}
