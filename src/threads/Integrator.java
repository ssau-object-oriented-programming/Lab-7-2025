package threads;

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
        int tasksCount = task.getTasksCount();
        int completedTasks = 0;

        while (completedTasks < tasksCount) {
            try {
                if (isInterrupted()) {
                    System.out.println("Integrator прерван");
                    break;
                }
                semaphore.beginRead();
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();
                try {
                    double result = Functions.integrate(task.getFunction(), left, right, step);
                    System.out.printf("Result %.4f %.4f %.4f %.6f%n", left, right, step, result);
                } catch (IllegalArgumentException e) {
                    System.out.printf("Result %.4f %.4f %.4f ERROR: %s%n", left, right, step, e.getMessage());
                }
                completedTasks++;
                semaphore.endRead();

            } catch (InterruptedException e) {
                System.out.println("Integrator прерван во время ожидания");
                break;
            }
        }
    }
}

