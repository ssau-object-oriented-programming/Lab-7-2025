package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;
    private boolean running = true;
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    @Override
    public void run() {
        while (running) {
            synchronized (task) {
                if (!task.isExecuted()) {
                    System.out.println("Source " + task.getLeftX() + " " + task.getRightX() + " " + task.getStepIntegration());
                    double value = Functions.findIntegral(task.getFunction(), task.getLeftX(), task.getRightX(), task.getStepIntegration());
                    System.out.println("№" + task.getCurrentIteration() + ". Result " + task.getLeftX() + " " + task.getRightX() + " " + task.getStepIntegration() + " " + value);

                    task.incrementIteration();
                }
                running = task.getCurrentIteration() != task.getIterations();
            }
        }
    }
}
