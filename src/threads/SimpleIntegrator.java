package threads;

import functions.*;
import static functions.Functions.integral;


public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    public void run() {
        int taskCount = task.getTaskCount();
        int completedTasks = 0;

        while (completedTasks < taskCount) {
            Function function = null;
            double left = 0;
            double right = 0;
            double step = 0;

            synchronized (task) {
                function = task.getFunction();
                left = task.getLeftBorder();
                right = task.getRightBorder();
                step = task.getDiscretStep();
                task.setFunction(null);
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
                    Thread.sleep(5); // Короткая пауза
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        System.out.println("Integrator завершен");
    }
}


