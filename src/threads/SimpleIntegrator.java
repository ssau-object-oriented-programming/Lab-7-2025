package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    @Override
    public void run() {
        int tasksCount = task.getTasksCount();

        for (int i = 0; i < tasksCount; i++) {
            synchronized(task) {
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();

                try {
                    double result = Functions.integrate(task.getFunction(), left, right, step);
                    System.out.printf("Result %.4f %.4f %.4f %.6f%n", left, right, step, result);
                } catch (IllegalArgumentException e) {
                    System.out.printf("Result %.4f %.4f %.4f ERROR: %s%n", left, right, step, e.getMessage());
                }
            }

            try {
                Thread.sleep(2); // небольшая пауза для генератора
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
